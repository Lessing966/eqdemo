package com.lessing.equipment.modules.eq.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OperationDTO {
    @ApiModelProperty(value = "ip")
    private String ip;
    @ApiModelProperty(value = "账号")
    private String username;
    @ApiModelProperty(value = "密码")
    private String password;
    @ApiModelProperty(value = "设备端口")
    private Integer prot;
    @ApiModelProperty(value = "通道号")
    private Integer channel;
    @ApiModelProperty(value = "序列号")
    private String deviceid;
    @ApiModelProperty(value = "操作参数：0-上，1-下，2-左，3-右，4-左上，5-左下，6-右上，7-右下，8-放大，9-缩小，10-停止")
    private String param;
    @ApiModelProperty(value = "移动持续时间，单位毫秒")
    private Long duration;

}
