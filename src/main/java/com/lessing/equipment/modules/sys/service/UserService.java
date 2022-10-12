package com.lessing.equipment.modules.sys.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lessing.equipment.modules.sys.dto.*;
import com.lessing.equipment.modules.sys.entity.UserEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


public interface UserService {

    UserEntity getUserBase(String phone);

    Page<UserListDTO> getUserList(UserListDTO user);

    boolean addUser(UserEntity user);

    boolean update(UserEntity user);

    UserListDTO selectUserOne(Integer uid);

    boolean delete(Integer uid);

    List<UserNameDTO> selectUserList(ArchitecDTO architecDTO);


    List<CameraDTO> selectEq(String uid);

    UserEntity selectUserOneByUid(String userid);

    UserEntity selectUserRole(String uid, Integer role);

    boolean updatePassword(UserEntity user);

    boolean save(UserEntity user);

    UserEntity selectUser(String phone);

    UserEntity selectcode(String code);


}
