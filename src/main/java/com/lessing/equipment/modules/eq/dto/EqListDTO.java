package com.lessing.equipment.modules.eq.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class EqListDTO {
    @ApiModelProperty(value = "设备id")
    private Integer id;
    @ApiModelProperty(value = "设备编号")
    private String sn;
    @ApiModelProperty(value = "IP")
    private String ip;
    @ApiModelProperty(value = "账号")
    private String username;
    @ApiModelProperty(value = "序列号")
    private String deviceid;
    @ApiModelProperty(value = "密码")
    private String password;
    @ApiModelProperty(value = "项目名称")
    private String pname;
    @ApiModelProperty(value = "项目地址")
    private String paddress;
    @ApiModelProperty(value = "项目状态")
    private Integer status;
    @ApiModelProperty(value = "备注")
    private String note;
    @ApiModelProperty(value = "页面页数")
    private Integer PageNumber;
    @ApiModelProperty(value = "页面条数")
    private Integer PageSize;
}
