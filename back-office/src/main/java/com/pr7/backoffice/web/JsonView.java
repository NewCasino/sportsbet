/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pr7.backoffice.web;

import flexjson.JSONSerializer;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.View;

/**
 *
 * @author Admin
 */
public class JsonView implements View {
    private Map<String, Object> dataMap = new HashMap<String, Object>();
    static private JSONSerializer SERIALIZER = new JSONSerializer().exclude("*.class");
    private JSONSerializer instanceSerializer;    

    public Map<String, Object> getDataMap() {
        return dataMap;
    }

    /**
     * construct a normal JSON response
     */
    public JsonView() {
    }

    /**
     * chainable method to build the json response (following the builder pattern),
     * using same key will overwrite the prev value
     *
     * @param key
     * @param value
     * @return this
     */
    public JsonView put(String key, Object value) {
        dataMap.put(key, value);
        return this;
    }

    /**
     * using your own serializer to do the serialization, otherwise, a system wise default json serializer will be used
     *
     * @param serializer
     * @return
     */
    public JsonView with(JSONSerializer serializer) {
        instanceSerializer = serializer;
        return this;
    }

    private JSONSerializer getSerializer() {
        return instanceSerializer == null ? SERIALIZER : instanceSerializer;
    }

    @Override
    public String getContentType() {
        return "application/json;charset=UTF-8";
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request,
            HttpServletResponse response) throws Exception {        
        response.addHeader("Pragma", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        response.addHeader("Expires", "Mon, 26 Jul 1997 05:00:00 GMT");
        response.setContentType(getContentType());
        response.getWriter().write(getSerializer().deepSerialize(dataMap));
        //response.addHeader("X-Last-Serial", String.valueOf(hash));
    }

    @Override
    public String toString() {
        return getSerializer().deepSerialize(dataMap);
    }

}
