package com.pr7.util;

import java.util.Collection;
import java.util.Map;

/**
 *
 * @author Admin
 */
public final class Ensure {

    /**
     * 
     * @param obj 
     * @throws IllegalArgumentException when obj is null 
     * @return the original object itself
     */
    public static <T> T notNull(T obj) {
        return notNull(obj, "argument can not be null");
    }

    /**
     * 
     * @param obj
     * @param errorMsgTemplate 
     * @param param param for String.format of the errorMsgTemplate
     * @throws IllegalArgumentException when obj is null 
     * @return the original object itself
     */
    public static <T> T notNull(T obj, String errorMsgTemplate, Object... param) {
        if (obj == null) {
            throw new AssertionException(String.format(errorMsgTemplate, param));
        }
        return obj;
    }
    
    public static <T> T isNull(T obj, String errorMsgTemplate, Object... param) {
        if (obj != null) {
            throw new AssertionException(String.format(errorMsgTemplate, param));
        }
        return obj;
    }
    
    public static <T> T equals(T obj1, T obj2, String errorMsgTemplate, Object... param) {
        if (!obj1.equals(obj2)) {
            throw new AssertionException(String.format(errorMsgTemplate, param));
        }
        return obj1;
    }
    
    public static <T> String contain(String obj, T[] items, String errorMsgTemplate, Object... param) {
        for (T item: items) {
            if (obj.equals(item.toString())) {
                return obj;
            }
        }
        throw new AssertionException(String.format(errorMsgTemplate, param));
    }

    /**
     * ensure the collection of the expected size 
     * 
     * @param collection
     * @param expectedSize 
     * @return the original object itself
     */
    public static Collection size(Collection collection, int expectedSize) {
        notNull(collection);
        if (collection.size() != expectedSize) {
            throw new AssertionException("expected collection size " + expectedSize);
        }
        return collection;
    }

    /**
     * ensure the collection of the expected size 
     * 
     * @param map
     * @param expectedSize 
     * @throws com.spero.player.tote.util.Ensure.AssertionException is 
     * @return the original object itself
     */
    public static Map size(Map map, int expectedSize) {
        notNull(map);
        if (map.size() != expectedSize) {
            throw new AssertionException("expected collection size " + expectedSize);
        }
        return map;
    }

    /**
     * 
     * @param collection
     * @throws com.spero.player.tote.util.Ensure.AssertionException if collection == null or collection.isEmpty()
     * @return the original object itself
     */
    public static Collection notEmpty(Collection collection) throws AssertionException {
        notNull(collection);
        if (collection.isEmpty()) {
            throw new AssertionException("collection is empty");
        }
        return collection;
    }

    /**
     * 
     * @param map
     * @throws com.spero.player.tote.util.Ensure.AssertionException if map == null or map.isEmpty()
     * @return the original object itself
     */
    public static Map notEmpty(Map map) throws AssertionException {
        notNull(map);
        if (map.isEmpty()) {
            throw new AssertionException("collection is empty");
        }
        return map;
    }

    /**
     * marker exception
     */
    public static class AssertionException extends RuntimeException {

        public AssertionException(String msg) {
            super(msg);
        }
    }
}
