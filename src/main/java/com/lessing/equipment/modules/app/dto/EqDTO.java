package com.lessing.equipment.modules.app.dto;

import com.lessing.equipment.modules.eq.entity.EqEntity;
import lombok.Data;

import java.util.List;

@Data
public class EqDTO {
    private Integer id;
    private String name;
    private String address;
    private Integer status;
    private List<EqEntity> eqList;

}
