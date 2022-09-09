package com.lessing.equipment.modules.eq.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lessing.equipment.modules.eq.dto.EqListDTO;
import com.lessing.equipment.modules.eq.entity.EqEntity;
import com.lessing.equipment.modules.eq.entity.EqScaleEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface EqScaleDao extends BaseMapper<EqScaleEntity> {

    List<EqScaleEntity> selectByeqId(String location);

    List<EqListDTO> selectEqList(Page<EqListDTO> page, EqListDTO entity);

    void updateByIp(String ip,String url);

    EqEntity selectgetEq(String ip);

}
