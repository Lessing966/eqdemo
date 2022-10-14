package com.lessing.equipment.modules.eq.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lessing.equipment.common.utils.ExcelColumn;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("sys_eq")
public class EqEntity {
    @ApiModelProperty(value = "设备ID")
    private Long eqId;

    @ExcelColumn(value = "设备编号", col = 0)
    @ApiModelProperty(value = "设备编号")
    private String cameraSn;

    @ExcelColumn(value = "设备IP", col = 1)
    @ApiModelProperty(value = "设备IP")
    private String ip;

    @ExcelColumn(value = "序列号", col = 4)
    @ApiModelProperty(value = "序列号")
    private String deviceid;

    @ExcelColumn(value = "设备账号", col = 2)
    @ApiModelProperty(value = "设备账号")
    private String username;

    @ExcelColumn(value = "设备密码", col = 3)
    @ApiModelProperty(value = "设备密码")
    private String password;

//    @TableField(exist = false)
    private String url;

    @TableField(exist = false)
    private String operation; //操作行为 0-上，1-下，2-左，3-右，4-左上，5-左下，6-右上，7-右下，8-放大，9-缩小，10-停止

    @TableField(exist = false)
    private String duration; //移动持续时间，单位毫秒

    private String channel;

    @ApiModelProperty(value = "所属项目ID")
//    @TableField(updateStrategy = FieldStrategy.IGNORED )
    private Long pId;

    @TableField(exist = false)
    private String pAddress;

    @TableField(exist = false)
    private String pName;

    @ApiModelProperty(value = "设备状态")
    private Integer eqStatus;

    @ApiModelProperty(value = "备注")
//    @TableField(exist = false)
    private String note;

    @TableField(exist = false)
    private Long sid;

    @ApiModelProperty(value = "播放状态")
    private Integer liveStatus;

    @TableField(exist = false)
    private boolean offline;
}
