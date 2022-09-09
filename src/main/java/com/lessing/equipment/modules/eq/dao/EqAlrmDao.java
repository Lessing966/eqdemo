package com.lessing.equipment.modules.eq.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lessing.equipment.modules.app.dao.AlramListDTO;
import com.lessing.equipment.modules.eq.entity.EqalrmEnyity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface EqAlrmDao extends BaseMapper<EqalrmEnyity> {

    EqalrmEnyity selectEq();

    List<EqalrmEnyity> selectAlrmList(Page<EqalrmEnyity> page, AlramListDTO alram);

}
