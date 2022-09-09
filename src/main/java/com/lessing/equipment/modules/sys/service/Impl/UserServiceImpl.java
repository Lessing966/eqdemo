package com.lessing.equipment.modules.sys.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lessing.equipment.common.utils.JwtUtils;
import com.lessing.equipment.common.utils.MD5Utils;
import com.lessing.equipment.common.utils.StringUtils;
import com.lessing.equipment.modules.sys.dao.SysUserDao;
import com.lessing.equipment.modules.sys.dto.*;
import com.lessing.equipment.modules.sys.entity.UserEntity;
import com.lessing.equipment.modules.sys.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private SysUserDao userDao;

    @Override
    public UserEntity getUserBase(String phone) {
        return userDao.selectOne(new QueryWrapper<UserEntity>()
                .eq("login_name",phone).eq("status",0));
    }

    @Override
    public Page<UserListDTO> getUserList(UserListDTO user) {
        Page<UserListDTO> page = new Page<>(user.getPageNumber(), user.getPageSize());
        List<UserListDTO> list = userDao.selectUserList(page,user);
        return page.setRecords(list);
    }

    @Override
    public boolean addUser(UserEntity user) {
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        user.setPassword(MD5Utils.MD5Encode(user.getCode()));
        user.setLoginName(user.getPhone());
        int insert = userDao.insert(user);
        if(insert >0){
            return true;
        }
        return false;
    }

    @Override
    public boolean update(UserEntity user) {
        user.setUpdateTime(new Date()); 
        user.setPassword(MD5Utils.MD5Encode(user.getPassword()));
        int i = userDao.updateById(user);
        if(i>0){
            return true;
        }
        return false;
    }

    @Override
    public UserListDTO selectUserOne(Integer uid) {
        return userDao.selectUserOne(uid);
    }

    @Override
    public boolean delete(Integer uid) {
        int i = userDao.deleteById(uid);
        if(i >0){
            return true;
        }
        return false;
    }

    @Override
    public List<UserNameDTO> selectUserList(ArchitecDTO architecDTO) {
        return userDao.selectByArchitrue(architecDTO);
    }

    @Override
    public List<CameraDTO> selectEq(String uid) {
        List<CameraDTO> eqlist =new ArrayList<>();
        List<CameraDTO> list = userDao.selectEq();
        for(CameraDTO c : list){
            Set<String> newList = new HashSet<>();
            if(!StringUtils.isEmpty(c.getUsername())){
                String substring = c.getUsername().substring(1, c.getUsername().length() - 1);
                String[] split = substring.split(",");
                List<String> list1 = Arrays.asList(split);
                newList.addAll(list1);
                UserEntity u = userDao.selectOne(new QueryWrapper<UserEntity>()
                        .eq("user_name",c.getHead()));
                UserEntity u1 = userDao.selectOne(new QueryWrapper<UserEntity>()
                        .eq("user_name",c.getHon()));
                if(null != u){
                    newList.add(String.valueOf(u.getId()));
                }
                if(null != u1){
                    newList.add(String.valueOf(u1.getId()));
                }
                String[] strings1 = newList.toArray(new String[]{});
                for (int i = 0 ; i <strings1.length ; i++ ) {
                    if(strings1[i].equals(uid)){
                        CameraDTO eq = new CameraDTO();
                        eq.setId(c.getId());
                        eq.setSn(c.getSn());
                        eq.setName(c.getName());
                        eq.setAddress(c.getAddress());
                        eqlist.add(eq);
                    }
                }
            }
            }
        return eqlist;
    }

    @Override
    public UserEntity selectUserOneByUid(String userid) {
        return userDao.selectOne(new QueryWrapper<UserEntity>()
                .eq("id",userid).eq("status",0));
    }

    @Override
    public UserEntity selectUserRole(String uid, Integer role) {
        return userDao.selectOne(new QueryWrapper<UserEntity>()
                .eq("id",uid).eq("role",role).eq("status",0));
    }

    @Override
    public boolean updatePassword(UserEntity user) {
        user.setPassword(MD5Utils.MD5Encode(user.getPassword()));
        user.setNum(2);
        user.setUpdateTime(new Date());
        int i = userDao.updateByMapper(user);
        if(i>0){
            return true;
        }
        return false;
    }

    @Override
    public boolean save(UserEntity user) {
        user.setUpdateTime(new Date());
        int i = userDao.updateByMapper(user);
        if(i>0){
            return true;
        }
        return false;
    }
}