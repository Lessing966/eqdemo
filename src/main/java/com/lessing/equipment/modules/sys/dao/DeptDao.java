package com.lessing.equipment.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lessing.equipment.modules.sys.dto.ArchitecDTO;
import com.lessing.equipment.modules.sys.dto.ArchitectureDTO;
import com.lessing.equipment.modules.sys.entity.DeptEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface DeptDao extends BaseMapper<DeptEntity> {


}
