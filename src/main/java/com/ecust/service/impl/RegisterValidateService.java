package com.ecust.service.impl;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import com.ecust.dao.UserDao;
import com.ecust.pojo.Result;
import com.ecust.pojo.User;
import com.ecust.service.MailService;
import com.ecust.service.UserService;
import com.ecust.utils.EncodingTool;
import com.ecust.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegisterValidateService {

    @Autowired
    private MailService mailService;

    @Autowired
    private UserService userService;


    /**
     * �����û��һ������߼�,
     */

    public synchronized Result findPswBack(User userIn, String url) {
        Result result = new Result();
        User currentUser = userService.queryUserByName(userIn.getUserName());
        if (currentUser == null) {
            result.setMessage("���û��������أ�����ȥע��");
            result.setCode("signup");   // Controller ���ж� Ϊ 4 �Ժ�� �������� ע��ҳ��
            return result;
        }

        currentUser.setNewpassword(userIn.getNewpassword());
        currentUser.setActiveCode(MD5Util.encode2hex(currentUser.getUserName()));
        userService.updateUserForReNewPwd(currentUser);
        sendEmailToUserForPassword(currentUser, url);
        result.setMessage("�������������䷢��һ��ȷ���ʼ���ȷ�Ϻ���Ч");
        result.setCode("login");

        return result;
    }

    /**
     * �����û���¼�߼�,��֪����ʲô�����ﷵ�ص������������룬����ֱ��д��controller �������������
     */
    public synchronized Result login(User userIn, String url) {

        Result result = new Result();
        User currentUser = userService.queryUserByName(userIn.getUserName());

        // ����û��Ѿ�ע����ˣ������û�ȥ��¼��
        if (currentUser != null) {
            if (currentUser.getStatus() != null && currentUser.getStatus() == 0) {
                result.setMessage("��ע����ˣ���ע����ռ����ʼ������һ���ҳ");
                result.setCode("login");

            } else {
                if (currentUser.getStatus() == 0) {
                    result.setMessage("����û��֤����Ŷ����¼����ȥ��֤��");
                    result.setCode("login");
                } else {
                    result.setMessage("���û��Ѿ����ڣ�������ȥֱ��ȥ��¼�����һ���ҳ");
                    result.setCode("login");
                }

            }

            return result;
        }

        // �û���δע�������������ע���߼�

        int userNums = userService.countUserNum();
        if (userNums < 50) {
            // ���û���Ϣд�����ݿ� , ����ʾ�û�������֤
            boolean bool = userService.createUser(userIn);
            if (bool) {
                // ע��ɹ������û�����������֤
                sendEmailToUserForValidation(userIn, url);
                result.setMessage("ע��ɹ�����ȥ����鿴�����ʼ��ɣ����һ���ҳ");
                result.setCode("login");
                return result;
            } else {
                result.setMessage("ע��ʧ�ܣ���������ע��һ�ΰɣ���������ע��");
                result.setCode("signup");
                return result;
            }
        } else {
            result.setMessage("����վע����Ա�ˣ��� guest �˺ŵ�¼��<br> �û���guest<br> ���룺guest<br>���һ���ҳ");
            result.setCode("login");
            return result;
        }
    }

    /**
     * ����ע��
     */
    public void sendEmailToUserForValidation(User user, String url) {
        ///�ʼ�������
        StringBuffer sb = new StringBuffer("����������Ӽ����˺ţ�24Сʱ��Ч ��������ע���˺�" +
                "������ֻ��ʹ��һ�Σ��뾡��ȷ�ϣ����κ��������ֱ�ӻظ����ʼ���ϵ�ң�" +
                "<br>Keep In Touch My dude<br>");
        sb.append("<a href=\"" + url + "/user/validateUser/1?email=");
        sb.append(user.getUserName());
        sb.append("&activeCode=");
        sb.append(user.getActiveCode());
        sb.append("\">" + url + "/user/validateUser/1?email=");
        sb.append(user.getUserName());
        sb.append("&activeCode=");
        sb.append(user.getActiveCode());
        sb.append("</a><br>");
        sb.append("�����û���: " + user.getUserName());
        sb.append("<br>");
        sb.append("����  ����: " + user.getPassword());
        sb.append("<br>");

        //�����ʼ�
        mailService.sendMail(user, sb.toString(), "Validate Your Account");

        System.out.println("�����ʼ�");

    }

    /**
     * �����һ�����
     */
    public void sendEmailToUserForPassword(User user, String url) {
        ///�ʼ�������
        StringBuffer sb = new StringBuffer("�����������ȷ�����������룬24Сʱ��Ч��" +
                "������ֻ��ʹ��һ�Σ��뾡�켤����κ��������ֱ�ӻظ����ʼ���ϵ�ң�" +
                "<br>Keep In Touch My dude<br>");
        sb.append("<a href=\"" + url + "/user/validateUser/2?email=");
        sb.append(user.getUserName());
        sb.append("&activeCode=");
        sb.append(user.getActiveCode());
        sb.append("\">" + url + "/user/validateUser/1?email=");
        sb.append(user.getUserName());
        sb.append("&activeCode=");
        sb.append(user.getActiveCode());
        sb.append("</a><br><br>");
        sb.append("����������Ϊ: <font color = \"red\">" + user.getNewpassword() + "</font>");
        sb.append("</br>");

        //�����ʼ�
        mailService.sendMail(user, sb.toString(), "Re_New Password");

        System.out.println("�����ʼ�");

    }

    /**
     * �����һ�����
     *
     * @throws ParseException
     */
    //���ݼ������email����
    public synchronized Result processFindPassWordBack(String email, String validateCode) throws ParseException {
        Result result = null;
        //���ݷ��ʲ㣬ͨ��email��ȡ�û���Ϣ
        User user = (User) userService.queryUserByName(email);

        if (user == null) {
            result = new Result("signup", "�û������ڣ������Ѿ����Ƴ���ȥע���");
            return result;
        }

        if (user != null && user.getStatus() == 0) {
            result = new Result("login", "�����û�����δ�㼤���ȥ���伤���");
            return result;
        }
        //��֤�û��Ƿ���ڣ�������ӽ�������ģ��û�һ�㶼����
        if (!validateCode.equals(user.getActiveCode())) {
            result = new Result("password", "�����ʼ��Ѿ�ʧЧ�����������һ������");
            return result;
        }
        //��֤�û�����״̬
        if (user.getNewpassword() != null) {
            ///û����
            Date currentTime = new Date();//��ȡ��ǰʱ��
            //��֤�����Ƿ����
            Date dNow = new Date();   //��ǰʱ��
            Date dBefore = new Date();
            Calendar calendar = Calendar.getInstance();  //�õ�����
            calendar.setTime(dNow);//�ѵ�ǰʱ�丳������
            calendar.add(Calendar.DAY_OF_MONTH, -1);  //����Ϊǰһ��
            dBefore = calendar.getTime();   //�õ�ǰһ���ʱ��
            if (dBefore.before(user.getRepwdTime())) {
                //��֤�������Ƿ���ȷ
                //����ɹ��� //�������û��ļ���״̬��Ϊ�Ѽ���
                user.setPassword(user.getNewpassword());
                user.setActiveCode(MD5Util.encode2hex(user.getUserName()));
                result = new Result("login", "��������ɹ�������ȥ��¼��");
                userService.updateUser(user);
                return result;
            }else {
                result = new Result("login", "��֤�����Ѿ�����");
                userService.updateUser(user);
                return result;
            }
        } else {
            // 1 �ɹ� 2 �ظ����� 3 ����ʧЧ 4 δ֪����
            // ��֤�ʼ��Ѿ���ʱ���ܾ���֤
            // ��ʱ��������û�δ��֤����ɾ�����û�
            result = new Result("password", "��������Ч�����������һ������");
            return result;
        }
    }


    /**
     * ������
     *
     * @throws ParseException
     */
    //���ݼ������email����
    public synchronized Result processActivate(String email, String validateCode) throws ParseException {
        Result result = null;
        //���ݷ��ʲ㣬ͨ��email��ȡ�û���Ϣ
        User user = (User) userService.queryUserByName(email);
        //��֤�û��Ƿ���ڣ�������ӽ�������ģ��û�һ�㶼����
        if (user != null) {
            //��֤�û�����״̬
            if (validateCode.equals(user.getActiveCode())) {
                ///û����
                Date currentTime = new Date();//��ȡ��ǰʱ��
                //��֤�����Ƿ����
                Date dNow = new Date();   //��ǰʱ��
                Date dBefore = new Date();
                Calendar calendar = Calendar.getInstance();  //�õ�����
                calendar.setTime(dNow);//�ѵ�ǰʱ�丳������
                calendar.add(Calendar.DAY_OF_MONTH, -1);  //����Ϊǰһ��
                dBefore = calendar.getTime();   //�õ�ǰһ���ʱ��

                if (dBefore.before(user.getCreateTime())) {
                    //��֤�������Ƿ���ȷ
                    if (user.getStatus() == 0) {
                        //����ɹ��� //�������û��ļ���״̬��Ϊ�Ѽ���
                        System.out.println("==sq===" + user.getStatus());
                        user.setStatus(1);//��״̬��Ϊ����
                        user.setActiveCode(MD5Util.encode2hex(user.getUserName()));
                        userService.updateUser(user); // ֻ���������� ����ʱ��
                        result = new Result("login", "����ɹ�������ȥ��¼��");
                        return result;
                    } else {
                        result = new Result("login", "���Ѿ��������ȥ��¼");
                        return result;
                    }
                } else {
                    // ��֤�ʼ��Ѿ���ʱ���ܾ���֤
                    // ��ʱ��������û�δ��֤����ɾ�����û�
                    if (user.getStatus() == 0) {
                        userService.deleteUser(String.valueOf(user.getId()));
                    }
                    result = new Result("signup", "�����ʼ��Ѿ�ʧЧ����������ע��");
                    return result;
                }
            } else {
                result = new Result("login", "�����Ѿ�ʧЧ������ȥ��¼��");
                return result;
            }
        } else {
            result = new Result("signup", "���û��ѱ��Ƴ�������ע���");
            return result;
        }
    }
}
