package com.lessing.equipment.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lessing.equipment.modules.app.dao.AlramListDTO;
import com.lessing.equipment.modules.sys.dto.PojNameListDTO;
import com.lessing.equipment.modules.sys.dto.ProjectListDTO;
import com.lessing.equipment.modules.sys.entity.ProjectEntity;
import org.apache.ibatis.annotations.Param;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface ProjectDao extends BaseMapper<ProjectEntity> {

    List<ProjectListDTO> selectPojList(Page<ProjectListDTO> page, ProjectListDTO project);

    ProjectListDTO selectPojOne(Integer id);

    List<PojNameListDTO> selectPojNameList();

    List<ProjectEntity> selectby(@Param("gid") Integer gid,@Param("one") Integer noe,@Param("two") Integer two,@Param("did") Integer did);

}
