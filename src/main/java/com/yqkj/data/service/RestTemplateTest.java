package com.yqkj.data.service;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class RestTemplateTest {
    @Autowired
    private RestTemplate restTemplate;

    /**
     * 以get方式请求第三方http接口 getForEntity
     * @param url
     * @return
     */
    public JSONObject doGetForEntity(String url) {
        ResponseEntity<JSONObject> responseEntity = restTemplate.getForEntity(url, JSONObject.class);
        return responseEntity.getBody();
    }

    /**
     * 以get方式请求第三方http接口 getForObject
     * 返回值返回的是响应体
     * @param url
     * @return
     */
    public JSONObject doGetForObject(String url) {
        JSONObject result = restTemplate.getForObject(url, JSONObject.class);
        return result;
    }

    /**
     * 以post方式请求第三方http接口 postForEntity
     * @param url
     * @return
     */
    public JSONObject doPostForEntity(String url) {
        //可设置请求参数
        JSONObject param = new JSONObject();
        param.put("auth_token","12345");
        ResponseEntity<JSONObject> responseEntity = restTemplate.postForEntity(url, param, JSONObject.class);
        return responseEntity.getBody();
    }

    /**
     * 以post方式请求第三方http接口 postForObject
     * @param url
     * @return
     */
    public JSONObject doPostForObject(String url) {
        //可设置请求参数
        JSONObject param = new JSONObject();
        param.put("auth_token","12345");
        JSONObject result = restTemplate.postForObject(url, param, JSONObject.class);
        return result;
    }


    /**
     * exchange方法请求第三方http接口
     *
     */
    public JSONObject doExchange(String url,String token) {
        //设置header中参数
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("auth_token",token);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        //设置请求参数
        JSONObject param = new JSONObject();
        param.put("auth_token",token);
        //创建请求对象
        HttpEntity<JSONObject> request = new HttpEntity<>(param,httpHeaders);
        //执行请求(请求路径，请求方式，请求体，响应体)
        ResponseEntity<JSONObject> responseEntity = restTemplate.exchange(url, HttpMethod.POST, request, JSONObject.class);
        return responseEntity.getBody();
    }
}