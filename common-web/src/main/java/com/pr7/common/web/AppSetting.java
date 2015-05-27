package com.pr7.common.web;

import com.pr7.common.web.spring.support.SpringApplicationContext;
import java.io.InputStream;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author mark.ma
 */
@Component
public class AppSetting {
    private final static Logger logger = Logger.getLogger(AppSetting.class.getName());

    // default configuration properties
    private final static String DEFAULT_VERSION = "0.0.1";
    // refer to resources
    private final static String manifestPath = "/META-INF/MANIFEST.MF";
    private final static String manifestProperty = "Hudson-Build-Number";
    // runtime values
    
    @Value("${app.debug}")
    private String debugMode;
    
    @Value("${app.servicePath}")
    private String servicePath;
    
    @Value("${app.cdnPrefix}")
    private String cdnPrefix;
    
    private String version;

    public void init() {
        try {
            InputStream manifestStream = SpringApplicationContext.getCurrent().getResource(manifestPath).getInputStream();
            Manifest manifest = new Manifest(manifestStream);
            Attributes attributes = manifest.getMainAttributes();
            String hudsonBuildNumber = attributes.getValue(manifestProperty);

            if (!StringUtils.isBlank(hudsonBuildNumber)) {
                version = hudsonBuildNumber;
            } else {
                version = DEFAULT_VERSION;
                logger.log(Level.INFO, "Property ''{0}'' not be found in folder '{1}', will use default version {2}",
                        new Object[]{manifestProperty, manifestPath, DEFAULT_VERSION});
            }
        } catch (Exception ex) {
            version = DEFAULT_VERSION;
            logger.log(Level.WARNING, null, ex);
        }
        
        if (cdnPrefix == null) {
            cdnPrefix = "";
        }
        if (servicePath == null) {
            servicePath = "";
        }
    }

    public String getCdnPrefix() {
        return StringUtils.defaultIfBlank(cdnPrefix, "");
    }

    public boolean isDebugMode() {
        return debugMode.equalsIgnoreCase("true");
    }

    public String getServicePath() {
        return StringUtils.defaultIfBlank(servicePath, "");
    }

    public String getVersion() {
        if(StringUtils.isBlank(version)) init();
        return version;
    }

}
