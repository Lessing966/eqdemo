package com.lessing.equipment.modules.eq.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RtmpDTO {
    @ApiModelProperty(value = "设备ip")
    private String ip;
    @ApiModelProperty(value = "设备账号")
    private String username;
    @ApiModelProperty(value = "设备密码")
    private String password;
    private String deviceid;
    @ApiModelProperty(value = "通道号")
    private String channel;
}
