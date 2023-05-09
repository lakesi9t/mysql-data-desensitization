package com.yqkj.data.run;

import com.yqkj.data.constant.TableType;
import com.yqkj.data.service.CRUDService;
import com.yqkj.data.service.CustomService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;



@Component
@Order(1)
@Slf4j
public class RunProject implements CommandLineRunner {
    @Value("${common.desensitization.url}")
    private String url;

    @Autowired
    private CRUDService crudService;

    @Autowired
    private CustomService customService;

    @Override
    public void run(String... args) throws Exception {
        long t1 = System.currentTimeMillis();
        if(args.length != 2) {
            log.info("请输入需要脱敏的表类型及表名");
            System.exit(1);
        }
        String tableType = args[0];
        String tableName = args[1];

//        String url = "http://localhost:8080/user/test3";
        if (TableType.CUSTOM.equalsIgnoreCase(tableType))
        {
            customService.handle(url, tableName);
            log.info("{}表处理流程！", tableName);
        }
        if (TableType.USER.equalsIgnoreCase(tableType))
        {
            crudService.handle(url, tableName);
            log.info("{}表处理流程！", tableName);
        }


        long t2 = System.currentTimeMillis();
        log.info("执行完成。耗时:{}秒,args:{}",(t2-t1)/1000,args);
        System.exit(1);

    }
}
