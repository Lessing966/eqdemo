package com.lessing.equipment.modules.sys.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LoginDTO {

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "工号")
    private String code;
}
