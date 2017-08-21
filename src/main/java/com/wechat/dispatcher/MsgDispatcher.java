package com.wechat.dispatcher;

import com.ecust.dto.CompanyForm;
import com.ecust.pojo.Company;
import com.ecust.service.EquipmentService;
import com.ecust.service.impl.EquipmentServiceImpl;
import com.ecust.utils.DataTrans;
import com.wechat.wechatinput.InputData;
import com.wechat.wechatinput.SessionData;
import com.wechat.utils.MessageUtil;
import com.wechat.message.resp.Article;
import com.wechat.message.resp.NewsMessage;
import com.wechat.message.resp.TextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.util.*;

/**
 * ClassName: MsgDispatcher
 *
 * @author dapengniao
 * @Description: ��Ϣҵ����ַ���
 * @date 2016 �� 3 �� 7 �� ���� 4:04:21
 */
@Component
public class MsgDispatcher {

    static EquipmentService equipmentService;

    @Autowired
    public void setSomeThing(EquipmentService equipmentService){
        MsgDispatcher.equipmentService = equipmentService;
    }

    private static Timer timer = new Timer();

    public static String processMessage(Map<String, String> map) {
        if (map.get("MsgType").equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) { // �ı���Ϣ
            final String openid = map.get("FromUserName"); //�û� openid
            String mpid = map.get("ToUserName");   //���ں�ԭʼ ID

            final Map<String, InputData> session = SessionData.getSessionData();

            TextMessage txtmsg = new TextMessage();
            txtmsg.setToUserName(openid);
            txtmsg.setFromUserName(mpid);
            txtmsg.setCreateTime(new Date().getTime());
            txtmsg.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
            System.out.println(map.get("Content").trim());
            System.out.println(map.get("Content"));
            if (map.get("MsgType").equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) { // �ı���Ϣ

                InputData inputData = session.get(openid);
                if (map.get("Content").trim().equals("���") || inputData != null) { // ����߼�
                    if (inputData == null) {

                        // ��ʱ�����û�����ʱ�� 30 �룬����Ϊ�����˲���
                        TimerTask task = new TimerTask() {
                            public void run() {
                                session.remove(openid);
                            }
                        };
                        timer.schedule(task, 1000 * 30);
                        inputData = new InputData();
                        inputData.setVisitTime(1);
                        session.put(openid, inputData);
                        txtmsg.setContent("�����빫˾����:");
                    } else if (inputData.getVisitTime() == 1) {
                        inputData.setVisitTime(2);
                        txtmsg.setContent("��������Ƹ��λ:");
                        inputData.getCompanyForm().setName(map.get("Content"));
                    } else if (inputData.getVisitTime() == 2) {
                        inputData.setVisitTime(3);
                        txtmsg.setContent("������Ͷ������:");
                        inputData.getCompanyForm().setPosition(map.get("Content"));
                    } else if (inputData.getVisitTime() == 3) {
                        inputData.setVisitTime(1);
                        inputData.getCompanyForm().setLink(map.get("Content"));
                        txtmsg.setContent("���Ѿ�������");
                        session.remove(openid);

                        new Thread() {
                            @Override
                            public void run(){
                                Company company = DataTrans.toCompany(session.get(openid).getCompanyForm());
                                Boolean bool = equipmentService.createEquipment(company);
                            }
                        }.start();
                    } else {
                        session.remove(openid);
                        txtmsg.setContent("�����Ƹ��ظ�\"���\"");
                    }
                } else {
                    txtmsg.setContent("�����Ƹ��ظ�\"���\""); // ������߼�
                }
                return MessageUtil.textMessageToXml(txtmsg);
            }
        }

        if (map.get("MsgType").equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) { // ͼƬ��Ϣ
            String openid = map.get("FromUserName"); //�û� openid
            String mpid = map.get("ToUserName");   //���ں�ԭʼ ID
            //��ͼ����Ϣ
            NewsMessage newmsg = new NewsMessage();
            newmsg.setToUserName(openid);
            newmsg.setFromUserName(mpid);
            newmsg.setCreateTime(new Date().getTime());
            newmsg.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);

            if (map.get("MsgType").equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) { // ͼƬ��Ϣ
                System.out.println("==============����ͼƬ��Ϣ��");
                Article article = new Article();
                article.setDescription("����ͼ����Ϣ 1"); //ͼ����Ϣ������
                article.setPicUrl("http://res.cuiyongzhi.com/2016/03/201603086749_6850.png"); //ͼ����ϢͼƬ��ַ
                article.setTitle("ͼ����Ϣ 1");  //ͼ����Ϣ����
                article.setUrl("http://www.cuiyongzhi.com");  //ͼ�� url ����
                List<Article> list = new ArrayList<Article>();
                list.add(article);     //���﷢�͵��ǵ�ͼ�ģ������Ҫ���Ͷ�ͼ���������� list �м����� Article ���ɣ�
                newmsg.setArticleCount(list.size());
                newmsg.setArticles(list);
                return MessageUtil.newsMessageToXml(newmsg);
            }
        }

        if (map.get("MsgType").equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)) { // ������Ϣ
            System.out.println("==============����������Ϣ��");
        }

        if (map.get("MsgType").equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) { // λ����Ϣ
            System.out.println("==============����λ����Ϣ��");
        }

        if (map.get("MsgType").equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)) { // ������Ϣ
            System.out.println("==============����������Ϣ��");
        }

        return null;
    }
}
