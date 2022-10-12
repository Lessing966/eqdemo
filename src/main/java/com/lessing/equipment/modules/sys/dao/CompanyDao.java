package com.lessing.equipment.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lessing.equipment.modules.sys.entity.CompanyEntity;
import com.lessing.equipment.modules.sys.entity.DeptEntity;
import org.apache.ibatis.annotations.Param;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface CompanyDao extends BaseMapper<CompanyEntity> {
    List<CompanyEntity> selectListtwo();

    List<CompanyEntity> selectListtwoBygroup(Integer id);

    List<CompanyEntity> selectListtwos();

    DeptEntity selectid(@Param("did") Integer id);

}
