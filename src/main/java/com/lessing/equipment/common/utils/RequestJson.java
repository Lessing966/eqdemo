package com.lessing.equipment.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@Slf4j
public class RequestJson {

    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private RestTemplate restTemplate;


    public String getToken(){
        String accessToken =null;
        accessToken = redisUtils.get("accesstoken");
        if(null == accessToken){
            log.info("重新请求 Token");
            accessToken = this.RequsetToken();
            return accessToken;
        }else {
            log.info("redis中的 Token");
            return accessToken;
        }
    }


    public String RequsetToken(){
        Map<String, Object> stringObjectMap = paramsInit(new HashMap<>());
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(stringObjectMap));
        ResponseEntity<String> jsonObjectResponseEntity = restTemplate.postForEntity("https://openapi.lechange.cn/openapi/accessToken", jsonObject, String.class);
        JSONObject json = JSONObject.parseObject(jsonObjectResponseEntity.getBody()).getJSONObject("result").getJSONObject("data");
        //拿到token 存放redis
        redisUtils.set("accesstoken",json.getString("accessToken"),json.getLong("expireTime"));
        return json.getString("accessToken");
    }


    public static Map<String, Object> paramsInit(Map<String, Object> paramsMap) {
        long time = System.currentTimeMillis() / 1000;
        String nonce = UUID.randomUUID().toString();
        String id = UUID.randomUUID().toString();

        StringBuilder paramString = new StringBuilder();
        paramString.append("time:").append(time).append(",");
        paramString.append("nonce:").append(nonce).append(",");
        paramString.append("appSecret:").append("7efb1c11960a4a4188654ea55079aa");

        String sign = "";
        // 计算MD5得值
        try {
            sign = DigestUtils.md5Hex(paramString.toString().trim().getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map<String, Object> systemMap = new HashMap<String, Object>();
        systemMap.put("ver", "1.0");
        systemMap.put("sign", sign);
        systemMap.put("appId", "lcdc677243e9a94cca");
        systemMap.put("nonce", nonce);
        systemMap.put("time", time);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("system", systemMap);
        map.put("params", paramsMap);
        map.put("id", id);
        return map;
    }

}
