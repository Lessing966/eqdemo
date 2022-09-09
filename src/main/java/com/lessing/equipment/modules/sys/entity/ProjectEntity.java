package com.lessing.equipment.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("sys_project")
@ApiModel(description = "项目表")
public class ProjectEntity {
    @ApiModelProperty(value = "项目ID")
    private Integer id;
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "地址")
    private String address;
    @ApiModelProperty(value = "所属集团id")
    @TableField(updateStrategy = FieldStrategy.IGNORED )
    private Integer groupId;
    @ApiModelProperty(value = "一级公司id")
    @TableField(updateStrategy = FieldStrategy.IGNORED )
    private Integer cpevelOne;
    @ApiModelProperty(value = "二级公司id")
    @TableField(updateStrategy = FieldStrategy.IGNORED )
    private Integer cpevelTwo;
    @ApiModelProperty(value = "部门id")
    @TableField(updateStrategy = FieldStrategy.IGNORED )
    private Integer deptId;
    @ApiModelProperty(value = "项目经理")
    private String head;
    @ApiModelProperty(value = "安全员")
    private String hon;

    @ApiModelProperty(value = "项目人员")
//    @TableField(exist = false)
    private String username;
    @ApiModelProperty(value = "项目状态")
    private Integer status;
}
