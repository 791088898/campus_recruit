package com.wechat.wechatinput;

import com.wechat.wechatinput.InputData;

import java.util.*;

/**
 * Created by cheng on 2017/8/21.
 * <p>
 * ��Ϊ΢�ź�̨û��  session  �����Լ�дһ��ģ�� session ,
 */
public class SessionData {

    private static Map<String, InputData> session = new HashMap<>();


    private SessionData() {

    }

    public static Map<String, InputData> getSessionData() {
        return session;
    }
}
