package com.lessing.equipment.modules.app.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lessing.equipment.common.utils.JwtUtils;
import com.lessing.equipment.common.utils.R;
import com.lessing.equipment.common.utils.RedisUtils;
import com.lessing.equipment.common.utils.StringUtils;
import com.lessing.equipment.lib.RequestList;
import com.lessing.equipment.modules.app.dao.AlramListDTO;
import com.lessing.equipment.modules.app.dto.EqDTO;
import com.lessing.equipment.modules.eq.dto.DTO;
import com.lessing.equipment.modules.eq.dto.RtmpDTO;
import com.lessing.equipment.modules.eq.entity.EqEntity;
import com.lessing.equipment.modules.eq.entity.EqalrmEnyity;
import com.lessing.equipment.modules.eq.service.EqService;
import com.lessing.equipment.modules.eq.service.Impl.EqSerivceImpl;
import com.lessing.equipment.modules.sys.dto.UserRoleDTO;
import com.lessing.equipment.modules.sys.entity.UserEntity;
import com.lessing.equipment.modules.sys.service.UserService;
import com.lessing.equipment.sdk.FileDelete;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@RestController
@Api(value = "UserController",tags = "小程序接口")
@RequestMapping(value = "/sys/app")
public class AppController {

    @Autowired
    private UserService userService;
    @Autowired
    private EqService eqService;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private RestTemplate restTemplate;
    @Value("${openapi.zpurl}")
    String zpurl;
    @Value("${openapi.local}")
    String local;

    @Autowired
    private RequestList requestList;
    @Value("${openapi.rympurl}")
    String rympUrl;

    @ApiOperation("修改密码")
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public R update(@RequestBody UserEntity user){
        if(userService.updatePassword(user)){
            return R.ok();
        }
        return R.error();
    }

    @ApiOperation("抓拍")
    @RequestMapping(value = "/setDeviceSnapEnhanced",method = RequestMethod.POST)
    public R setDeviceSnapEnhanced(@RequestBody EqEntity eq){
        String s = requestList.DeviceSnap(eq);
        if(!StringUtils.isEmpty(s)) {
            try {
                EqSerivceImpl.download(s, local, eq.getIp() + ".jpg");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("结束");
            return R.ok(zpurl+eq.getIp()+".jpg");
        }
        return R.ok(zpurl+"err"+".png");
    }

    @ApiOperation("保存头像")
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public R save(@RequestBody UserEntity user){
        if(userService.save(user)){
            UserEntity userEntity = userService.selectUserOneByUid(String.valueOf(user.getId()));
            return R.ok().put("data",userEntity);
        }
        return R.error();
    }


    /**
     * 获取摄像头的列表
     * @return
     */
    @ApiOperation("获取摄像头列表")
    @RequestMapping(value = "/getliveList",method = RequestMethod.GET)
    public R getliveList(String eSn ,HttpServletRequest request) throws IOException {
        List<EqDTO> eqEntity=null;
        UserRoleDTO user = jwtUtils.getUid(request);
        UserEntity userEntity = userService.selectUserOneByUid(user.getUid());
        switch (user.getRole()){
            case 0: //超级管理员
                System.out.println("超级管理员");
                //超级管理员查询所有设备
                eqEntity = eqService.getEqList(eSn);
                break;
            case 1: //集团管理员
                System.out.println("集团管理员");
                eqEntity = eqService.selectEqlistByRoleapp(userEntity.getGroupId(),0,0,0,eSn);
                break;
            case 2: //一级管理员
                System.out.println("一级管理员");
                eqEntity = eqService.selectEqlistByRoleapp(0,userEntity.getCompanyoneId(),0,0,eSn);
                break;
            case 3: //二级管理员
                System.out.println("二级管理员");
                eqEntity = eqService.selectEqlistByRoleapp(0,0,userEntity.getCompanytwoId(),0,eSn);
                break;
            case 4: //部门管理员
                System.out.println("部门管理员");
                eqEntity = eqService.selectEqlistByRoleapp(0,0,0,userEntity.getDeptId(),eSn);
                break;
            default:
                //普通员工
                System.out.println("普通员工");
                eqEntity =eqService.getProjEqList(eSn,user.getUid());
                break;
        }
        return R.ok().put("data",eqEntity);
    }


    /**
     * 删除所有抓拍图
     * @return
     */
    @ApiOperation("删除所有抓拍图")
    @RequestMapping(value = "/deleteSanp",method = RequestMethod.GET)
    public R sanp() {
        try {
            boolean deletefile = FileDelete.deletefile();
            if(deletefile){
                return R.ok();
            }
            return R.error();
        } catch (Exception e) {
            e.printStackTrace();
            return R.error();
        }
    }

    @ApiOperation("查看事件列表")
    @RequestMapping(value = "getAlrm",method = RequestMethod.POST)
    public R getArlm(@RequestBody AlramListDTO alramListDTO){
        Page<EqalrmEnyity> list = eqService.selectAlrmList(alramListDTO);
        return R.ok().put("data",list);
    }

    @ApiOperation("查看单个设备播放")
    @RequestMapping(value = "getRemp",method = RequestMethod.POST)
    public R getRemp(@RequestBody RtmpDTO rtmp){
        DTO dto =new DTO();
        //查询redis中是否有再推流的ip 如果有 就直接返回 没有就推流并放入redis~
        String s = redisUtils.get(rtmp.getIp());
        if(null != s){
            dto = JSONObject.parseObject(s, DTO.class);
            if(null != dto.getDeviceId()){
                return R.ok().put("data",dto);
            }
        }else {
            EqEntity eq =new EqEntity();
            eq.setDeviceid(rtmp.getDeviceid());
            eq.setChannel("0");
            JSONObject rtmpOne = requestList.getRtmpOne(eq);
            dto.setUrl(rtmpOne.getString("hls"));
            dto.setDeviceId(rtmpOne.getString("deviceId"));
            //将返回的播放地址存放
            redisUtils.set(rtmp.getIp(),dto);

        }
        return R.ok().put("data",dto);
    }

}