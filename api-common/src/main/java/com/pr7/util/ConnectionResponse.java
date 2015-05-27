/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pr7.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * @author Admin
 */
public class ConnectionResponse {
    private static final Logger _logger = LogManager.getLogger(ConnectionResponse.class);
    private String bodyAsString;
    private byte[] data;
    private byte[] originalData;
    private Map<String, String> headers;
    List<String> cookies;
    private int status;
    String contentEncoding;
    
    public ConnectionResponse(byte[] data, int status, String contentEncoding) {
        this.originalData = data;
        this.status = status;
        this.data = null;
        this.contentEncoding = contentEncoding;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getBodyAsString() {
        String contentType = StringUtils.defaultIfBlank(getHeaders().get("Content-Type"), "");
        if (originalData == null || contentType.contains("image")) {
            return "";
        }
        
        if (bodyAsString == null) {
            try {
                bodyAsString = new String(getData(), getCharsetFromContentType(contentType));
            } catch (UnsupportedEncodingException ex) {
                _logger.error(ex.getMessage());
                try {
                    bodyAsString = new String(getData(), getCharsetFromContentType("UTF-8"));
                } catch (UnsupportedEncodingException ex1) {                    
                    _logger.error(ex1.getMessage());
                }
            }
        }
        return bodyAsString;
    }

    public String getContentEncoding() {
        return contentEncoding;
    }

    public byte[] getOriginalData() {
        return originalData;
    }
    
    public byte[] getData() {
        if(data == null){
            try {
                data = deCompressData(originalData, contentEncoding);
            } catch (IOException ex) {
                _logger.error("Can not decompress data." , ex);
            }
        }
        return data;
    }

    public Map<String, String> getHeaders() {
        if (headers == null) {
            headers = new HashMap<String, String>();
        }
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public List<String> getCookies() {
        if(cookies == null) cookies = new ArrayList<String>();
        return cookies;
    }


    /**
     * Get Response cookies.
     *
     * @return Set-Cookie header value, empty if no Set-Cookie header.
     */
    public String getResponseCookies() {
        return StringUtils.defaultIfBlank(getHeaders().get("Set-Cookie"), "");
    }

    @Override
    public String toString() {
        return "ConnectionResponse{\n" + printHeader(getHeaders()) + "ResponseCode = " + status + "\nBody=\n" + getBodyAsString() + "}";
    }

    public static String printHeader(Map<String, String> header) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry : header.entrySet()) {
            builder.append(entry.getKey()).append(" = ").append(entry.getValue()).append("\n");
        }
        return builder.toString();
    }
    private static final Pattern charsetPattern = Pattern.compile("(?i)\\bcharset=\\s*\"?([^\\s;\"]*)");

    public static String getCharsetFromContentType(String contentType) {
        if (contentType == null) {
            return "UTF-8";
        }
        Matcher m = charsetPattern.matcher(contentType);
        if (m.find()) {
            return m.group(1).trim().toUpperCase();
        }
        return "UTF-8";
    }

    private static byte[] deCompressData(byte[] original, String encoding) throws IOException {
        if(original == null) return null;
        byte [] data;
        if("gzip".equalsIgnoreCase(encoding)){            
            GZIPInputStream gZIPInputStream = new GZIPInputStream(new ByteArrayInputStream(original));
            data = IOUtils.toByteArray(gZIPInputStream);
        }
        else if ("deflate".equalsIgnoreCase(encoding)){
            InflaterInputStream inflaterInputStream = new InflaterInputStream(new ByteArrayInputStream(original), new Inflater(true));            
            data = IOUtils.toByteArray(inflaterInputStream);
        }
        else{
            data = original;
        }
        return data;
    }
}
