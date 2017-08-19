package com.ecust.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

/**
 * Created by cheng on 2017/8/18.
 */
@Aspect
public class CompanyAspect {
    @Pointcut("execution(* com.ecust.dao.EquipmentDao.*(..))")
    public void actionLog(){}
//    @Before("actionLog()")
//    public void before() {
//        this.printLog(" @Before  actionLog() ׼����ӡaction����־...  ");
//    }
//
//    @Around("actionLog()")
//    public void around(ProceedingJoinPoint pjp) throws Throwable{
//        this.printLog(" @Around  actionLog() ׼����ӡaction����־... ");
//        pjp.proceed();
//        this.printLog(" @Around  actionLog() action���߼��Ѿ�ִ�����  ");
//    }
    @After("actionLog()")
    public void after() {
        this.printLog(" @After  actionLog() action���߼��Ѿ�ִ�����  ");
    }
    private void printLog(String str){
        System.out.println(str);
    }

}
