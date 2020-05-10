package com.breez.utils;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

public class HttpClientUtils {

    private static Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);

    private static String getParams(Map<String,Object> map) {
        StringBuffer params = new StringBuffer();

        try {
            //使用EntrySet遍历map
            for (Map.Entry<String,Object> entry : map.entrySet()) {
                //拿到value,判断是不是字符串
                if(entry.getValue() instanceof String) {
                    //字符串在这里做一下编码,防止汉字乱码
                    params.append(entry.getKey() + "=" + URLEncoder.encode((String) entry.getValue(),"utf-8"));
                } else {
                    params.append(entry.getKey() + "=" + entry.getValue());
                }
                //添加连接符
                params.append("&");
            }
            //删除最后一个&符号
            params.deleteCharAt(params.length() - 1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return params.toString();
    }

    /**
     * 发送GET请求,不带参数
     * @param url 请求url
     * @return 服务器返回的内容
     */
    public static String requestGet(String url) {

        //创建GET请求
        HttpGet httpGet = new HttpGet(url);
        String entityString = "";


        try(
                //创建一个HttpClient
                CloseableHttpClient httpClient = HttpClientBuilder.create().build();
                //响应模型
                CloseableHttpResponse response = httpClient.execute(httpGet);
                ) {

            RequestConfig requestConfig = RequestConfig.custom()
                    //设置连接超时,单位ms
                    .setConnectTimeout(5000)
                    //设置请求超时时间,单位ms
                    .setConnectionRequestTimeout(5000)
                    //socket读写超时时间,单位ms
                    .setSocketTimeout(5000)
                    //设置是否允许重定向(默认为true)
                    .setRedirectsEnabled(true).build();
            httpGet.setConfig(requestConfig);

            logger.debug("URI:" + httpGet.getURI());

            //获得响应实体
            HttpEntity responseEntity = response.getEntity();

            logger.debug("响应状态为:" + response.getStatusLine());

            if(responseEntity != null) {
                logger.debug("响应长度为:" + responseEntity.getContentLength());

                entityString = EntityUtils.toString(responseEntity);
                logger.debug("响应内容为:" + entityString);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return entityString;
    }

    /**
     * 发送GET请求,带请求参数
     * @param url 请求url
     * @param map 请求参数,key-value形式.最后会拼接到url后面
     * @return 服务器返回的内容
     */
    public static String requestGet(String url, Map<String,Object> map) {

        String params = getParams(map);

        return requestGet(url + "?" + params);
    }

    /**
     * 发送无参POST请求
     * @param url 请求URL
     * @return 服务器返回的内容
     */
    public static String requestPost(String url) {

        //创建post请求
        HttpPost httpPost = new HttpPost(url);
        //设置ContentType(注:如果只是传普通参数的话,ContentType不一定非要用application/json)
        httpPost.setHeader("Content-Type", "application/json;charset=utf8");
        String entityString = "";

        try(
                //创建http客户端
                CloseableHttpClient httpClient = HttpClientBuilder.create().build();
                CloseableHttpResponse response = httpClient.execute(httpPost);
        ) {
            HttpEntity responseEntity = response.getEntity();

            logger.debug("URI:" + httpPost.getURI());
            logger.debug("响应状态为:" + response.getStatusLine());

            if(responseEntity != null) {
                logger.debug("响应长度为:" + responseEntity.getContentLength());

                entityString = EntityUtils.toString(responseEntity);
                logger.debug("响应内容为:" + entityString);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return entityString;
    }

    /**
     * 发送普通带参数的POST请求
     * @param url 请求地址
     * @param map 请求参数,key-value形式
     * @return 服务器返回的内容
     */
    public static String requestPost(String url, Map<String,Object> map) {
        String params = getParams(map);
        return requestPost(url + "?" + params);
    }

    /**
     * 发送对象参数(函数内使用FastJson将任意对象转换成JSON字符串进行发送)
     * 支持自定义对象 集合 Map等
     * @param url 请求URL
     * @return 服务器返回的内容
     */
    public static <T> String requestJsonPost(String url,T object) {

        //创建post请求
        HttpPost httpPost = new HttpPost(url);
        //设置ContentType(注:如果只是传普通参数的话,ContentType不一定非要用application/json)
        httpPost.setHeader("Content-Type", "application/json;charset=utf8");

        //服务器返回的结果
        String entityString = "";

        String jsonString = FastJsonUtils.entityToJsonString(object);
        StringEntity stringEntity = new StringEntity(jsonString,"UTF-8");

        //将entity放入请求体中
        httpPost.setEntity(stringEntity);

        try(
                //创建http客户端
                CloseableHttpClient httpClient = HttpClientBuilder.create().build();
                CloseableHttpResponse response = httpClient.execute(httpPost);
        ) {
            HttpEntity responseEntity = response.getEntity();
            logger.debug("URI:" + httpPost.getURI());
            logger.debug("响应状态为:" + response.getStatusLine());

            if(responseEntity != null) {
                logger.debug("响应长度为:" + responseEntity.getContentLength());

                entityString = EntityUtils.toString(responseEntity);
                logger.debug("响应内容为:" + entityString);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return entityString;
    }

}
