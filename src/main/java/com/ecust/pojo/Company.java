package com.ecust.pojo;


import java.io.Serializable;

/**
 * Created by cheng on 2017/8/17.
 */
public class Company implements Serializable {
    //��˾����ID
    private String id;
    private String name;
    // ��Ƹְλ
    private String position;

    //Ͷ������
    private String link;

    //�豸״̬��0����1��ά��2����ά��

    private String deadline;
    private Integer push;

    private String push_code;
    private Integer status;

    private String createdUser;

    public String getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(String createdUser) {
        this.createdUser = createdUser;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public Integer getPush() {
        return push;
    }

    public void setPush(Integer push) {
        this.push = push;
    }

    public String getPush_code() {
        return push_code;
    }

    public void setPush_code(String push_code) {
        this.push_code = push_code;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
