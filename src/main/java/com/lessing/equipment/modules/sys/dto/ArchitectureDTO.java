package com.lessing.equipment.modules.sys.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.LinkedHashSet;
import java.util.List;

@Data
@ApiModel(description = "组织架构DTO")
public class ArchitectureDTO {
    @ApiModelProperty(value = "集团id")
    private Integer gId;

    //和GID同值 只做展示用到
    private Integer id;

    @ApiModelProperty(value = "集团名称")
    private String Name;
    @ApiModelProperty(value = "集团状态")
    private Integer Status;

    @ApiModelProperty(value = "一级公司集合")
    private LinkedHashSet<ComopanyNoe> cpList;

}
