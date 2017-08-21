package com.ecust.utils;

import com.ecust.dto.CompanyForm;
import com.ecust.pojo.Company;
import com.ecust.pojo.RUser;
import com.ecust.pojo.User;

/**
 * Created by cheng on 2017/8/18.
 */
public class DataTrans {
    public static Company toCompany(CompanyForm companyForm) {
        Company company = new Company();
        company.setName(companyForm.getName());
        company.setPosition(companyForm.getPosition());
        company.setLink((companyForm.getLink()=="" || companyForm.getLink()== null )?"δ֪":companyForm.getLink());
        company.setDeadline((companyForm.getDeadline() == ""||  companyForm.getDeadline() == null)? "δ֪" : companyForm.getDeadline());
        if (companyForm.getPush_code() == "" || companyForm.getPush_code() == null) {
            company.setPush(0);  // 1 push; 0 not push
            company.setPush_code("");
        }else {
            company.setPush(1);
            company.setPush_code(companyForm.getPush_code());
        }
        company.setStatus(0); // 0 :δͶ�� 1: ��Ͷ��
        return company;
    }

    public static User toUser(RUser rUser) {
        User user = new User();
        user.setUserName(rUser.getUserName());
        user.setPassword(rUser.getPassword());
        user.setRoleName("�û�");
        return user;
    }

    public static void main(String[] args) {
        System.out.println("\" nihao \"");
    }
}
