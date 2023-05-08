package com.yqkj.data.service;

import com.github.pagehelper.PageHelper;
import com.yqkj.data.bean.Custom;
import com.yqkj.data.bean.RequestBodyEntity;
import com.yqkj.data.bean.User;
import com.yqkj.data.config.StaticConfig;
import com.yqkj.data.dao.CRUDDao;
import com.yqkj.data.dao.CustomDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CustomService implements CustomDao {
    @Resource
    private CustomDao customDao;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<Custom> getAllCustom() {
        return customDao.getAllCustom();
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
    public int deleteCustom(Integer id) {
        return customDao.deleteCustom(id);
    }

    //分页查询
    public List<Custom> findPage(Integer pageNo, Integer pageSize) {

        PageHelper.startPage(pageNo,pageSize);

        return customDao.getAllCustom();//这里的list方法是查询全部数据的方法

    }

    public void handle(String url) {
//        String url = "http://localhost:8080/user/test3";
//        JSONObject postData = new JSONObject();
//        Result allUser = crudControllergetAllUser();

//        Integer count = countAllUser();

        int countPage = countAllCustom() / StaticConfig.pageSize + 1;

        for (int i = 1; i <= countPage; i++) {
            List<Custom> page = findPage(i, StaticConfig.pageSize);
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
                            updateCustom(e);
                        } else {
//                while () 失败之后重试两次，再失败，记录到文件里
                            System.out.println("接口请求失败");
                        }


                    }
            );

        }
    }
}
