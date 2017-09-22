package com.example.wechat.service;


/**
 * Created by liyuan on 2017/9/21
 */
public interface WechatService {

    /**
     * wechat公众号注册token校验
     * @param signature 微信加密签名
     * @param timestamp 时间戳
     * @param nonce     随机数
     * @param echostr   随机字符串
     *
     * @return echostr
     */
    String getToken(String signature, String timestamp, String nonce, String echostr);

    /**
     * 获取 access_token
     * access_token是公众号的全局唯一接口调用凭据，公众号调用各接口时都需使用access_token。
     * access_token的存储至少要保留512个字符空间。access_token的有效期目前为2个小时，需定时刷新，
     * 重复获取将导致上次获取的access_token失效。
     *
     * @return {"access_token":"ACCESS_TOKEN","expires_in":7200}
     * access_token获取到的凭证  expires_in凭证有效时间，单位：秒
     */
    String getAccessToken();

    /**
     * 刷新token
     */
    String refreshToken();

    /**
     * 获取微信服务器IP地址
     * 如果公众号基于安全等考虑，需要获知微信服务器的IP地址列表，
     * 以便进行相关限制，可以通过该接口获得微信服务器IP地址列表或者IP网段信息。
     * @return {"ip_list": ["127.0.0.1","127.0.0.2","101.226.103.0/25"]}
     * ip_list 微信服务器IP地址列表
     */
    String ServerIP();


    String createMenu();

}
