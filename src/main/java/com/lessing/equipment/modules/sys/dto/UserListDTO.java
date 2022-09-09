package com.lessing.equipment.modules.sys.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "用户列表DTO")
public class UserListDTO {
    @ApiModelProperty(value = "id")
    private Integer id;

    @ApiModelProperty(value = "角色")
    private Integer role;
    @ApiModelProperty(value = "小程序头像")
    private String headpg;

    @ApiModelProperty(value = "用户名")
    private String userName;
    @ApiModelProperty(value = "登录名")
    private String loginName;
    @ApiModelProperty(value = "工号")
    private String code;
    @ApiModelProperty(value = "手机号")
    private String phone;

    private Integer gid;
    @ApiModelProperty(value = "所属集团")
    private String gname;
    private Integer oneid;
    @ApiModelProperty(value = "所属一级公司")
    private String onename;
    private Integer twoid;
    @ApiModelProperty(value = "所属二级公司")
    private String twoname;
    private Integer did;
    @ApiModelProperty(value = "所属部门")
    private String dname;
    @ApiModelProperty(value = "状态")
    private Integer status;
    @ApiModelProperty(value = "首次登录标识")
    private Integer num;
    @ApiModelProperty(value = "页号")
    private int pageNumber;

    @ApiModelProperty(value = "页面条数")
    private int pageSize;
}
