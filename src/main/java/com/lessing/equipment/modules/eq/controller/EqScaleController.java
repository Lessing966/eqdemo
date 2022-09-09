package com.lessing.equipment.modules.eq.controller;

import com.alibaba.fastjson.JSONObject;
import com.lessing.equipment.common.utils.R;
import com.lessing.equipment.common.utils.RedisUtils;
import com.lessing.equipment.common.utils.StringUtils;
import com.lessing.equipment.lib.RequestList;
import com.lessing.equipment.modules.eq.dto.DTO;
import com.lessing.equipment.modules.eq.dto.RtmpDTO;
import com.lessing.equipment.modules.eq.entity.EqEntity;
import com.lessing.equipment.modules.eq.entity.EqScaleEntity;
import com.lessing.equipment.modules.eq.service.EqService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/eq")
@Api(value = "OpenApiController",tags = "大屏设置分屏API")
public class EqScaleController {

    @Autowired
    private EqService eqService;
    @Autowired
    private RequestList requestList;

    @Autowired
    RedisUtils redisUtils;

    @Value("${openapi.rympurl}")
    String rympUrl;
    @Value("${openapi.closeurl}")
    String closeUrl;

    @ApiOperation("为设备设置分屏")
    @RequestMapping(value = "/insert",method = RequestMethod.POST)
    @Transactional
    public R insert(@RequestBody EqScaleEntity scaleEntity){
        //根据分屏id 查询是否绑定了摄像头 如果绑定 先把此摄像头的播放状态改为未绑定
        EqEntity eq =new EqEntity();
//        RtmpDTO rtmpDTO =new RtmpDTO();
        DTO dto =new DTO();
        Map<String,Object> map =new HashMap<>();
        EqScaleEntity scaleOn = eqService.getScaleOn(scaleEntity.getId());
        EqEntity eqByeq = eqService.getEqByeqId(scaleEntity.getEqId());
        if(null != scaleOn.getEqId()){
            //把绑定的设备解绑
            eq.setEqId(Long.valueOf(scaleOn.getEqId()));
            eq.setLiveStatus(0);
            eqService.updateEq(eq);// 根据分屏id 去修改现绑定的摄像头

            JSONObject rtmpOne = requestList.getRtmpOne(eqByeq);
            dto.setUrl(rtmpOne.getString("hls"));
            dto.setDeviceId(rtmpOne.getString("deviceId"));
            map.put("url",dto.getUrl());
            map.put("deviceid",dto.getDeviceId());

        }else {
            JSONObject rtmpOne = requestList.getRtmpOne(eqByeq);
            dto.setUrl(rtmpOne.getString("hls"));
            dto.setDeviceId(rtmpOne.getString("deviceId"));
            map.put("url",dto.getUrl());
            map.put("deviceid",dto.getDeviceId());
            redisUtils.set(eqByeq.getIp(),dto);
        }
        //如果没绑定分屏 就给绑定摄像头 并把该摄像头的播放状态改为 绑定中
        int i = eqService.updateScale(scaleEntity);
        if(i>0){
            eq.setEqId(Long.valueOf(scaleEntity.getEqId()));
            eq.setLiveStatus(1);
            eqService.updateEq(eq);
            return R.ok().put("data",map);
        }
        return R.error();
    }

    @ApiOperation("取消绑定设备")
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    @Transactional
    public R delete(@RequestBody EqScaleEntity scaleEntity){
        EqScaleEntity scale = eqService.selectScale(scaleEntity.getId());
        EqEntity eq =new EqEntity();
        eq.setLiveStatus(0);
        eq.setEqId(Long.valueOf(scale.getEqId()));
        int i = eqService.updateEq(eq);
        if(i>0){
            scale.setEqId(null);
            eqService.updateScale(scale);
        }
        return R.ok();
    }

}

