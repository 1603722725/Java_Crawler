package com.webMagic.Task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Author: wl
 * @Date: 2019/10/12 7:58
 * 定时爬取  crow 表达式
 */
@Component
public class Test {
    @Scheduled(cron = "0/5 * * * * *")
    public void test(){
        System.out.println("定时任务开启");
    }
}
