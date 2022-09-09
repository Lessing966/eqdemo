package com.lessing.equipment.modules.sys.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class LivelistDTO {
    @ApiModelProperty(value = "播放地址")
    private String url;
    @ApiModelProperty(value = "token")
    private String token;
    @ApiModelProperty(value = "设备IP")
    private String ip;
    @ApiModelProperty(value = "设备序列号")
    private String deviceid;
    @ApiModelProperty(value = "设备账号")
    private String username;
    @ApiModelProperty(value = "设备密码")
    private String password;
    @ApiModelProperty(value = "设备通道号")
    private String channelId;
    @ApiModelProperty(value = "项目地址")
    private String paddress;
    @ApiModelProperty(value = "设备编号")
    private String cameraSn;
    @ApiModelProperty(value = "设备ID")
    private Long eqId;
    @ApiModelProperty(value = "设备状态")
    private Integer eqStatus;
    @ApiModelProperty(value = "设备播放状态")
    private Integer liveStatus;
    @ApiModelProperty(value = "分屏ID")
    private Long sId;
}
