package com.ecust.controller;

import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ecust.pojo.User;
import com.wechat.utils.MessageUtil;
import com.wechat.utils.SignUtil;
import com.wechat.dispatcher.EventDispatcher;
import com.wechat.dispatcher.MsgDispatcher;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/wechat")
public class WeChatController {



    private static Logger logger = Logger.getLogger(WeChatController.class);

    /**
     *
     * @Description: ���ڽ��� get ������������֤����
     * @param @param request
     * @param @param response
     * @param @param signature
     * @param @param timestamp
     * @param @param nonce
     * @param @param echostr
     * @author dapengniao
     * @date 2016 �� 3 �� 4 �� ���� 6:20:00
     */
    @RequestMapping(value = "security", method = RequestMethod.GET)
    public void doGet(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(value = "signature", required = true) String signature,
            @RequestParam(value = "timestamp", required = true) String timestamp,
            @RequestParam(value = "nonce", required = true) String nonce,
            @RequestParam(value = "echostr", required = true) String echostr) {
        try {
            if (SignUtil.checkSignature(signature, timestamp, nonce)) {
                PrintWriter out = response.getWriter();
                out.print(echostr);
                out.close();
            } else {
                logger.info("������ڷǷ�����");
            }
        } catch (Exception e) {
            logger.error(e, e);
        }
    }

    @RequestMapping(value = "security", method = RequestMethod.POST)
    // post �������ڽ���΢�ŷ������Ϣ
    public void DoPost(HttpServletRequest request,HttpServletResponse response) {
        System.out.println("���� post ������");

        try{
            Map<String, String> map=MessageUtil.parseXml(request);
            String msgtype=map.get("MsgType");
            if(MessageUtil.REQ_MESSAGE_TYPE_EVENT.equals(msgtype)){
                EventDispatcher.processEvent(map); //�����¼�����
            }else{
                response.setCharacterEncoding("utf-8");
                String respXML = MsgDispatcher.processMessage(map); //
                PrintWriter out = response.getWriter();
                out.print(respXML);
                out.flush();
                out.close();
            }
        }catch(Exception e){
            logger.error(e,e);
        }
    }
}