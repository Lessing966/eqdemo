package com.lessing.equipment.modules.eq.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lessing.equipment.common.utils.RedisUtils;
import com.lessing.equipment.common.utils.StringUtils;
import com.lessing.equipment.lib.RequestList;
import com.lessing.equipment.modules.app.dao.AlramListDTO;
import com.lessing.equipment.modules.app.dto.EqDTO;
import com.lessing.equipment.modules.eq.dao.EqAlrmDao;
import com.lessing.equipment.modules.eq.dao.EqDao;
import com.lessing.equipment.modules.eq.dao.EqScaleDao;
import com.lessing.equipment.modules.eq.dto.EqListDTO;
import com.lessing.equipment.modules.eq.entity.EqEntity;
import com.lessing.equipment.modules.eq.entity.EqScaleEntity;
import com.lessing.equipment.modules.eq.entity.EqalrmEnyity;
import com.lessing.equipment.modules.eq.service.EqService;
import com.lessing.equipment.modules.sys.dao.ProjectDao;
import com.lessing.equipment.modules.sys.dao.SysUserDao;
import com.lessing.equipment.modules.sys.entity.ProjectEntity;
import com.lessing.equipment.modules.sys.entity.UserEntity;
import com.lessing.equipment.sdk.PTZControlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class EqSerivceImpl implements EqService {

    @Autowired
    private EqDao eqDao;
    @Autowired
    private EqScaleDao eqScaleDao;
    @Autowired
    private ProjectDao projectDao;
    @Autowired
    private SysUserDao userDao;
    @Autowired
    private EqAlrmDao eqAlrmDao;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private RequestList requestList;

    @Value("${openapi.tpurl}")
    String Url;

    @Override
    public List<EqEntity> getEq() {
        List<EqEntity> eq_status = eqDao.selectList(new QueryWrapper<EqEntity>().eq("eq_status", 0));
        for(EqEntity eq:eq_status){
            ProjectEntity id = projectDao.selectOne(new QueryWrapper<ProjectEntity>().eq("id", eq.getPId()));
            if(!ObjectUtils.isEmpty(id)){
                eq.setPAddress(id.getAddress());
                eq.setPName(id.getName());
            }
        }
        return eq_status;
    }

    @Override
    public List<EqDTO> getEqList(String eSn) {
        List<EqDTO> eqDTOList =new ArrayList<>();
        List<ProjectEntity> projectEntities = projectDao.selectList(new QueryWrapper<ProjectEntity>()
                .eq("status",0));
        for(ProjectEntity po:projectEntities){
            EqDTO eqDTO=new EqDTO();
            eqDTO.setAddress(po.getAddress());
            eqDTO.setStatus(po.getStatus());
            eqDTO.setName(po.getName());
            eqDTO.setId(po.getId());
            List<EqEntity> eqEntities = eqDao.selectEqList(String.valueOf(eqDTO.getId()),eSn);
            for(EqEntity e:eqEntities){
                e.setPAddress(eqDTO.getAddress());
                e.setPName(eqDTO.getName());
                e.setPId(Long.valueOf(eqDTO.getId()));
//                boolean snap = PTZControlUtil.snap(e.getIp(), e.getUsername(), e.getPassword());
                boolean snap =false;
                if(snap){
                    String url = Url+e.getIp()+".jpg";
                    eqScaleDao.updateByIp(e.getIp(),url);
                }
            }
            eqDTO.setEqList(eqEntities);
            eqDTOList.add(eqDTO);
        }
        return eqDTOList;
    }


    @Override
    public Page<EqalrmEnyity> selectAlrmList(AlramListDTO alramListDTO) {
        Page<EqalrmEnyity> page = new Page<>(alramListDTO.getPageNumber(), alramListDTO.getPageSize());
        List<EqalrmEnyity> list = eqAlrmDao.selectAlrmList(page,alramListDTO);
         for(EqalrmEnyity eq:list){
            String format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(eq.getStartTime());
            String format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(eq.getStopTime());
            eq.setStartDate(format);
            eq.setStopDate(format1);
        }
        return page.setRecords(list);
    }

    @Override
    public List<EqScaleEntity> getEqScale(String location) {
        return eqScaleDao.selectByeqId(location);
    }

    @Override
    public EqEntity getEqByeqId(Integer eqId) {
        return eqDao.selectOne(new QueryWrapper<EqEntity>().eq("eq_id",eqId).eq("eq_status",0));
    }

    @Override
    public void insertScale(EqScaleEntity scaleEntity) {
        eqScaleDao.insert(scaleEntity);
    }

    @Override
    public int updateScale(EqScaleEntity scaleEntity) {
        return eqScaleDao.updateById(scaleEntity);
    }

    @Override
    public EqScaleEntity getScaleOn(Long id) {
        return eqScaleDao.selectById(id);
    }

    @Override
    public int updateEq(EqEntity eqEntity) {
        return eqDao.update(eqEntity,new QueryWrapper<EqEntity>().eq("eq_id",eqEntity.getEqId()));
    }

    @Override
    public EqScaleEntity selectScale(Long id) {
        return eqScaleDao.selectById(id);
    }

    @Override
    public Page<EqListDTO> selectEqList(EqListDTO entity) {
        Page<EqListDTO> page = new Page<>(entity.getPageNumber(), entity.getPageSize());
        List<EqListDTO> eqList = eqScaleDao.selectEqList(page,entity);
        return page.setRecords(eqList);
    }


    @Transactional
    @Override
    public boolean add(EqEntity eq) {
        eq.setChannel("0");
        String addeq = requestList.addeq(eq);
        if(addeq.equals("0")){
            redisUtils.set(eq.getIp(),eq);
            eqDao.insert(eq);
            return true;
        }else {
            return false;
        }
    }

    @Override
    public boolean update(EqEntity eqEntity) {
        if(eqEntity.getEqStatus() != 0){
            eqEntity.setPId(null);
        }
        int i = eqDao.update(eqEntity,new QueryWrapper<EqEntity>().eq("eq_id",eqEntity.getEqId()));
        if(i > 0){
            return true;
        }
        return false;
    }

    @Override
    public void delete(Integer id) {
        int eq_id = eqDao.delete(new QueryWrapper<EqEntity>().eq("eq_id", id));
        if(eq_id > 0){
            EqEntity eq = eqDao.selectOne(new QueryWrapper<EqEntity>().eq("eq_id", id));
            redisUtils.delete(eq.getIp());
            if(!ObjectUtils.isEmpty(eq)){
                requestList.unBindDevice(eq);
            }
        }
    }

    @Override
    public List<EqEntity> getProjList(String uid) {
        List<EqEntity> eqEntities=new ArrayList<>();
        List<ProjectEntity> poj = projectDao.selectList(new QueryWrapper<>());
        for(ProjectEntity c : poj){
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
                for(int i = 0 ; i <strings1.length ; i++ ){
                    if(strings1[i].equals(uid)){
                        List<EqEntity> eqEntities1 = eqDao.selectList(new QueryWrapper<EqEntity>()
                                .eq("p_id", c.getId()).eq("eq_status", 0));
                        for(EqEntity eq:eqEntities1){
                            eq.setPAddress(c.getAddress());
                            eq.setPName(c.getName());
                            eqEntities.add(eq);
                        }
                    }
                }
            }
        }
        return eqEntities;
    }

    @Override
    public List<EqDTO> getProjEqList(String eSn,String uid) {
        List<EqDTO> eqEntities=new ArrayList<>();
        List<ProjectEntity> poj = projectDao.selectList(new QueryWrapper<>());
        for(ProjectEntity c : poj){
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
                for(int i = 0 ; i <strings1.length ; i++ ){
                    if(strings1[i].equals(uid)){
                        EqDTO eq =new EqDTO();
                        eq.setId(c.getId());
                        eq.setAddress(c.getAddress());
                        eq.setStatus(c.getStatus());
                        eq.setName(c.getName());
                        List<EqEntity> eqEntities1 = eqDao.selectEqList(String.valueOf(c.getId()),eSn);
                        for(EqEntity e:eqEntities1){
                            e.setPName(eq.getName());
                            e.setPId(Long.valueOf(eq.getId()));
                            e.setPAddress(eq.getAddress());
//                            boolean snap = PTZControlUtil.snap(e.getIp(), e.getUsername(), e.getPassword());
                            boolean snap =false;
                            if(snap){
                                String url = Url+e.getIp()+".jpg";
                                eqScaleDao.updateByIp(e.getIp(),url);
                            }
                        }
                        eq.setEqList(eqEntities1);
                        eqEntities.add(eq);
                    }

                }
            }
        }
        return eqEntities;
    }

}
