package com.wechat.quartz;

import com.wechat.common.WeChatTask;
import org.apache.log4j.Logger;


public class QuartzJob{
    private static Logger logger = Logger.getLogger(QuartzJob.class);
    /**
     * @Description: ����ִ�л�ȡ token
     * @param
     * @author dapengniao
     * @date 2016 �� 3 �� 10 �� ���� 4:34:26
     */
    public void workForToken() {
        try {
            WeChatTask timer = new WeChatTask();
            timer.getToken_getTicket();
        } catch (Exception e) {
            logger.error(e, e);
        }
    }

}
