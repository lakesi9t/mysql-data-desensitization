package com.yqkj.data.run;

import com.yqkj.data.constant.CollectionName;
import com.yqkj.data.service.CRUDService;
import com.yqkj.data.service.CustomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;



@Component
@Order(1)
@Slf4j
public class RunProject implements CommandLineRunner {

    @Autowired
    private CRUDService crudService;

    @Autowired
    private CustomService customService;

    @Override
    public void run(String... args) throws Exception {
        long t1 = System.currentTimeMillis();
        if(args.length == 0){
            log.info("kafka消费程序启动");
            System.exit(1);
        }
        String tableName = args[0];

        String url = "http://localhost:8080/user/test3";
        if (CollectionName.CUSTOM.equalsIgnoreCase(tableName))
        {
            customService.handle(url);
            log.info("{}表处理流程！", tableName);
        }
        if (CollectionName.USER.equalsIgnoreCase(tableName))
        {
            crudService.handle(url);
            log.info("{}表处理流程！", tableName);
        }


        long t2 = System.currentTimeMillis();
        log.info("执行完成。耗时:{}秒,args:{}",(t2-t1)/1000,args);
        System.exit(1);

    }
}
