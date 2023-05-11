package com.yqkj.data.service;

import com.github.pagehelper.PageHelper;
import com.yqkj.data.bean.Custom;
import com.yqkj.data.bean.RequestBodyEntity;
import com.yqkj.data.dao.CustomDao;
import com.yqkj.data.utils.ReadWriteUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class CustomService implements CustomDao {
    @Value("${common.custom.pageSize}")
    private Integer pageSize;

    @Resource
    private CustomDao customDao;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<Custom> getAllCustom() {
        return customDao.getAllCustom();
    }

    @Override
    public List<Custom> getTableAllCustom(String tableName) {
        return customDao.getTableAllCustom(tableName);
    }

    @Override
    public Integer countAllCustom(String tableName) {
        return customDao.countAllCustom(tableName);
    }

    @Override
    public Custom getCustomById(Integer id) {
        return customDao.getCustomById(id);
    }

    @Override
    public int addCustom(Custom custom) {
        return customDao.addCustom(custom);
    }

    @Override
    public int updateCustom(Custom custom) {
        return customDao.updateCustom(custom);
    }

    @Override
    public int updateTableCustom(String tableName, Integer id, String username, String tel, String idcard) {
        return customDao.updateTableCustom(tableName, id, username, tel, idcard);
    }

    @Override
    public int deleteCustom(Integer id) {
        return customDao.deleteCustom(id);
    }

    //分页查询
    public List<Custom> findPage(Integer pageNo, Integer pageSize, String tableName) {

        PageHelper.startPage(pageNo,pageSize);

        return customDao.getTableAllCustom(tableName);//这里的list方法是查询全部数据的方法

    }

    public void handle(String url, String tableName) {

        File file = new File("./logs");
        if (!file.exists()) {
            file.mkdir();
        }
        File file1 = new File(file, tableName +"_error_data.txt");
        try {
            if (!file1.exists()){
                file1.createNewFile();
            }
        } catch (IOException e) {
            log.info("error data文件创建失败");
        }

        int countPage = countAllCustom(tableName) / pageSize + (countAllCustom(tableName) % pageSize == 0 ? 0 :1);


        for (int i = 1; i <= countPage; i++) {
            List<Custom> page = findPage(i, pageSize, tableName);
            page.forEach(
                    e -> {
                        RequestBodyEntity etl = new RequestBodyEntity(e.getTel(), "TEL");
                        RequestBodyEntity card = new RequestBodyEntity(e.getIdcard(), "CARD");
                        ResponseEntity<RequestBodyEntity> jsonObjectResponseEntity = restTemplate.postForEntity(url, etl, RequestBodyEntity.class);
                        ResponseEntity<RequestBodyEntity> cardResponseEntity = restTemplate.postForEntity(url, card, RequestBodyEntity.class);
                        HttpStatus statusCode = jsonObjectResponseEntity.getStatusCode();
                        HttpStatus cardStatusCode = cardResponseEntity.getStatusCode();

                        if (statusCode.is2xxSuccessful() && cardStatusCode.is2xxSuccessful()) {
                            RequestBodyEntity body = jsonObjectResponseEntity.getBody();
                            RequestBodyEntity body2 = cardResponseEntity.getBody();
                            String content = body.getContent();
                            String content2 = body2.getContent();
//                            e.setTel(content);
//                            e.setIdcard(content2);
                            try {
                                updateTableCustom(tableName, e.getId(), e.getUsername(), content, content2);
                            } catch (Exception exception) {
                                log.info("更新失败" + exception.getMessage());
                                ReadWriteUtil.writeString(file1, e.toString()+"\n");
                            }
                        } else {
//                while () 失败之后重试两次，再失败，记录到文件里
                            log.info("此行接口数据调用失败, TEL:{}, TEL接口返回{}, IDCARD:{}, IDCARD接口返回:{}", e.getTel(), statusCode, e.getIdcard(), cardStatusCode);
                            ReadWriteUtil.writeString(file1, e.toString()+"\n");
                        }

                    }
            );

        }
    }
}
