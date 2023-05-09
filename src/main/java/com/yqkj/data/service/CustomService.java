package com.yqkj.data.service;

import com.github.pagehelper.PageHelper;
import com.yqkj.data.bean.Custom;
import com.yqkj.data.bean.RequestBodyEntity;
import com.yqkj.data.dao.CustomDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.List;

@Service
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
    public Integer countAllCustom() {
        return customDao.countAllCustom();
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
//        String url = "http://localhost:8080/user/test3";
//        JSONObject postData = new JSONObject();
//        Result allUser = crudControllergetAllUser();

//        Integer count = countAllUser();
        int countPage = countAllCustom() / pageSize + (countAllCustom() % pageSize == 0 ? 0 :1);

//        int countPage = countAllCustom() / StaticConfig.pageSize + 1;

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
                            e.setTel(content);
                            e.setIdcard(content2);
                            updateTableCustom(tableName, e.getId(), e.getUsername(), e.getTel(), e.getIdcard());
                        } else {
//                while () 失败之后重试两次，再失败，记录到文件里
                            System.out.println("接口请求失败");
                        }


                    }
            );

        }
    }
}
