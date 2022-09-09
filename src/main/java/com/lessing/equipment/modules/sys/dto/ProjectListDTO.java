package com.lessing.equipment.modules.sys.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ProjectListDTO {
    @ApiModelProperty(value = "项目ID")
    private Integer id;
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "地址")
    private String address;
    private Integer gid;
    @ApiModelProperty(value = "所属集团")
    private String gname;
    private Integer oneid;
    @ApiModelProperty(value = "一级公司")
    private String onename;
    private Integer twoid;
    @ApiModelProperty(value = "二级公司")
    private String twoname;
    private Integer did;
    @ApiModelProperty(value = "部门")
    private String dname;
    @ApiModelProperty(value = "项目经理")
    private String head;
    @ApiModelProperty(value = "安全员")
    private String hon;

    private String username;

    @ApiModelProperty(value = "项目人员")
    private List<String> userList;
    @ApiModelProperty(value = "项目状态")
    private Integer status;
    @ApiModelProperty(value = "项目人数")
    private Integer num;

    private Integer PageNumber;
    private Integer PageSize;
}
