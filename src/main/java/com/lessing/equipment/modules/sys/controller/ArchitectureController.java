package com.lessing.equipment.modules.sys.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lessing.equipment.common.utils.JwtUtils;
import com.lessing.equipment.common.utils.R;
import com.lessing.equipment.modules.sys.dto.*;
import com.lessing.equipment.modules.sys.entity.CompanyEntity;
import com.lessing.equipment.modules.sys.entity.DeptEntity;
import com.lessing.equipment.modules.sys.entity.GroupEntity;
import com.lessing.equipment.modules.sys.entity.UserEntity;
import com.lessing.equipment.modules.sys.service.ArchitectureService;
import com.lessing.equipment.modules.sys.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.swing.*;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value = "/sys/at")
@Api(value = "组织架构Controller",tags = "组织架构API")
public class ArchitectureController {

    @Autowired
    private ArchitectureService architectureService;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtils jwtUtils;

    @RequestMapping(value = "/getList",method = RequestMethod.POST)
    @ApiOperation("查询组织架构")
    public R getList(@RequestBody ArchitecDTO architec){
        Set<ArchitectureDTO> list = architectureService.selectGroupList1(architec);
        return R.ok().put("data",list);
    }

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @ApiOperation("新增组织")
    public R add(@RequestBody ArchitecDTO architecDTO){
        if(null == architecDTO){
            return R.error("参数错误");
        }
        if(architectureService.add(architecDTO)){
            return R.ok();
        }
        return R.error("添加失败");
    }


    @RequestMapping(value = "/getGroup",method = RequestMethod.GET)
    @ApiOperation("查询所有集团")
    public R getGroup(HttpServletRequest request){
        List<GroupEntity> groupOneList=null;
        //获取当前用户id和角色
        UserRoleDTO user = jwtUtils.getUid(request);
        if(user.getRole() !=null && user.getRole() ==0){
//            如果是超级管理员返回所有数据
            groupOneList = architectureService.getGroupOneList();
        }else {
//            根据uid和角色 去查询用户
            UserEntity userEntity = userService.selectUserRole(user.getUid(), user.getRole());
            groupOneList= architectureService.getGroupOneByRole(userEntity.getGroupId());
        }
        return R.ok().put("data",groupOneList);
    }

    @RequestMapping(value = "/getComOneList",method = RequestMethod.GET)
    @ApiOperation("根据集团查询一级公司 :gid")
    public R getComBiGid(Integer gid,HttpServletRequest request){
        List<ComopanyNoe> comBiGidOneList=null;
        //获取当前用户id和角色
        UserRoleDTO user = jwtUtils.getUid(request);
        if(null == gid){
            return R.ok().put("data",null);
        }
        if(gid == -1){
            comBiGidOneList= architectureService.getComBiGidAll();  
            return R.ok().put("data",comBiGidOneList);
        }
        if(null == gid && user.getRole() !=null && user.getRole() ==0){
                comBiGidOneList= architectureService.getComBiGidAll();
        }else if(null == gid && user.getRole() !=null && user.getRole() !=0){
            UserEntity userEntity = userService.selectUserRole(user.getUid(), user.getRole());
            comBiGidOneList= architectureService.getComByRole(userEntity.getCompanyoneId());
        }else {
            comBiGidOneList = architectureService.getComBiGid(gid);
        }
        return R.ok().put("data",comBiGidOneList);
    }

    @RequestMapping(value = "/getComTwoList",method = RequestMethod.GET)
    @ApiOperation("根据一级公司查询二级公司:oneid")
    public R getOneCom(Integer oneid){
        List<CompanyEntity> groupOneList =null;
        if(null == oneid){
//            groupOneList = architectureService.getComTwoAll();
            return R.ok().put("data",null);
        }else {
            groupOneList = architectureService.getComTwo(oneid);
        }
        if(oneid == -1){
            groupOneList = architectureService.getComTwoAll();
            return R.ok().put("data",groupOneList);
        }
        return R.ok().put("data",groupOneList);
    }

    @RequestMapping(value = "/getDeptList",method = RequestMethod.GET)
    @ApiOperation("根据二级公司查询部门:twoid")
    public R getDeptList(Integer twoid){
        List<DeptEntity> groupOneList=null;
        if(null == twoid){
            return R.ok().put("data",null);
//            groupOneList= architectureService.getDeptListAll();
        }else {
            groupOneList= architectureService.getDeptList(twoid);
        }
        if(twoid == -1){
            groupOneList= architectureService.getDeptListAll();
            return R.ok().put("data",groupOneList);
        }
        return R.ok().put("data",groupOneList);
    }

//    @RequestMapping(value = "/getAllListByGid",method = RequestMethod.GET)
//    @ApiOperation("根据集团id查询所属数据 :gid")
//    public R getAllListByGid(Integer gid){
//        List<ArchitectureDTO> list = architectureService.selectAllListByGid(gid);
//        if(null == list && list.size() <=0){
//            return R.error("暂无数据");
//        }
//        return R.ok().put("data",list);
//    }

    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @ApiOperation("编辑组织")
    public R update(@RequestBody ArchitecDTO dto){
        if(architectureService.update(dto)){
            return R.ok();
        }
        return R.error("编辑失败");
    }


    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    @ApiOperation("删除组织")
    private R delete(@RequestBody DeleteDTO dto){
        if(null == dto){
            return R.error("参数错误");
        }
        if(architectureService.delete(dto)){
            return R.ok();
        }
        return R.error("删除失败");
    }

}