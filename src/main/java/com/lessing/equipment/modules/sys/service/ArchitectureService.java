package com.lessing.equipment.modules.sys.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lessing.equipment.modules.eq.dto.EqListDTO;
import com.lessing.equipment.modules.sys.dto.ArchitecDTO;
import com.lessing.equipment.modules.sys.dto.ArchitectureDTO;
import com.lessing.equipment.modules.sys.dto.ComopanyNoe;
import com.lessing.equipment.modules.sys.dto.DeleteDTO;
import com.lessing.equipment.modules.sys.entity.CompanyEntity;
import com.lessing.equipment.modules.sys.entity.DeptEntity;
import com.lessing.equipment.modules.sys.entity.GroupEntity;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public interface ArchitectureService {
    Set<ArchitectureDTO> selectGroupList1(ArchitecDTO architec);

    List<GroupEntity> getGroupList();

    boolean add(ArchitecDTO architecDTO);

//    List<ArchitectureDTO> selectAllListByGid(Integer gid);

    boolean update(ArchitecDTO dto);

    boolean delete(DeleteDTO gid);

    List<GroupEntity> getGroupOneList();

    List<ComopanyNoe> getComBiGid(Integer gid);

    List<CompanyEntity> getComTwo(Integer oneid);

    List<DeptEntity> getDeptList(Integer twoid);

    List<ComopanyNoe> getComBiGidAll();

    List<CompanyEntity> getComTwoAll();

    List<DeptEntity> getDeptListAll();

    List<GroupEntity> getGroupOneByRole(Integer groupId);

    List<ComopanyNoe> getComByRole(Integer companyoneId);

}
