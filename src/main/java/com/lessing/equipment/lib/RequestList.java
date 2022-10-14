package com.lessing.equipment.lib;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lessing.equipment.common.utils.RequestJson;
import com.lessing.equipment.modules.eq.dto.OperationDTO;
import com.lessing.equipment.modules.eq.entity.EqEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class RequestList {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private RequestJson requestJson;


    /**
     * 设备抓怕
     * */
    public String DeviceSnap(EqEntity eq) {
        ResponseEntity<String> jsonObjectResponseEntity = null;
        String json =null;
        HashMap<String,Object> paramsMap =new HashMap<>();
        paramsMap.put("deviceId",eq.getDeviceid());
        paramsMap.put("channelId","0");
        paramsMap.put("token",requestJson.getToken());
        Map<String, Object> stringObjectMap = RequestJson.paramsInit(paramsMap);
        System.out.println(stringObjectMap);
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(stringObjectMap));
        System.out.println(jsonObject);
        log.debug("设备抓怕接口传入对象{} ",jsonObject);
        jsonObjectResponseEntity = restTemplate.postForEntity("https://openapi.lechange.cn/openapi/setDeviceSnapEnhanced", jsonObject, String.class);
        System.err.println(jsonObjectResponseEntity);
        String jsonObject1 = JSONObject.parseObject(jsonObjectResponseEntity.getBody()).getJSONObject("result").getString("code");
        if(jsonObject1.equals("DV1007")){
            return "";
        }
        System.out.println(jsonObjectResponseEntity);
        String string = JSONObject.parseObject(jsonObjectResponseEntity.getBody()).getJSONObject("result").getJSONObject("data").getString("url");
        return string;


    }

    /**
     * 移动摄像头
     * */
    public String controlMovePTZ(OperationDTO eqByeq) {
        HashMap<String,Object> paramsMap =new HashMap<>();
        paramsMap.put("deviceId",eqByeq.getDeviceid());
        paramsMap.put("channelId","0");
        paramsMap.put("token",requestJson.getToken());
        paramsMap.put("operation",eqByeq.getParam());
        paramsMap.put("duration",eqByeq.getDuration());
        Map<String, Object> stringObjectMap = RequestJson.paramsInit(paramsMap);
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(stringObjectMap));
        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity("https://openapi.lechange.cn/openapi/controlMovePTZ", jsonObject, String.class);
        String string = JSONObject.parseObject(stringResponseEntity.getBody()).getJSONObject("result").getString("code");
        log.debug("移动摄像头{} "+string);
        return string;
    }

    /**
     * 新增设备时创建 hls地址
     * */
    public JSONObject bindDeviceLive(EqEntity eqByeq){
        HashMap<String,Object> paramsMap =new HashMap<>();
        paramsMap.put("deviceId",eqByeq.getDeviceid());
        paramsMap.put("channelId",eqByeq.getChannel());
        paramsMap.put("streamId",0);
        paramsMap.put("token",requestJson.getToken());
        Map<String, Object> stringObjectMap = RequestJson.paramsInit(paramsMap);
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(stringObjectMap));
        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity("https://openapi.lechange.cn/openapi/bindDeviceLive", jsonObject, String.class);
        JSONObject jsonObject1 = JSONObject.parseObject(stringResponseEntity.getBody()).getJSONObject("result").getJSONObject("data").getJSONArray("streams").getJSONObject(0);
        return jsonObject1;
    }



    /**
     * 查询hls地址
     * */
    public JSONObject getRtmpOne(EqEntity eqByeq) {
        HashMap<String,Object> paramsMap =new HashMap<>();
        paramsMap.put("deviceId",eqByeq.getDeviceid());
        paramsMap.put("channelId",eqByeq.getChannel());
        paramsMap.put("token",requestJson.getToken());
        Map<String, Object> stringObjectMap = RequestJson.paramsInit(paramsMap);
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(stringObjectMap));
        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity("https://openapi.lechange.cn/openapi/getLiveStreamInfo", jsonObject, String.class);
        System.out.println(stringResponseEntity);
        JSONObject jsonObject1 = JSONObject.parseObject(stringResponseEntity.getBody()).getJSONObject("result").getJSONObject("data").getJSONArray("streams").getJSONObject(0);
        log.debug("获取hls地址{} "+jsonObject1);
        return jsonObject1;
    }


    /**
     * 绑定设备
     * */
    public String addeq(EqEntity eqByeq) {
        HashMap<String,Object> paramsMap =new HashMap<>();
        paramsMap.put("deviceId",eqByeq.getDeviceid());
        paramsMap.put("code",eqByeq.getPassword());
        paramsMap.put("token",requestJson.getToken());
        Map<String, Object> stringObjectMap = RequestJson.paramsInit(paramsMap);
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(stringObjectMap));
        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity("https://openapi.lechange.cn/openapi/bindDevice", jsonObject, String.class);
        String jsonObject1 = JSONObject.parseObject(stringResponseEntity.getBody()).getJSONObject("result").getString("msg");
        log.debug("绑定设备{} "+jsonObject1);
        return jsonObject1;
    }

    /**
     * 解绑设备
     * */
    public String unBindDevice(EqEntity eqByeq) {
        HashMap<String,Object> paramsMap =new HashMap<>();
        paramsMap.put("deviceId",eqByeq.getDeviceid());
        paramsMap.put("token",requestJson.getToken());
        Map<String, Object> stringObjectMap = RequestJson.paramsInit(paramsMap);
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(stringObjectMap));
        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity("https://openapi.lechange.cn/openapi/unBindDevice", jsonObject, String.class);
        String jsonObject1 = JSONObject.parseObject(stringResponseEntity.getBody()).getJSONObject("result").getString("code");
        log.debug("解绑设备{} "+jsonObject1);
        return jsonObject1;
    }

}
