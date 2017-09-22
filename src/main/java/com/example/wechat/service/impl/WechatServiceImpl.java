package com.example.wechat.service.impl;

import com.example.wechat.object.Menu;
import com.example.wechat.object.TokenInfo;
import com.example.wechat.object.WechatError;
import com.example.wechat.service.WechatService;
import com.example.wechat.utils.CheckUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by liyuan on 2017/9/21
 */

@Service
public class WechatServiceImpl implements WechatService {

    private static final Logger logger = LoggerFactory.getLogger(WechatServiceImpl.class);

    private final RestTemplate restTemplate;

    private final StringRedisTemplate redisTemplate;

    @Autowired
    public WechatServiceImpl(RestTemplate restTemplate, StringRedisTemplate redisTemplate) {
        this.restTemplate = restTemplate;
        this.redisTemplate = redisTemplate;
    }

    @Value("${wechat.accesstoken.grant_type}")
    private String grant_type;//获取access_token填写client_credential
    @Value("${wechat.appid}")
    private String appid;//第三方用户唯一凭证
    @Value("${wechat.appsecret}")
    private String secret;//第三方用户唯一凭证密钥，即appsecret
    @Value("${wechat.check.token}")
    private String token;//与微信配置信息中的Token要一致


    @Override
    public String getToken(String signature, String timestamp, String nonce, String echostr) {
        // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
        if (signature != null && CheckUtil.checkSignature(token, signature, timestamp, nonce)) {
            return echostr;
        } else {
            return "";
        }
    }

    @Override
    public String getAccessToken() {
        //redis中获取accessToken
        String accessToken = redisTemplate.opsForValue().get(String.format("wechat:%s:accessToken", appid));
        if (accessToken == null) {
            //触发刷新accessToken
            accessToken = refreshToken();
        }
        return accessToken;
    }

    private TokenInfo getToken() {
        Map<String, String> params = new HashMap<>();
        params.put("grant_type", grant_type);
        params.put("appid", appid);
        params.put("secret", secret);
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type={grant_type}&appid={appid}&secret={secret}";
        return restTemplate.getForObject(url, TokenInfo.class, params);
    }

    @Override
    public String refreshToken() {
        TokenInfo tokenInfo = getToken();
        if (tokenInfo.getAccessToken() != null) {
            redisTemplate.opsForValue().set(String.format("wechat:%s:accessToken", appid), tokenInfo.getAccessToken(), tokenInfo.getExpiresIn(), TimeUnit.SECONDS);
            return tokenInfo.getAccessToken();
        } else {
            logger.error("刷新accessToken失败", tokenInfo.getErrcode(), tokenInfo.getErrmsg(), appid);
            return null;
        }
    }

    @Override
    public String ServerIP() {
        String url = "https://api.weixin.qq.com/cgi-bin/getcallbackip?access_token=" + getAccessToken();
        return restTemplate.getForObject(url, String.class);
    }

    @Override
    public String createMenu() {
        String url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=" + getAccessToken();
        Map<String, List<Menu>> data = new HashMap<>();

        //一级菜单
        List<Menu> button = new ArrayList<>();
        Menu menu = new Menu();
        menu.setName("今日歌曲");
        menu.setType("click");
        menu.setKey("V1001_TODAY_MUSIC");
        button.add(menu);
        //一级菜单
        Menu menu2 = new Menu();
        menu2.setName("菜单");
        menu2.setType("click");
        menu2.setKey("V1001_TODAY_MUSIC");
        List<Menu> subMenuList = new ArrayList<>();
        Menu subMenu = new Menu();
        subMenu.setType("view");
        subMenu.setName("搜索");
        subMenu.setUrl("http://www.soso.com/");
        subMenuList.add(subMenu);
        Menu subMenu2 = new Menu();
        subMenu2.setType("miniprogram");
        subMenu2.setName("wxa");
        subMenu2.setUrl("http://mp.weixin.qq.com");
        subMenu2.setAppid(appid);
        subMenu2.setPagepath("pages/lunar/index");
        subMenuList.add(subMenu2);
        Menu subMenu3 = new Menu();
        subMenu3.setType("click");
        subMenu3.setName("赞我们一下");
        subMenu3.setKey("V1001_GOOD");
        subMenuList.add(subMenu3);
        



        data.put("button", button);





        restTemplate.postForObject(url, data, WechatError.class);
        return null;
    }

}
