package com.lessing.equipment.modules.sys.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lessing.equipment.common.utils.R;
import com.lessing.equipment.modules.sys.dto.ArchitecDTO;
import com.lessing.equipment.modules.sys.dto.ProjectListDTO;
import com.lessing.equipment.modules.sys.entity.ProjectEntity;
import com.lessing.equipment.modules.sys.service.ProjectService;
import com.lessing.equipment.modules.sys.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "项目Controller",tags = "项目管理API")
@RequestMapping(value = "/sys/poj")
public class ProjectController {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/getPojList",method = RequestMethod.POST)
    @ApiOperation("项目列表")
    public R getPoj(@RequestBody ProjectListDTO project){
        Page<ProjectListDTO> list = projectService.selectPojList(project);
        return R.ok().put("data",list);
    }

    @RequestMapping(value = "/getUsername",method = RequestMethod.POST)
    @ApiOperation("根据部门id查询用户：architecDTO")
    public R getUsername(@RequestBody ArchitecDTO architecDTO){
        if(null == architecDTO){
            return R.error("参数错误");
        }
        return R.ok().put("data",userService.selectUserList(architecDTO));
    }

    @RequestMapping(value = "/addPoj",method = RequestMethod.POST)
    @ApiOperation("新增项目")
    public R addPoj(@RequestBody ProjectEntity project){
        if(projectService.addPoj(project)){
            return R.ok();
        }
        return R.error("新增失败");
    }

    @RequestMapping(value = "/getpojOne",method = RequestMethod.GET)
    @ApiOperation("项目详情")
    public R getPojOne(Integer id){
        return R.ok().put("data",projectService.selectPojOne(id));
    }

    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @ApiOperation("编辑项目")
    public R update(@RequestBody ProjectEntity project){
        if(projectService.update(project)){
            return R.ok();
        }
        return R.error("编辑失败");
    }

    @RequestMapping(value = "/delete",method = RequestMethod.GET)
    @ApiOperation("删除项目")
    public R delete(Integer id){
        if(projectService.delete(id)){
            return R.ok();
        }
        return R.error("删除失败");
    }


}