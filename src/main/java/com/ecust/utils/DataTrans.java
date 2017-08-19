package com.ecust.utils;

import com.ecust.dto.CompanyForm;
import com.ecust.pojo.Company;

/**
 * Created by cheng on 2017/8/18.
 */
public class DataTrans {
    public static Company toCompany(CompanyForm companyForm) {
        Company company = new Company();
        company.setName(companyForm.getName());
        company.setPosition(companyForm.getPosition());
        company.setLink(companyForm.getLink()==null?"δ֪":companyForm.getLink());
        company.setDeadline(companyForm.getDeadline() == null ? "δ֪" : companyForm.getDeadline());
        if (companyForm.getPush_code() == null) {
            company.setPush(1);  // 1 not push; 0 push
        }else {
            company.setPush(0);
            company.setPush_code(companyForm.getPush_code());
        }
        company.setStatus(0); // 0 :δͶ�� 1: ��Ͷ��
        return company;
    }

    public static void main(String[] args) {
        System.out.println("\" nihao \"");
    }
}
