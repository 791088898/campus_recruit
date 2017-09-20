package com.ecust.pojo;

import java.io.Serializable;

/**
 * Created by cheng on 2017/9/16.
 */
public class Result implements Serializable {
    private String message;

    private String code; // 1 �ɹ� 2 �ظ����� 3 ����ʧЧ 4 δ֪����

    public Result() {
    }

    public Result(String code ,String message) {
        this.code = code;
        this.message = message;
    }

    public Result(String message) {
        this.message = message;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
