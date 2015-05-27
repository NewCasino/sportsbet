package com.pr7.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class ConnectionHelper {

    private static final Logger _logger = LogManager.getLogger(ConnectionHelper.class);

    // default socket timeout (SO_TIMEOUT) in milliseconds which is the timeout for waiting for data.
    private static int READ_TIMEOUT = 900000;
    
    // timeout in milliseconds until a connection is established.
    private static int CONNECT_TIMEOUT = 60000;
    
   
    
    // set READ_TIMEOUT and CONNECT_TIMEOUT value from JNDI
    static {
        String jndiName="";
        Object [] o = Thread.currentThread().getStackTrace();
        for (int i=0;i<o.length;i++){
            String stackTraceStr = o[i].toString();
            if (stackTraceStr.contains(".membersite.")){
                jndiName = "PR7_MEMBERSITE_SB";
                break;
            }else if (stackTraceStr.contains(".spi.")){
                jndiName = "PR7_SB_SPI";
                break;
            }else if (stackTraceStr.contains(".scraper.")){
                jndiName = "PR7_SCRAPER_SB";
                break;
            }else if (stackTraceStr.contains(".backoffice.")){
                jndiName = "PR7_BO_SB";
                break;
            }else if (stackTraceStr.contains(".affiliate.")){
                jndiName = "PR7_AFFILIATE_SB";
                break;
            }else if (stackTraceStr.contains(".casino.")){
                jndiName = "PR7_CASINO_SPI";
                break;
            }
        }
        
        String readTimeoutStr = PropertyHelper.getFromJndi(jndiName, "http.read.timeout");
        String connectTimeoutStr = PropertyHelper.getFromJndi(jndiName, "http.connect.timeout");
        READ_TIMEOUT = Integer.parseInt(readTimeoutStr);
        CONNECT_TIMEOUT = Integer.parseInt(connectTimeoutStr);
        
        System.out.println("ConnectionHelper init ::JNDI NAME = "+jndiName+",  READ_TIMEOUT = " + READ_TIMEOUT+", CONNECT_TIMEOUT = "+CONNECT_TIMEOUT);
    }
    /**
     *
     * @param url
     * @return
     */    
    public static String getResponseAsString(String url) {
        String result = null;
        try {
//            _logger.debug("Connecting to " + url);
//
//            HttpParams httpParameters = new BasicHttpParams();
//            // Set the timeout in milliseconds until a connection is established.
//            int timeoutConnection = 10000;
//            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
//            // Set the default socket timeout (SO_TIMEOUT) in milliseconds which is the timeout for waiting for data.
//            int timeoutSocket = 60000;
//            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
//
//            DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
//            HttpGet httpget = new HttpGet(url);
//            HttpResponse response = httpclient.execute(httpget);
//            HttpEntity entity = response.getEntity();
//            result = IOUtils.toString(entity.getContent(), "UTF-8");
//            _logger.debug("DONE connecting to " + url);
            return getFromUrl(url, "").getBodyAsString();
        } catch (Exception e) {
            _logger.error(e.getMessage(), e);
        }
        return result;
    }

    public static ConnectionResponse connectToUrl(String url, String method, String requestPayload, Map<String, String> requestHeader) throws MalformedURLException, IOException{
        _logger.debug("connectToUrl:: method = " + method + ", connecting to " + url);
                
        //Trust all SSL connection, because we only get infomation.
        //Prevent exception when scraping some site using untrusted certificate.
        if (url.toLowerCase().startsWith("https")) {
            SelfSignedSSLTrust.install();
        }
        if ("GET".equalsIgnoreCase(method) && StringUtils.isNotBlank(requestPayload)) {
            url = url + (url.indexOf("?") != -1 ? "&" : "?") + requestPayload;
            requestPayload = "";
        }
      
         // Send data
        URL urlObj = new URL(url);
        //HttpURLConnection.setFollowRedirects(false);
        HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection(); 
        if (method!=null){
            conn.setRequestMethod(method);
        }
        conn.setInstanceFollowRedirects(false);
        //Add timeout to prevent thread blocking, especially scraper 
        conn.setConnectTimeout(CONNECT_TIMEOUT);
        conn.setReadTimeout(READ_TIMEOUT);        
        
        for (Entry<String, String> header : requestHeader.entrySet()) {
            conn.setRequestProperty(header.getKey().toLowerCase(), header.getValue());
        }
        if(!requestHeader.containsKey("Host") && !requestHeader.containsKey("host")){
            conn.setRequestProperty("host", urlObj.getHost());
        }
        
        requestPayload = StringUtils.defaultIfBlank(requestPayload, "");
        
        if ("POST".equalsIgnoreCase(method)) {
            if(!requestHeader.containsKey("Content-Type") && !requestHeader.containsKey("content-type")){
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            }
            
            conn.setDoOutput(true);
            conn.setRequestProperty("content-length", requestPayload.getBytes("UTF-8").length + "");
//            DataOutputStream postStream = new DataOutputStream(conn.getOutputStream());
            IOUtils.write(requestPayload , conn.getOutputStream(), "UTF-8");            
//            postStream.writeBytes(requestPayload);
//            postStream.flush();
//            postStream.close();
        }

        InputStream stream;
        try {
            stream = conn.getInputStream();
        } catch (IOException ex) {
            stream = conn.getErrorStream();
        }
//        if ("gzip".equalsIgnoreCase(conn.getContentEncoding())) {
//            stream = new GZIPInputStream(stream);
//        }
//        
        if (stream == null) {
//            throw new ConnectException("Error response");
            stream = new ByteArrayInputStream("".getBytes());
        }
        
        ConnectionResponse connectionResponse = new ConnectionResponse(IOUtils.toByteArray(stream), conn.getResponseCode(), conn.getContentEncoding());
                
        Map<String,String> responseHeaders = new HashMap<String, String>();        
        for (Entry<String, List<String>> header : conn.getHeaderFields().entrySet()) {                        
            String val = "";
            int count = 0;
            for (String value : header.getValue()) {
                if("Set-Cookie".equalsIgnoreCase(header.getKey())){
                    connectionResponse.getCookies().add(value);
                }
                if(count++ > 0) val += "; ";
                val += value;
            }
            if (header.getKey() != null) {
                responseHeaders.put(header.getKey(), val);
            }
        }
        
        connectionResponse.setHeaders(responseHeaders);        
        return connectionResponse;
    }

    public static ConnectionResponse connectToUrl(String url, String method, Map<String, String> params, Map<String, String> headers) throws Exception {
        StringBuilder requestParams = new StringBuilder();
        if (params != null) {
            for (Entry<String, String> param : params.entrySet()) {
                requestParams.append(URLEncoder.encode(param.getKey(), "UTF-8")).append("=").append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8")).append("&");
            }
        }

        return connectToUrl(url, method, requestParams.toString(), headers);
    }

    public static ConnectionResponse connectToUrl(String url, String method, Map<String, String> params, String cookies) throws Exception {
        HashMap<String, String> headers = new HashMap<String, String>();
        if (StringUtils.isNotBlank(cookies)) {
            headers.put("Cookie", cookies);
        }
        headers.put("Connection", "keep-alive");
        return connectToUrl(url, method, params, headers);
    }

    public static ConnectionResponse postToUrl(String url, String requestPayLoad, Map<String, String> requestHeaders) throws Exception {
        return connectToUrl(url, "POST", requestPayLoad, requestHeaders);
    }

    public static ConnectionResponse postToUrl(String url, String requestPayLoad, Map<String, String> requestHeaders, int retries) throws Exception {
        try {
            return connectToUrl(url, "POST", requestPayLoad, requestHeaders);
        } catch (IOException ex) {
            if (retries <= 0) {
                ConnectException exception = new ConnectException("Max retries reached due to http connection error");
                exception.initCause(ex);
                exception.setStackTrace(ex.getStackTrace());
                throw exception;
            }
            Thread.sleep(3000);
            return postToUrl(url, requestPayLoad, requestHeaders, --retries);
        }
    }
    
    public static ConnectionResponse postToUrl(String url, Map<String, String> params,  Map<String, String> requestHeaders) throws Exception {
        return connectToUrl(url, "POST" ,params, requestHeaders);
    }

    public static ConnectionResponse postToUrl(String url, Map<String, String> params, String cookies) throws Exception {
        return connectToUrl(url, "POST", params, cookies);
    }

    public static ConnectionResponse getFromUrl(String url, String cookies) throws Exception {
        return connectToUrl(url, "GET", null, cookies);
    }

    public static ConnectionResponse getFromUrl(String url, String cookies, int retries) throws Exception {
        try {
            return connectToUrl(url, "GET", null, cookies);
        } catch (IOException ex) {
            if (retries <= 0) {
                ConnectException exception = new ConnectException("Max retries reached due to http connection error");
                exception.initCause(ex);
                exception.setStackTrace(ex.getStackTrace());
                throw exception;
            }
            Thread.sleep(3000);
            return getFromUrl(url, cookies, --retries);
        }
    }

    public static ConnectionResponse getFromUrl(String url, Map<String, String> params, String cookies) throws Exception {
        return connectToUrl(url, "GET", params, cookies);
    }

    public static ConnectionResponse getFromUrl(String url, Map<String, String> params, Map<String, String> requestHeader) throws Exception {
        return connectToUrl(url, "GET", params, requestHeader);
    }

    @Deprecated
    public static Map<String, Object> connectUrl(String url, String method, String params, Map<String, String> requestHeader) throws Exception {
        return connectUrl(url, method, params, requestHeader, false);
    }

    /**
     *
     * @param url
     * @param method
     * @param params
     * @param requestHeader
     * @return response header set. "body" is response content in string,
     * "bodyarray" is response content in byte[], the rest is response header
     * @throws Exception
     */
    @Deprecated
    public static Map<String, Object> connectUrl(String url, String method, String params, Map<String, String> requestHeader, boolean httpsSelftTrust) throws Exception {
        Map<String, Object> responseHeaderWithBody = new HashMap<String, Object>();

        _logger.debug("connectUrl Deprecated:: method = " + method + ", connecting to " + url);

        //Trust all SSL connection, because we only get infomation.
        //Prevent exception when scraping some site using untrusted certificate.
        if (url.toLowerCase().startsWith("https") && httpsSelftTrust) {
            SelfSignedSSLTrust.install();
        }

        // Send data
        URL urlObj = new URL(url);
        //HttpURLConnection.setFollowRedirects(false);
        HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
        conn.setRequestMethod(method);
        conn.setInstanceFollowRedirects(false);
        //Add timeout to prevent thread blocking, especially scraper 
        conn.setConnectTimeout(CONNECT_TIMEOUT);
        conn.setReadTimeout(READ_TIMEOUT);

        for (Entry<String, String> header : requestHeader.entrySet()) {
            conn.setRequestProperty(header.getKey(), header.getValue());
        }
        conn.setRequestProperty("Host", urlObj.getHost());
        
        OutputStreamWriter wr = null;
        if (StringUtils.isNotBlank(params)) {
            conn.setRequestProperty("Content-Length", params.getBytes().length + "");
            conn.setDoOutput(true);
            wr = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");

            wr.write(params);
            wr.flush();
        } else {
            conn.setRequestProperty("Content-Length", 0 + "");
        }

        // Get the response
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // Fake code simulating the copy
        // You can generally do better with nio if you need...
        // And please, unlike me, do something about the Exceptions :D
        byte[] buffer = new byte[1024];
        int len;

        InputStream stream;
                
        try {
            stream = conn.getInputStream();
        } catch (Exception ex) {
            _logger.error(ex);
            stream = conn.getErrorStream();
        }
        if ("gzip".equalsIgnoreCase(conn.getContentEncoding())) {
            stream = new GZIPInputStream(stream);
        }

        while ((len = stream.read(buffer)) > 0) {
            baos.write(buffer, 0, len);
        }
        baos.flush();

        String contentType = conn.getHeaderField("Content-Type");
        String charset = null;
        if (contentType != null) {
            for (String param : contentType.replace(" ", "").split(";")) {
                if (param.startsWith("charset=")) {
                    charset = param.split("=")[1];
                    break;
                }
            }
        }

        if (charset == null) {
            charset = "UTF-8";
        }
        _logger.debug("contentType = " + contentType + ", charset = " + charset);

        InputStreamReader inputStreamReader = new InputStreamReader(new ByteArrayInputStream(baos.toByteArray()), charset);

        BufferedReader rd = new BufferedReader(inputStreamReader);
        StringBuilder builder = new StringBuilder();
        String line = "";
        while ((line = rd.readLine()) != null) {
            builder.append(line).append("\n");
        }

        responseHeaderWithBody.put("body", builder.toString().trim());
        responseHeaderWithBody.put("bodyarray", IOUtils.toByteArray(new ByteArrayInputStream(baos.toByteArray())));
        responseHeaderWithBody.put("responsecode", String.valueOf(conn.getResponseCode()));

        if (wr != null) {
            wr.close();
        }

        rd.close();
        baos.close();

        for (Entry<String, List<String>> header : conn.getHeaderFields().entrySet()) {
            String val = "";
            int count = 0;
            for (String value : header.getValue()) {
                if(count++ > 0) val += "; ";
                val += value;
            }
            if (header.getKey() != null) {

                /*
                 * if ("Set-Cookie".equalsIgnoreCase(header.getKey())) { Matcher
                 * match = Pattern.compile("(.+?)\\=(.+?);").matcher(val); if
                 * (match.find()) { String cookies =
                 * requestHeader.get("Cookie"); cookies =
                 * cookies.replaceAll(String.format("", ""), match.group(2)); }
                }
                 */

                responseHeaderWithBody.put(header.getKey(), val);
            }
        }


        return responseHeaderWithBody;
    }

    @Deprecated    
    public static Map<String, Object> connectUrlWithGzip(String url, String method, String params, Map<String, String> requestHeader) throws Exception {
        Map<String, Object> responseHeaderWithBody = new HashMap<String, Object>();

        _logger.debug("connectUrlWithGzip:: method = " + method + ", connecting to " + url);

        // Send data
        URL urlObj = new URL(url);
        HttpURLConnection.setFollowRedirects(false);
        HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
        conn.setRequestMethod(method);
        //Add timeout to prevent thread blocking, especially scraper 
        conn.setConnectTimeout(CONNECT_TIMEOUT);
        conn.setReadTimeout(READ_TIMEOUT);

        for (Entry<String, String> header : requestHeader.entrySet()) {
            conn.setRequestProperty(header.getKey(), header.getValue());
        }
        conn.setRequestProperty("Host", urlObj.getHost());
        conn.setDoOutput(true);
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");

        wr.write(params);
        wr.flush();

        // Get the response
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // Fake code simulating the copy
        // You can generally do better with nio if you need...
        // And please, unlike me, do something about the Exceptions :D
        byte[] buffer = new byte[1024];
        int len;
        while ((len = conn.getInputStream().read(buffer)) > 0) {
            baos.write(buffer, 0, len);
        }
        baos.flush();

        String contentType = conn.getHeaderField("Content-Type");
        String charset = null;
        if (contentType != null) {
            for (String param : contentType.replace(" ", "").split(";")) {
                if (param.startsWith("charset=")) {
                    charset = param.split("=")[1];
                    break;
                }
            }
        }

        if (charset == null) {
            charset = "UTF-8";
        }
        _logger.debug("contentType = " + contentType + ", charset = " + charset);
        GZIPInputStream inputStreamReader = new GZIPInputStream(new ByteArrayInputStream(baos.toByteArray()));

        StringBuffer builder = new StringBuffer();

        byte tByte[] = new byte[1024];

        while (true) {
            int iLength = inputStreamReader.read(tByte, 0, 1024); // <-- Error comes here
            if (iLength < 0) {
                break;
            }
            builder.append(new String(tByte, 0, iLength));
        }

//        BufferedReader rd = new BufferedReader(inputStreamReader);
//        StringBuilder builder = new StringBuilder();
//        String line = "";
//        while ((line = rd.readLine()) != null) {
//            builder.append(line).append("\n");
//        }

        responseHeaderWithBody.put("body", builder.toString());
        responseHeaderWithBody.put("bodyarray", IOUtils.toByteArray(new ByteArrayInputStream(baos.toByteArray())));
        responseHeaderWithBody.put("responsecode", String.valueOf(conn.getResponseCode()));

        wr.close();
        //rd.close();        
        baos.close();

        for (Entry<String, List<String>> header : conn.getHeaderFields().entrySet()) {
            String val = "";
            int count = 0;
            for (String value : header.getValue()) {
                if(count++ > 0) val += "; ";
                val += value;
            }
            if (header.getKey() != null) {
                responseHeaderWithBody.put(header.getKey(), val);
            }
        }


        return responseHeaderWithBody;
    }

    /**
     * Extract response set cookies as request cookies (excludes ",path,expires,httponly,Secure,Domain,")
     * @param src Set cookies string, (multi set cookies separate by ;)
     * @return
     */
    public static String extractCookieValues(String src) {
        String excludeList = ",path,expires,httponly,Secure,Domain,Version,Max-Age,";
        StringBuilder stringBuilder = new StringBuilder();
        String[] array = src.split(";");
        for (String cookiePair : array) {
            if (cookiePair.contains("=")) {
                String[] cookie = cookiePair.split("=", 2);
                if (!StringUtils.containsIgnoreCase(excludeList, "," + cookie[0].trim() + ",")
                        && !"deleted".equalsIgnoreCase(cookie[1].trim())) {
                    stringBuilder.append(cookie[0].trim()).append("=").append(cookie[1].trim()).append(";");
                }
            }
        }

        return stringBuilder.toString();
    }

    public static String printHeader(Map<?, ?> header) {
        StringBuilder builder = new StringBuilder();

        for (Entry<?, ?> entry : header.entrySet()) {
            builder.append(entry.getKey()).append(" = ").append(entry.getValue()).append("\n");
        }

        return builder.toString();
    }
    
}
