package com.lessing.equipment.modules.eq.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


import java.util.Date;

@Data
@TableName("eq_alrm")
public class EqalrmEnyity {
    @ApiModelProperty(value = "报警ID")
    private Integer id;
    @ApiModelProperty(value = "")
    private String chn;
    @ApiModelProperty(value = "报警类型")
    private String type;
    @ApiModelProperty(value = "报警状态")
    private Integer status;
    @ApiModelProperty(value = "所属设备编号")
    private String eSn;
    @ApiModelProperty(value = "所属项目地址")
    private String pAddress;
    @ApiModelProperty(value = "报警录像")
    private String url;
    @ApiModelProperty(value = "报警开始时间")
    private Date startTime;

    @ApiModelProperty(value = "报警结束时间")
    private Date stopTime;

    @TableField(exist = false)
    private String startDate;
    @TableField(exist = false)
    private String stopDate;

}
