package com.lessing.equipment.modules.eq.controller;

import com.alibaba.fastjson.JSONObject;
import com.lessing.equipment.common.utils.*;

import com.lessing.equipment.modules.eq.entity.EqEntity;
import com.lessing.equipment.modules.eq.entity.EqScaleEntity;
import com.lessing.equipment.modules.eq.service.EqService;
import com.lessing.equipment.modules.sys.dto.LivelistDTO;
import com.lessing.equipment.modules.sys.dto.UserRoleDTO;
import com.lessing.equipment.sdk.PTZControlUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping(value = "/eq")
@Log4j2
@Api(value = "OpenApiController",tags = "大屏监控API")
public class OpenApiController {
    Logger logger = LogManager.getLogger(this.getClass());
    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private EqService eqService;

    @Autowired
    private JwtUtils jwtUtils;
    /**
     * 获取摄像头的列表
     * @return
     */
    @ApiOperation("获取摄像头列表")
    @RequestMapping(value = "/getliveList",method = RequestMethod.GET)
    public R getliveList(HttpServletRequest request){
        List<EqEntity> eqEntity=null;
        UserRoleDTO user = jwtUtils.getUid(request);
        if(null != user.getRole() && user.getRole() == 0){
            //超级管理员查询所有设备
           eqEntity = eqService.getEq();
        }else {
            eqEntity =eqService.getProjList(user.getUid());
        }
        return R.ok().put("data",eqEntity);
    }

    /**
     * 获取大屏选择框
     * @param location
     * @return
     */
    @ApiOperation("大屏选择分屏")
    @RequestMapping(value = "/geteqlive",method = RequestMethod.GET)
    public R getEqScale(String location){
        if(StringUtils.isEmpty(location)){
            return R.error(501,"参数不存在");
        }
        List<LivelistDTO> list1 = new ArrayList<>();
        List<EqScaleEntity> list = eqService.getEqScale(location);
        for(EqScaleEntity eq : list){
            if(null != eq.getEqId()){
                EqEntity eqEntity = eqService.getEqByeqId(eq.getEqId());
                if(null == eqEntity){
                    return R.error();
                }
                String s = redisUtils.get(eqEntity.getIp());
                if(null != s &&!StringUtils.isEmpty(s)){
                    LivelistDTO livelistDTO = JSONObject.parseObject(s, LivelistDTO.class);
                    livelistDTO.setCameraSn(eqEntity.getCameraSn());
                    livelistDTO.setPaddress(eqEntity.getPAddress());
                    livelistDTO.setEqId(eqEntity.getEqId());
                    livelistDTO.setUsername(eqEntity.getUsername());
                    livelistDTO.setPassword(eqEntity.getPassword());
                    livelistDTO.setIp(eqEntity.getIp());
                    livelistDTO.setDeviceid(eqEntity.getDeviceid());
                    livelistDTO.setChannelId(eqEntity.getChannel());
                    livelistDTO.setEqStatus(eqEntity.getEqStatus());
                    livelistDTO.setLiveStatus(eqEntity.getLiveStatus());
                    livelistDTO.setSId(eq.getId());
                    list1.add(livelistDTO);
                }
            }else {
                LivelistDTO livelistDTO =new LivelistDTO();
                livelistDTO.setSId(eq.getId());
                list1.add(livelistDTO);
            }
        }
        return R.ok().put("data",list1);
    }


    //开始订阅事件
//    @PostConstruct
//    public void s(){
//        logger.info("开始订阅事件");
//        List<EqEntity> eq = eqService.getEq();
//        for(EqEntity e : eq){
//            PTZControlUtil.startListen(e.getIp(),e.getUsername(),e.getPassword());
//        }
//    }
}