package com.lessing.equipment.modules.eq.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lessing.equipment.modules.eq.entity.EqEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface EqDao extends BaseMapper<EqEntity> {

    List<EqEntity> selectEqList(String pid, String eSn);

}
