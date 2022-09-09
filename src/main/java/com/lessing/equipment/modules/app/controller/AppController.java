package com.lessing.equipment.modules.app.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lessing.equipment.common.utils.JwtUtils;
import com.lessing.equipment.common.utils.R;
import com.lessing.equipment.common.utils.RedisUtils;
import com.lessing.equipment.modules.app.dao.AlramListDTO;
import com.lessing.equipment.modules.app.dto.EqDTO;
import com.lessing.equipment.modules.eq.dto.DTO;
import com.lessing.equipment.modules.eq.dto.RtmpDTO;
import com.lessing.equipment.modules.eq.entity.EqalrmEnyity;
import com.lessing.equipment.modules.eq.service.EqService;
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
    public R getliveList(String eSn ,HttpServletRequest request){
        List<EqDTO> eqEntity=null;
        UserRoleDTO user = jwtUtils.getUid(request);
        if(null != user.getRole() && user.getRole() == 0){
            //超级管理员查询所有设备
            eqEntity = eqService.getEqList(eSn);
        }else {
            eqEntity =eqService.getProjEqList(eSn,user.getUid());
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
        //查询redis中是否有再推流的ip 如果有 就直接返回 没有就推流并放入redis
        String s = redisUtils.get(rtmp.getIp());
        DTO dto = JSONObject.parseObject(s, DTO.class);
        if(dto.getDeviceId()!= null){
            return R.ok().put("data",dto);
        }
        DTO dto1 = restTemplate.postForObject(rympUrl, rtmp, DTO.class);
        if(null == dto1){
            return R.error("推流失败");
        }
        //将返回的播放地址存放
        redisUtils.set(rtmp.getIp(),dto1);

        return R.ok().put("data",dto1);
    }

}