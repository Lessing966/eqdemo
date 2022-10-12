package com.lessing.equipment.modules.sys.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "添加组织DTO")
public class  ArchitecDTO {
    private Integer gid;
    @ApiModelProperty(value = "集团")
    private String gName;

    private Integer companyoneId;
    @ApiModelProperty(value = "一级公司")
    private String cnameOne;

    private Integer companytwoId;
    @ApiModelProperty(value = "二级公司")
    private String cnameTwo;

    private Integer did;
    @ApiModelProperty(value = "部门")
    private String deptName;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "页号")
    private int pageNumber;

    @ApiModelProperty(value = "页面条数")
    private int pageSize;

    //勾选
    private Boolean isSelect;
}
