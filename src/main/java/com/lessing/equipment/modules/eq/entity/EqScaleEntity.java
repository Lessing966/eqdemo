package com.lessing.equipment.modules.eq.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("eq_scale")
public class EqScaleEntity {
    private Long id;
    @ApiModelProperty(value = "序号")
    private Integer num;
    @ApiModelProperty(value = "分屏参数")
    private Integer location;

    @ApiModelProperty(value = "设备ID")
    @TableField(updateStrategy = FieldStrategy.IGNORED )
    private Integer eqId;


}
