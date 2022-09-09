package com.lessing.equipment.modules.sys.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lessing.equipment.modules.sys.dto.PojNameListDTO;
import com.lessing.equipment.modules.sys.dto.ProjectListDTO;
import com.lessing.equipment.modules.sys.entity.ProjectEntity;

import java.util.List;

public interface ProjectService {
    Page<ProjectListDTO> selectPojList(ProjectListDTO project);

    boolean addPoj(ProjectEntity project);

    boolean update(ProjectEntity project);

    ProjectListDTO selectPojOne(Integer id);

    boolean delete(Integer id);

    List<PojNameListDTO> selectpojname();

}
