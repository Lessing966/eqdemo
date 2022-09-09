package com.lessing.equipment.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lessing.equipment.modules.sys.dto.ArchitecDTO;
import com.lessing.equipment.modules.sys.dto.CameraDTO;
import com.lessing.equipment.modules.sys.dto.UserListDTO;
import com.lessing.equipment.modules.sys.dto.UserNameDTO;
import com.lessing.equipment.modules.sys.entity.UserEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface SysUserDao extends BaseMapper<UserEntity> {
    List<UserListDTO> selectUserList(Page<UserListDTO> page,UserListDTO user);

    UserListDTO selectUserOne(Integer uid);

    List<UserNameDTO> selectUser(Integer did);

    List<CameraDTO> selectEq();

    List<UserNameDTO> selectByArchitrue(ArchitecDTO architecDTO);


    int updateByMapper(UserEntity user);

}
