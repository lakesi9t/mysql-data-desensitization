package com.yqkj.data.task;


import com.yqkj.data.bean.Custom;
import com.yqkj.data.bean.RequestBodyEntity;
import com.yqkj.data.dao.CustomDao;
import com.yqkj.data.service.CustomService;
import com.yqkj.data.utils.ReadWriteUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class CustomTask {

    @Autowired
    CustomService customService;

    @Value("${common.custom.pageSize}")
    private Integer pageSize;

    @Autowired
    private RestTemplate restTemplate;

    public void handle_copy(String tableName) {

//        CustomService bean = SpringBeanUtil.getBean(CustomService.class);

//        for (int i = 1; i <= 20; i++) {
//            customService.doSomething(i + "");
//        }
        File file = new File("./logs");
        if (!file.exists()) {
            file.mkdir();
        }
        File file1 = new File(file, tableName + "_error_data.txt");
        try {
            if (!file1.exists()) {
                file1.createNewFile();
            }
        } catch (IOException e) {
            log.info("error data文件创建失败");
        }

        int countPage = customService.countAllCustom(tableName) / pageSize + (customService.countAllCustom(tableName) % pageSize == 0 ? 0 : 1);


        for (int i = 1; i <= countPage; i++) {
            List<Custom> page = customService.findPage(i, pageSize, tableName);
            page.forEach(
                    e -> {
                        customService.desensitizationData(e, tableName, file1);
                    }
            );

        }
    }
}
