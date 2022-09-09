package com.lessing.equipment.modules.sys.service.Impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lessing.equipment.common.utils.StringUtils;
import com.lessing.equipment.modules.sys.dao.ProjectDao;
import com.lessing.equipment.modules.sys.dao.SysUserDao;
import com.lessing.equipment.modules.sys.dto.PojNameListDTO;
import com.lessing.equipment.modules.sys.dto.ProjectListDTO;
import com.lessing.equipment.modules.sys.entity.ProjectEntity;
import com.lessing.equipment.modules.sys.entity.UserEntity;
import com.lessing.equipment.modules.sys.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectDao projectDao;
    @Autowired
    private SysUserDao userDao;

    @Override
    public Page<ProjectListDTO> selectPojList(ProjectListDTO project) {
        Page<ProjectListDTO> page = new Page<>(project.getPageNumber(), project.getPageSize());
        List<ProjectListDTO> projectList = projectDao.selectPojList(page,project);
        if(null !=projectList && projectList.size()>0){
            for(ProjectListDTO poj:projectList){
                List<String> list =new ArrayList<>();
                if(!StringUtils.isEmpty(poj.getUsername())){
                    String substring = poj.getUsername().substring(1, poj.getUsername().length() - 1);
                    String[] split = substring.split(",");
                    for (int i = 0 ; i <split.length ; i++ ) {
                        UserEntity userEntity = userDao.selectById(split[i]);
                        if(null != userEntity){
                            list.add(userEntity.getUserName());
                        }
                        poj.setUserList(list);
                    }
                }
            }
        }
        return page.setRecords(projectList);
    }

    @Override
    public boolean addPoj(ProjectEntity project) {
        int insert = projectDao.insert(project);
        if(insert>0){
            return true;
        }
        return false;
    }

    @Override
    public boolean update(ProjectEntity project) {
        int i = projectDao.updateById(project);
        if(i >0){
            return true;
        }
        return false;
    }

    @Override
    public ProjectListDTO selectPojOne(Integer id) {
        ProjectListDTO poj = projectDao.selectPojOne(id);
//        if(null != poj && !StringUtils.isEmpty(poj.getUsername())){
//            List<String> list = new ArrayList<>();
//            String[] split = poj.getUsername().split(",");
//            for (int i = 0 ; i <split.length ; i++ ) {
//                UserEntity userEntity = userDao.selectById(split[i]);
//                list.add(userEntity.getUserName());
//                poj.setUserList(list);
//            }
//        }
        return poj;
    }

    @Override
    public boolean delete(Integer id) {
        if(projectDao.deleteById(id) >0){
            return true;
        }
        return false;
    }

    @Override
    public List<PojNameListDTO> selectpojname() {
        return projectDao.selectPojNameList();
    }
}
