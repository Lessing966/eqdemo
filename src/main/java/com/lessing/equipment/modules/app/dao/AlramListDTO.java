package com.lessing.equipment.modules.app.dao;

import lombok.Data;

@Data
public class AlramListDTO {
    private String eSn;
    private String startTime;

    private Integer PageNumber;
    private Integer PageSize;
}
