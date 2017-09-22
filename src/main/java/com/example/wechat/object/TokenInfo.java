package com.example.wechat.object;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by liyuan on 2017/9/21
 */
public class TokenInfo extends WechatError {
    //用于属性上，作用是把该属性的名称序列化为另外一个名称，如把accessToken属性序列化为access_token
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("expires_in")
    private long expiresIn;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }
}
