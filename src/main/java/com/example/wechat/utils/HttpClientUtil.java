package com.example.wechat.utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.IOException;

import java.util.List;
import java.util.Map;


/**
 * @author liyuan
 * @create 2017-09-21 11:01
 **/
public class HttpClientUtil {

    private static Logger logger = Logger.getLogger(HttpClientUtil.class);
    private static CookieStore cookieStore = new BasicCookieStore();
    private static CloseableHttpClient httpCilent = HttpClients.custom().setDefaultCookieStore(cookieStore).build();

    public static String httpPost(String url, List<BasicNameValuePair> list) {
        // 配置超时时间
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(1000).setConnectionRequestTimeout(1000)
                .setSocketTimeout(1000).setRedirectsEnabled(true).build();

        HttpPost httpPost = new HttpPost(url);
        // 设置超时时间
        httpPost.setConfig(requestConfig);

        String strResult = "";
        int StatusCode=404;
        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, "UTF-8");
            // 设置post求情参数
            httpPost.setEntity(entity);
            HttpResponse httpResponse = httpCilent.execute(httpPost);
            if (httpResponse != null) {
                StatusCode=httpResponse.getStatusLine().getStatusCode();
                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    strResult = EntityUtils.toString(httpResponse.getEntity());
                    logger.info("post/"+StatusCode+":"+strResult);
                    return strResult;
                }  else {
                    strResult = "Error Response: " + httpResponse.getStatusLine().toString();
                    logger.info("post/"+StatusCode+":"+strResult);
                    strResult=null;
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            // do something...
        }
        return strResult;
    }

    public static void httpGet(String url, Map<String, String> params) {
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000) // 设置连接超时时间
                .setConnectionRequestTimeout(5000) // 设置请求超时时间
                .setSocketTimeout(5000).setRedirectsEnabled(true)// 默认允许自动重定向
                .build();
        HttpGet httpGet2 = new HttpGet(url);
        httpGet2.setConfig(requestConfig);
        String srtResult =null;
        int StatusCode=404;
        try {
            HttpResponse httpResponse = httpCilent.execute(httpGet2);
            StatusCode=httpResponse.getStatusLine().getStatusCode();
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                srtResult = EntityUtils.toString(httpResponse.getEntity());// 获得返回的结果
                logger.info("get/"+StatusCode+":"+srtResult);
            } else {
                srtResult = EntityUtils.toString(httpResponse.getEntity());// 获得返回的结果
                logger.info("get/"+StatusCode+":"+srtResult);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // do something...
        }
    }
}
