package com.lessing.equipment.modules.eq.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lessing.equipment.modules.app.dao.AlramListDTO;
import com.lessing.equipment.modules.app.dto.EqDTO;
import com.lessing.equipment.modules.eq.dto.EqListDTO;
import com.lessing.equipment.modules.eq.entity.EqEntity;
import com.lessing.equipment.modules.eq.entity.EqScaleEntity;
import com.lessing.equipment.modules.eq.entity.EqalrmEnyity;
import com.lessing.equipment.modules.sys.dto.CameraDTO;
import com.lessing.equipment.modules.sys.dto.PojNameListDTO;
import io.swagger.models.auth.In;

import java.util.List;

public interface EqService {

    List<EqEntity> getEq();

    List<EqScaleEntity> getEqScale(String location);

    EqEntity getEqByeqId(Integer eqId);

    void insertScale(EqScaleEntity scaleEntity);

    int updateScale(EqScaleEntity scaleEntity);

    EqScaleEntity getScaleOn(Long id);

    int updateEq(EqEntity eqEntity);

    EqScaleEntity selectScale(Long id);

    Page<EqListDTO> selectEqList(EqListDTO entity);

    boolean add(EqEntity eq);

    boolean update(EqEntity eqEntity);

    void delete(Integer id);

    List<EqEntity> getProjList(String uid);

    List<EqDTO> getProjEqList(String eSn,String uid);

    List<EqDTO> getEqList(String eSn);

    Page<EqalrmEnyity> selectAlrmList(AlramListDTO alramListDTO);

}
