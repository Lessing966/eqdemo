package com.lessing.equipment.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lessing.equipment.common.utils.ExcelColumn;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@TableName(value = "sys_user")
@Data
@ApiModel(description = "用户表")
public class UserEntity {

    @ApiModelProperty(value = "用户ID")
    private Long id;

    @ApiModelProperty(value = "角色")
    private Integer role;

    @ApiModelProperty(value = "小程序头像")
    private String headpg;

    @ExcelColumn(value = "用户名", col = 0)
    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "登录名")
    private String loginName;

    @ApiModelProperty(value = "密码")
    private String password;

    @ExcelColumn(value = "工号", col = 1)
    @ApiModelProperty(value = "工号")
    private String code;

    @ExcelColumn(value = "手机号", col = 2)
    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "部门ID")
    @TableField(updateStrategy = FieldStrategy.IGNORED )
    private Integer deptId;
    @ApiModelProperty(value = "一级公司ID")
    @TableField(updateStrategy = FieldStrategy.IGNORED )
    private Integer companyoneId;
    @ApiModelProperty(value = "二级公司ID")
    @TableField(updateStrategy = FieldStrategy.IGNORED )
    private Integer companytwoId;
    @ApiModelProperty(value = "集团ID")
    @TableField(updateStrategy = FieldStrategy.IGNORED )
    private Integer groupId;
    @ApiModelProperty(value = "用户状态")
    private Integer status;

    @ApiModelProperty(value = "登录次数标题")
    @TableField(updateStrategy = FieldStrategy.IGNORED )
    private Integer num;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}
