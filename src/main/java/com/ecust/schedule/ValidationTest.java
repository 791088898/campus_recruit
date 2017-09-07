package com.ecust.schedule;

import com.ecust.service.EquipmentService;
import com.ecust.service.MyCopyService;
import com.ecust.utils.PropertyUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;


@Component
@Lazy(false)
public class ValidationTest {

    private static Logger logger = Logger.getLogger("myLog");


    @Autowired
    EquipmentService equipmentService;

    @Autowired
    MyCopyService myCopyService;

    public static SimpleDateFormat sdf_yyyyMMddHHmmss = new SimpleDateFormat("yyyyMMddHHmmss");

    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

    @Scheduled(cron = "0 0 2 * * ?")
//    @Scheduled(fixedRate = 50000)
    public void validateTest() {

        Date date1;
        Date date2 = new Date();
        List<Map<String, Object>> dataList = equipmentService.queryAllEquipmentForValidation();
        for (int i = 0; i < dataList.size(); i++) {
            HashMap oneCompany = (HashMap) dataList.get(i);
            String dateString = (String) oneCompany.get("deadline");
            try {
                date1 = sdf.parse(dateString);
                int days = (int) ((date1.getTime() - date2.getTime()) / (1000 * 3600 * 24));
                if (days < 0) { // �Ѿ�����
                    equipmentService.updateStatus(((Long)oneCompany.get("id")).intValue(),2);
                }else if(days < 3 ){ // ��������
                    equipmentService.updateStatus(((Long)oneCompany.get("id")).intValue(),1);
                }else { // ���ڽ���
                    equipmentService.updateStatus(((Long)oneCompany.get("id")).intValue(),0);
                }
            } catch (Exception e) {
                // ״̬δ֪
                equipmentService.updateStatus(((Long)oneCompany.get("id")).intValue(),4);
            }
        }

        myCopyService.clear();

        logger.info("validation test and clear my_copy ");

//        System.out.println(sdf_yyyyMMddHHmmss.format(new Date()) + " ��ִ���С�����");
    }

//    @Scheduled(fixedRate = 10000)
    public void handle() {

        // ��¼debug�������Ϣ
        logger.debug("This is debug message.");
        // ��¼info�������Ϣ
        logger.info("This is info message.");
        // ��¼error�������Ϣ
        logger.error("This is error message.");
        PropertyUtils.loadProperity("/wechat.properties");

        System.out.println("*****************************************");
    }
}