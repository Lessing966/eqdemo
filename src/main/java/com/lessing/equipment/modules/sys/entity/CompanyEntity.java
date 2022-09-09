package com.lessing.equipment.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.LinkedHashSet;
import java.util.List;


@Data
@TableName("sys_company")
public class CompanyEntity {
    private Integer id;
    private String name;
    private Integer groupId;
    private Integer companyId;
    private Integer status;

    @TableField(exist = false)
    private Integer comopanytwo;

    @TableField(exist = false)
    private LinkedHashSet<DeptEntity> cpList;
}
