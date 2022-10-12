package com.lessing.equipment.modules.sys.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lessing.equipment.common.utils.*;
import com.lessing.equipment.modules.eq.entity.EqEntity;
import com.lessing.equipment.modules.eq.service.EqService;
import com.lessing.equipment.modules.sys.dto.CameraDTO;
import com.lessing.equipment.modules.sys.dto.UserListDTO;
import com.lessing.equipment.modules.sys.dto.UserRoleDTO;
import com.lessing.equipment.modules.sys.entity.UserEntity;
import com.lessing.equipment.modules.sys.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;


@Api(value = "用户Controller", tags = {"用户模块API"})
@RequestMapping(value = "/sys")
@RestController
public class SysUserController {

    @Autowired
    private UserService userService;

    @ApiOperation("系统用户列表")
    @RequestMapping(value = "/userList",method = RequestMethod.POST)
    public R getUserList(@RequestBody UserListDTO user){
        Page<UserListDTO> list = userService.getUserList(user);
        return R.ok().put("data",list);
    }

    @ApiOperation("添加用户")
    @RequestMapping(value = "/addUser",method = RequestMethod.POST)
    public R addUser(@RequestBody UserEntity user){
        UserEntity phone = userService.selectUser(user.getPhone());
        if(null != phone){
            return R.error("手机号重复");
        }
        UserEntity code = userService.selectcode(user.getCode());
        if(null != code){
            return R.error("工号重复");
        }
        if(userService.addUser(user)){
            return R.ok();
        }
        return R.error("新增失败");
    }

    @ApiOperation("用户详情")
    @RequestMapping(value = "/getUserOne",method = RequestMethod.GET)
    public R getUserOne(Integer uid){
        return R.ok().put("data",userService.selectUserOne(uid));
    }

    @ApiOperation("编辑用户")
    @RequestMapping(value = "/updateUser",method = RequestMethod.POST)
    public R update(@RequestBody UserEntity user){
        if(userService.update(user)){
            return R.ok();
        }
        return R.error("编辑失败");
    }

    @ApiOperation("删除用户")
    @RequestMapping(value = "/delete",method = RequestMethod.GET)
    public R delete(Integer uid){
        if(userService.delete(uid)){
            return R.ok();
        }
         return R.error();
    }

    @ApiOperation("重置密码")
    @RequestMapping(value = "/reset",method = RequestMethod.POST)
    public R reset(@RequestBody UserEntity user){
        UserListDTO userdto = userService.selectUserOne(Math.toIntExact(user.getId()));
        user.setPassword(MD5Utils.MD5Encode(userdto.getCode()));
        if(userService.update(user)){
            return R.ok();
        }
        return R.error("重置失败");
    }

    @RequestMapping(value="/downloadExcel",method = RequestMethod.GET)
    @ApiOperation("下载模板")
    public ResponseEntity<Resource> downloadExcel(HttpServletResponse response) {
        try {
            ClassPathResource cpr = new ClassPathResource("/template/"+"人员信息.xlsx");
            InputStream is = cpr.getInputStream();
            Workbook workbook = new XSSFWorkbook(is);
            String fileName = "人员信息.xlsx";
            ExcelTemplateUtil.downLoadExcel(fileName, response, workbook);

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return new ResponseEntity<Resource>(HttpStatus.OK);
    }


    @RequestMapping(value = "/importExcel",method = RequestMethod.POST)
    @ApiOperation("一键导入")
    public R importExcel(@RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            //导入转换成实体的list
            List<UserEntity> userModelList = ExcelUtils.readExcel("", UserEntity.class, file);
            if(userModelList.size()==0){
                return R.error("识别文件数据失败！");
            }
            for(UserEntity user : userModelList){
                if(StringUtils.isEmpty(user.getUserName()) ||
                        StringUtils.isEmpty(user.getPhone()) || StringUtils.isEmpty(user.getCode())){
                    return R.error("字段全为必填项");
                }
                UserEntity userEntity = userService.selectUser(user.getPhone());
                if(!ObjectUtils.isEmpty(userEntity)){
                    return R.error("手机号不可重复");
                }
                UserEntity entity = userService.selectcode(user.getCode());
                if(!ObjectUtils.isEmpty(entity)){
                    return R.error("工号不可重复");
                }
                userService.addUser(user);
            }
            return R.ok();
        }catch (Exception e){
            e.printStackTrace();
            return R.error();
        }
    }

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private EqService eqService;

    @RequestMapping(value = "/getEq",method = RequestMethod.GET)
    @ApiOperation("查询用户绑定设备")
    public R getEq(String uid){
        List<EqEntity> eqEntity=null;
//        UserRoleDTO user = jwtUtils.getUid(request);
        UserEntity userEntity = userService.selectUserOneByUid(uid);
        switch (userEntity.getRole()){
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
                eqEntity =eqService.getProjList(String.valueOf(userEntity.getId()));
                break;
        }
//        List<CameraDTO> list = userService.selectEq(uid);
        return R.ok().put("data",eqEntity);
    }


}