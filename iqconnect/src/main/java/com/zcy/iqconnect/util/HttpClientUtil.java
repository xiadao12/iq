package com.zcy.iqconnect.util;

import com.zcy.iqconnect.model.HttpHeader;
import com.zcy.iqconnect.model.RepeatHttpParamList;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Map;

/**
 * create date : 2018/12/1
 */
public class HttpClientUtil {

    /**
     * get请求
     *
     * @return
     */
    public static String doGet(String url) {
        try {
            HttpClient client = HttpClients.createDefault();
            //发送get请求
            HttpGet request = new HttpGet(url);
            HttpResponse response = client.execute(request);

            /**请求发送成功，并得到响应**/
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                /**读取服务器返回过来的json字符串数据**/
                String strResult = EntityUtils.toString(response.getEntity());

                return strResult;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * post请求（用于请求json格式的参数）
     *
     * @param url
     * @return
     */
    public static String doPost(String url, Map<String, Object> paramMap, HttpHeader httpHeader) throws Exception {

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);// 创建httpPost

        //设置header
        initHeader(httpPost, httpHeader);

        //设置参数
        initEntity(httpPost, paramMap, httpHeader);

        CloseableHttpResponse response = null;

        try {
            response = httpclient.execute(httpPost);
            StatusLine status = response.getStatusLine();
            int state = status.getStatusCode();
            if (state == HttpStatus.SC_OK) {
                HttpEntity responseEntity = response.getEntity();
                String jsonString = EntityUtils.toString(responseEntity);
                return jsonString;
            } else {
                System.out.println("请求返回:" + state + "(" + url + ")");
            }
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 对参数进行拼接，类似于：name=zhangsan&age=20
     *
     * @param paramMap
     * @return
     */
    private static String jonitParams(Map<String, Object> paramMap) throws Exception {

        if (paramMap == null || paramMap.size() <= 0) {
            return "";
        }

        String result = "";

        //遍历参数
        for (String key : paramMap.keySet()) {
            Object value = paramMap.get(key);
            if (value == null) {
                break;
            }

            //如果是需要重复参数
            if (value instanceof RepeatHttpParamList) {
                RepeatHttpParamList repeatHttpParamList = (RepeatHttpParamList) value;
                for (Object repeatObjectvalue : repeatHttpParamList) {
                    result = result + key + "=" + repeatObjectvalue + "&";
                }
                continue;
            }

            //如果是正常参数
            result = result + key + "=" + value + "&";
        }

        //去除最后一个&
        result = result.substring(0, result.length() - 1);

        return result;
    }

    /**
     * 设置header（get和post都可设置）
     *
     * @param httpMessage(可以使HttpGet或PostGet)
     * @param httpHeader
     */
    private static void initHeader(HttpMessage httpMessage, HttpHeader httpHeader) throws Exception {
        if (httpMessage == null || httpHeader == null) {
            return;
        }

        String accept = httpHeader.getAccept();
        if (!StringUtils.isEmpty(accept)) {
            httpMessage.setHeader("Accept", accept);
        }

        String acceptEncoding = httpHeader.getAcceptEncoding();
        if (!StringUtils.isEmpty(acceptEncoding)) {
            httpMessage.setHeader("Accept-Encoding", acceptEncoding);
        }

        String acceptLanguage = httpHeader.getAcceptLanguage();
        if (!StringUtils.isEmpty(acceptLanguage)) {
            httpMessage.setHeader("Accept-Language", acceptLanguage);
        }

        String connection = httpHeader.getConnection();
        if (!StringUtils.isEmpty(connection)) {
            httpMessage.setHeader("Connection", connection);
        }

        String contentType = httpHeader.getContentType();
        if (!StringUtils.isEmpty(contentType)) {
            httpMessage.setHeader("Content-Type", contentType);
        }

        String cookie = httpHeader.getCookie();
        if (!StringUtils.isEmpty(cookie)) {
            httpMessage.setHeader("Cookie", cookie);
        }

        String host = httpHeader.getHost();
        if (!StringUtils.isEmpty(host)) {
            httpMessage.setHeader("Host", host);
        }

        String origin = httpHeader.getOrigin();
        if (!StringUtils.isEmpty(origin)) {
            httpMessage.setHeader("Origin", origin);
        }

        String referer = httpHeader.getReferer();
        if (!StringUtils.isEmpty(referer)) {
            httpMessage.setHeader("Referer", referer);
        }

        String userAgent = httpHeader.getUserAgent();
        if (!StringUtils.isEmpty(userAgent)) {
            httpMessage.setHeader("User-Agent", userAgent);
        }

        String xRequestedWith = httpHeader.getxRequestedWith();
        if (!StringUtils.isEmpty(xRequestedWith)) {
            httpMessage.setHeader("X-Requested-With", xRequestedWith);
        }
    }

    /**
     * 设置参数
     *
     * @param paramMap
     * @param httpHeader
     */
    private static void initEntity(HttpPost httpPost, Map<String, Object> paramMap, HttpHeader httpHeader) throws Exception {
        if (httpPost == null || paramMap == null || paramMap.size() <= 0) {
            return;
        }

        String charSet = "UTF-8";

        if (httpHeader != null && httpHeader.getContentType() != null) {
            String contentType = httpHeader.getContentType();
            //如果是multipart/form-data提交，则涉及到文件上传
            if (contentType.contains("multipart/form-data")) {
                String paramsString = jonitParams(paramMap);
                StringEntity entity = new StringEntity(paramsString, charSet);
                httpPost.setEntity(entity);
                //处理
                return;
            }

            if (contentType.contains("application/json")) {
                //处理
                return;
            }

            //如果是post提交
        }

        //其他所有情况都默认为application/x-www-form-urlencoded方式提交数据
        String paramsString = jonitParams(paramMap);
        StringEntity entity = new StringEntity(paramsString, charSet);
        httpPost.setEntity(entity);
    }


}