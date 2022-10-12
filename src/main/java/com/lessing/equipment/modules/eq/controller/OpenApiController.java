package com.lessing.equipment.modules.eq.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lessing.equipment.common.utils.*;

import com.lessing.equipment.modules.eq.dto.EqListDTO;
import com.lessing.equipment.modules.eq.entity.EqEntity;
import com.lessing.equipment.modules.eq.entity.EqScaleEntity;
import com.lessing.equipment.modules.eq.service.EqService;
import com.lessing.equipment.modules.sys.dao.ProjectDao;
import com.lessing.equipment.modules.sys.dto.LivelistDTO;
import com.lessing.equipment.modules.sys.dto.UserListDTO;
import com.lessing.equipment.modules.sys.dto.UserRoleDTO;
import com.lessing.equipment.modules.sys.entity.ProjectEntity;
import com.lessing.equipment.modules.sys.entity.UserEntity;
import com.lessing.equipment.modules.sys.service.UserService;
import com.lessing.equipment.sdk.PTZControlUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
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
    private ProjectDao projectDao;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserService userService;
    /**
     * 获取摄像头的列表
     * @return
     */
    @ApiOperation("获取摄像头列表")
    @RequestMapping(value = "/getliveList",method = RequestMethod.GET)
    public R getliveList(HttpServletRequest request){
        List<EqEntity> eqEntity=null;
        UserRoleDTO user = jwtUtils.getUid(request);
        UserEntity userEntity = userService.selectUserOneByUid(user.getUid());
        switch (user.getRole()){
            case 0: //超级管理员
                System.out.println("超级管理员");
                //超级管理员查询所有设备
                eqEntity = eqService.getEq();
                break;
            case 1: //集团管理员
                System.out.println("集团管理员");
                eqEntity = eqService.selectEqlistByRole(userEntity.getGroupId(),0,0,0);
                break;
            case 2: //一级管理员
                System.out.println("一级管理员");
                eqEntity = eqService.selectEqlistByRole(0,userEntity.getCompanyoneId(),0,0);
                break;
            case 3: //二级管理员
                System.out.println("二级管理员");
                eqEntity = eqService.selectEqlistByRole(0,0,userEntity.getCompanytwoId(),0);
                break;
            case 4: //部门管理员
                System.out.println("部门管理员");
                eqEntity = eqService.selectEqlistByRole(0,0,0,userEntity.getDeptId());
                break;
            default:
                //普通员工
                System.out.println("普通员工");
                eqEntity =eqService.getProjList(user.getUid());
                break;
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
            ProjectEntity project=new ProjectEntity();
            if(null != eq.getEqId()){
                EqEntity eqEntity = eqService.getEqByeqId(eq.getEqId());
                if(null == eqEntity){
                    return R.error("没有设备");
                }
                if(null != eqEntity.getPId()){
                     project = projectDao.selectOne(new QueryWrapper<ProjectEntity>().eq("id", eqEntity.getPId()));
                }

                String s = redisUtils.get(eqEntity.getIp());
                if(null != s &&!StringUtils.isEmpty(s)){
                    LivelistDTO livelistDTO = JSONObject.parseObject(s, LivelistDTO.class);
                    livelistDTO.setCameraSn(eqEntity.getCameraSn());
                    livelistDTO.setPaddress(project.getAddress());
                    livelistDTO.setPname(project.getName());

                    livelistDTO.setEqId(eqEntity.getEqId());
                    livelistDTO.setUsername(eqEntity.getUsername());
                    livelistDTO.setPassword(eqEntity.getPassword());
                    livelistDTO.setIp(eqEntity.getIp());
                    livelistDTO.setDeviceid(eqEntity.getDeviceid());
                    livelistDTO.setChannelId(eqEntity.getChannel());
                    livelistDTO.setEqStatus(eqEntity.getEqStatus());
                    livelistDTO.setLiveStatus(eqEntity.getLiveStatus());
//                    LivelistDTO livelistDTO =new LivelistDTO();
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