//class Test{
////    @ApiOperation("为设备设置分屏")
////    @RequestMapping(value = "/insert",method = RequestMethod.POST)
////    @Transactional
////    public R insert(@RequestBody EqScaleEntity scaleEntity){
////        //根据分屏id 查询是否绑定了摄像头 如果绑定 先把此摄像头的播放状态改为未绑定
////        EqEntity eq =new EqEntity();
////        RtmpDTO rtmpDTO =new RtmpDTO();
////        DTO dto =new DTO();
////        Map<String,Object> map =new HashMap<>();
////        EqScaleEntity scaleOn = eqService.getScaleOn(scaleEntity.getId());
////        EqEntity eqByeq = eqService.getEqByeqId(scaleEntity.getEqId());
////        if(null != scaleOn.getEqId()){
////            //把绑定的设备解绑
////            eq.setEqId(Long.valueOf(scaleOn.getEqId()));
////            eq.setLiveStatus(0);
////            eqService.updateEq(eq);// 根据分屏id 去修改现绑定的摄像头
//////            EqEntity eqByeqId = eqService.getEqByeqId(scaleOn.getEqId());
//////            //结束当前设备的推流 并删除当前存放的播放数据
//////            String s = String.valueOf(redisUtils.get(eqByeqId.getIp()));
//////            if(!StringUtils.isEmpty(s)){
//////                DTO dto = JSONObject.parseObject(s, DTO.class);
//////                String code = restTemplate.postForObject(closeUrl, dto.getToken(), JSONObject.class).getString("code");
//////                if(!StringUtils.isEmpty(code) && code.equals("200")){
//////                    redisUtils.delete(eqByeqId.getIp());
//////                }
//////            }
//////            //把新设置的设备进行推流
//////            EqEntity eqByeq = eqService.getEqByeqId(scaleEntity.getEqId());
//////            BeanUtils.copyProperties(eqByeq,rtmpDTO);
//////            DTO dto = restTemplate.postForObject(rympUrl, rtmpDTO, DTO.class);
//////            if(null == dto){
//////                return R.error("推流失败");
//////            }
//////            //将返回的播放地址存放
//////            redisUtils.set(rtmpDTO.getIp(),dto);
////
////            JSONObject rtmpOne = requestList.getRtmpOne(eqByeq);
////            dto.setUrl(rtmpOne.getString("rtmp"));
////            dto.setToken(rtmpOne.getString("deviceId"));
////            map.put("url",dto.getUrl());
////            map.put("token",dto.getToken());
////        }else {
//////            EqEntity eqByeqId = eqService.getEqByeqId(scaleEntity.getEqId());
//////            //把新设置的设备进行推流
//////            BeanUtils.copyProperties(eqByeqId,rtmpDTO);
//////            System.err.println(rtmpDTO);
//////            DTO dto = restTemplate.postForObject(rympUrl, rtmpDTO, DTO.class);
//////            if(ObjectUtils.isEmpty(dto)){
//////                return R.error("推流失败");
//////            }
//////            //将返回的播放地址存放
//////            redisUtils.set(rtmpDTO.getIp(),dto);
//////            map.put("url",dto.getUrl());
//////            map.put("token",dto.getToken());
////
////            JSONObject rtmpOne = requestList.getRtmpOne(eqByeq);
////            dto.setUrl(rtmpOne.getString("rtmp"));
////            dto.setToken(rtmpOne.getString("deviceId"));
////            map.put("url",dto.getUrl());
////            map.put("token",dto.getToken());
////        }
////        //如果没绑定分屏 就给绑定摄像头 并把该摄像头的播放状态改为 绑定中
////        int i = eqService.updateScale(scaleEntity);
////        if(i>0){
////            eq.setEqId(Long.valueOf(scaleEntity.getEqId()));
////            eq.setLiveStatus(1);
////            eqService.updateEq(eq);
////            return R.ok().put("data",map);
////        }
////        return R.error();
////    }
//}
