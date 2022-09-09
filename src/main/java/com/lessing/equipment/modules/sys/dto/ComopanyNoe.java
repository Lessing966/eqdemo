package com.lessing.equipment.modules.sys.dto;

import com.lessing.equipment.modules.sys.entity.CompanyEntity;
import lombok.Data;

import java.util.LinkedHashSet;
import java.util.List;

@Data
public class ComopanyNoe {
    private Integer id;
    private String name;
    private Integer status;
    private Integer groupId;
    private Integer comopanynoe;

    LinkedHashSet<CompanyEntity> cpList;
}
