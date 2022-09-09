package com.lessing.equipment.modules.sys.controller;


import com.lessing.equipment.common.utils.*;
import com.lessing.equipment.modules.sys.dto.LoginDTO;
import com.lessing.equipment.modules.sys.entity.UserEntity;
import com.lessing.equipment.modules.sys.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/sys")
@CrossOrigin
@Api(value = "登录Controller",tags = "系统登录接口")
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 登录
     * @param loginDTO
     * @return
     */
    @ApiOperation(value = "登陆", notes = "参数:手机号 工号")
    @PostMapping("/login")
    public R login(@RequestBody LoginDTO loginDTO) {
        if(StringUtils.isEmpty(loginDTO.getPhone()) || StringUtils.isEmpty(loginDTO.getCode())){
            return R.error(500,"错误参数");
        }
        UserEntity userBase = userService.getUserBase(loginDTO.getPhone());
        if(ObjectUtils.isEmpty(userBase)){
            return R.error(501,"没有此用户");
        }
        if(!MD5Utils.MD5Encode(loginDTO.getCode()).equals(userBase.getPassword())){
            return R.error(501,"密码错误");
        }
        //登录成功
        String token = JwtUtils.createToken(String.valueOf(userBase.getId()), userBase.getPhone());
        if(!StringUtils.isEmpty(token)){
            //存入redis  默认24小时
            redisUtils.set(userBase.getId()+":token",token);
        }
        return R.ok().put("token",token)
                .put("data",userBase.getRole())
                .put("uid",userBase.getId())
                .put("username",userBase.getUserName())
                .put("num",userBase.getNum())
                .put("loginname",userBase.getLoginName())
                .put("headpg",userBase.getHeadpg());
    }

}