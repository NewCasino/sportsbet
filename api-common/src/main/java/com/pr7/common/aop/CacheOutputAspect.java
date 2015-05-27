package com.pr7.common.aop;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.Configuration;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

@Aspect
public class CacheOutputAspect {
    private static final Logger logger = LogManager.getLogger(CacheOutputAspect.class);
    private final static ConcurrentHashMap<String, FutureTask<?>> methodInvocationBuffer = new ConcurrentHashMap<String, FutureTask<? extends Object>>(200);
    private Cache cache;
    final private static String CACHE_NAME = "CacheOutputAspect";

    @PostConstruct
    void init() {

//        final CacheManager cm = CacheManager.getInstance();
        
        CacheConfiguration cacheConfiguration = new CacheConfiguration(CACHE_NAME, 0);
        cacheConfiguration.setOverflowToDisk(false);
        Configuration configuration = new Configuration();
        configuration.setDefaultCacheConfiguration(cacheConfiguration);        
        CacheManager cm = CacheManager.create(configuration);//Singleton
        
        synchronized (CACHE_NAME) {
            if (cm.getCache(CACHE_NAME) == null) {                
                cm.addCache(CACHE_NAME);
                logger.debug("cache " + CACHE_NAME + " added");
            } else {
                logger.warn("multiple instance of the CachOutputAspect is configured. are you running multiple instance of spring container?");
            }
        }

        cache = cm.getCache(CACHE_NAME);

    }

    @PreDestroy
    void dispose() {
        CacheManager.getInstance().shutdown();
    }

    @Around(value = "@annotation(cacheOutput)", argNames = "cacheOutput")
    public Object checkCache(final ProceedingJoinPoint pjp, final CacheOutput cacheOutput) throws Throwable {

        MethodSignature signature = (MethodSignature) pjp.getSignature();
        final Method method = signature.getMethod();
        final String key = keygen(method, pjp.getArgs());
        Element element = cache.get(key);

        if (element != null) {
            return element.getObjectValue();
        } else {
            FutureTask<?> task = methodInvocationBuffer.get(key);

            if (task == null) {
                // create the task
                Callable<Object> callable = new Callable<Object>() {

                    @Override
                    public Object call() throws Exception {
                        Object result = null;
                        try {
                            result = pjp.proceed();
                        } catch (Throwable t) {

                            if (t instanceof Exception) {
                                throw (Exception) t;
                            }
                            // why this throwable is not an instance of Exception??
                            throw new Exception("expected the throwable to be an instance of Exception", t);
                        } finally {
                            if (result != null) {
                                Element el = new Element(key, result);
                                el.setTimeToLive(cacheOutput.ttl());
                                /*
                                 * here we put the element into the cache first, therefore before the task is removed from the buffer
                                 * when the sub sequent request hits the interceptor, they will get the data from the cache first
                                 */
                                cache.putQuiet(el);
                                methodInvocationBuffer.remove(key);
                            } else {
                                cache.remove(key);
                                methodInvocationBuffer.remove(key);
                            }
                        }
                        return result;
                    }
                };

                task = new FutureTask<Object>(callable);
                FutureTask<?> _task = methodInvocationBuffer.putIfAbsent(key, task);

                if (_task == null) {
                    task.run();
                } else {
                    task = _task;
                }

            }

            try {
                return task.get();
            } catch (ExecutionException e) {
                throw e.getCause();
            }
        }
    }

    private String keygen(Method method, Object[] param) {

        StringBuilder builder = new StringBuilder(method.getName());
        for (Object value : param) {
            builder.append(",");
            builder.append(value == null ? "" : value.toString());
        }

        return builder.toString();

    }
}
