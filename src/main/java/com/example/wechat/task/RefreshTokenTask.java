package com.example.wechat.task;

import com.example.wechat.service.WechatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 刷新token的定时任务
 *
 * @author liyuan
 * @create 2017-09-21 15:01
 **/

@Component
public class RefreshTokenTask {

    private final WechatService wechatService;

    @Autowired
    public RefreshTokenTask(WechatService wechatService) {
        this.wechatService = wechatService;
    }

    // （两小时-两分钟）执行一次
    @Scheduled(fixedRate = 7200000-120000)
    public void refresh() {
        wechatService.refreshToken();
    }

}
