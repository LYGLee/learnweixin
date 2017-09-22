package com.example.wechat.controller;

import com.example.wechat.service.WechatService;
import com.example.wechat.utils.CheckUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by liyuan on 2017/9/20
 */

@Controller
@RequestMapping("wechat")
public class AuthController {

    private final WechatService wechatService;

    @Autowired
    public AuthController(WechatService wechatService) {
        this.wechatService = wechatService;
    }

    /**
     * wechat公众号注册token校验
     * @param signature 微信加密签名
     * @param timestamp 时间戳
     * @param nonce     随机数
     * @param echostr   随机字符串
     * @return echostr
     */
    @RequestMapping(value = "/checkToken", method = RequestMethod.GET)
    @ResponseBody
    public String checkToken(@RequestParam String signature, @RequestParam String timestamp, @RequestParam String nonce, @RequestParam String echostr) {
        return wechatService.getToken(signature, timestamp, nonce, echostr);
    }


    /**
     * 获取 access_token
     * @return access_token
     */
    @RequestMapping(value = "/getAccessToken", method = RequestMethod.GET)
    @ResponseBody
    public String getAccessToken() {
        return wechatService.getAccessToken();
    }


    /**
     * 获取微信服务器IP地址
     * @return {"ip_list": ["127.0.0.1","127.0.0.2","101.226.103.0/25"]}
     */
    @RequestMapping(value = "/getServerIP", method = RequestMethod.GET)
    @ResponseBody
    public String getServerIP() {
        return wechatService.ServerIP();
    }





}
