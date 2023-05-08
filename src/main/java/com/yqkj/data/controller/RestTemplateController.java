package com.yqkj.data.controller;


import com.alibaba.fastjson.JSONObject;
import com.yqkj.data.bean.RequestBodyEntity;
import com.yqkj.data.bean.User;
import com.yqkj.data.service.CRUDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
public class RestTemplateController {
    @Autowired
    private CRUDController crudController;

    @Autowired
    private CRUDService crudService;

    @Autowired
    private RestTemplate restTemplate;

    /***********HTTP GET method*************/
    @GetMapping("/testGetApi")
    public String getJson() {
        String url = "http://localhost:8080/user/test1";
        //String json =restTemplate.getForObject(url,Object.class);
        ResponseEntity<String> results = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        String json = results.getBody();
        return json;
    }

    /**********HTTP POST method**************/
    @PostMapping(value = "/testPost")
    public Object postJson(@RequestBody JSONObject param) {
        System.out.println(param.toJSONString());
        param.put("action", "post");
        param.put("username", "tester");
        param.put("pwd", "123456748");
        return param;
    }

    @PostMapping(value = "/testPostApi")
    public Object testPost() {
        String url = "http://localhost:8081/testPost";
        JSONObject postData = new JSONObject();
        postData.put("descp", "request for post");
        JSONObject json = restTemplate.postForEntity(url, postData, JSONObject.class).getBody();
        return json;
    }



    @PostMapping(value = "/testPostDesensitization")
    public Object testPost1() {
        String url = "http://localhost:8080/user/test3";
//        JSONObject postData = new JSONObject();
//        Result allUser = crudController.getAllUser();
        List<User> allUser1 = crudService.getAllUser();
        allUser1.forEach(e -> {
            RequestBodyEntity etl = new RequestBodyEntity(e.getPassword(), "TEL");
            ResponseEntity<RequestBodyEntity> jsonObjectResponseEntity = restTemplate.postForEntity(url, etl, RequestBodyEntity.class);
            HttpStatus statusCode = jsonObjectResponseEntity.getStatusCode();

            if (statusCode.is2xxSuccessful()) {
                RequestBodyEntity body = jsonObjectResponseEntity.getBody();
                String content = body.getContent();
                e.setPassword(content);
                crudService.updateUser(e);
            } else {
//                while () 失败之后重试两次，再失败，记录到文件里
                System.out.println("接口请求失败");
            }
        });


        return "json";
    }
}
