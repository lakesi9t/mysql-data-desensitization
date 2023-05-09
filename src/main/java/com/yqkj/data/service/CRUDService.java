package com.yqkj.data.service;

import com.github.pagehelper.PageHelper;
import com.yqkj.data.bean.RequestBodyEntity;
import com.yqkj.data.bean.User;
import com.yqkj.data.dao.CRUDDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CRUDService implements CRUDDao {
    @Value("${common.user.pageSize}")
    private Integer pageSize;

    @Resource
    private CRUDDao crudDao;


    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<User> getAllUser() {
        return crudDao.getAllUser();
    }

    @Override
    public List<User> getTableAllUser(String tableName) {
        return crudDao.getTableAllUser(tableName);
    }

    @Override
    public Integer countAllUser() {
        return crudDao.countAllUser();
    }

    @Override
    public User getUserById(Integer id) {
        return crudDao.getUserById(id);
    }

    @Override
    public int addUser(User user) {
        return crudDao.addUser(user);
    }

    @Override
    public int updateUser(User user) {
        return crudDao.updateUser(user);
    }

    @Override
    public int updateTableCustom(String tableName, Integer id, String username, String password) {
        return crudDao.updateTableCustom(tableName, id, username, password);
    }

    @Override
    public int deleteUser(Integer id) {
        return crudDao.deleteUser(id);
    }

    //分页查询
    public List<User> findPage(Integer pageNo, Integer pageSize, String tableName) {

        PageHelper.startPage(pageNo,pageSize);

        return crudDao.getTableAllUser(tableName);//这里的list方法是查询全部数据的方法

    }


    public void handle(String url, String tableName) {
//        String url = "http://localhost:8080/user/test3";
//        JSONObject postData = new JSONObject();
//        Result allUser = crudControllergetAllUser();

//        Integer count = countAllUser();

        int countPage = countAllUser() / pageSize + (countAllUser() % pageSize == 0 ? 0 : 1);

        for (int i = 1; i <= countPage; i++) {
            List<User> page = findPage(i, pageSize, tableName);
            page.forEach(
                    e -> {
                        RequestBodyEntity etl = new RequestBodyEntity(e.getPassword(), "TEL");
                        ResponseEntity<RequestBodyEntity> jsonObjectResponseEntity = restTemplate.postForEntity(url, etl, RequestBodyEntity.class);
                        HttpStatus statusCode = jsonObjectResponseEntity.getStatusCode();

                        if (statusCode.is2xxSuccessful()) {
                            RequestBodyEntity body = jsonObjectResponseEntity.getBody();
                            String content = body.getContent();
                            e.setPassword(content);
                            updateUser(e);
                        } else {
//                while () 失败之后重试两次，再失败，记录到文件里
                            System.out.println("接口请求失败");
                        }
                    }
            );

        }
    }
}
