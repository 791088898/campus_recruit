package com.ecust.schedule;

import org.springframework.scheduling.annotation.Scheduled;

/**
 * Created by cheng on 2017/8/18.
 */
public class Test {
    //д���Լ����߼�����
    @Scheduled(fixedRate = 10 * 1000)
    public void handle() {
        //д���Լ����߼�����
        System.out.println("test");
    }
}