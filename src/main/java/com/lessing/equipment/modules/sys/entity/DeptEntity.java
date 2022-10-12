package com.lessing.equipment.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sys_dept")
public class  DeptEntity {
    private Integer id;
    private String name;
    private Integer companyId;
    private Integer status;

    @TableField(exist = false)
    private Integer did;

    @TableField(exist = false)
    private Integer comopanynoe;
    @TableField(exist = false)
    private Integer comopanytwo;
    @TableField(exist = false)
    private Integer gid;

}
