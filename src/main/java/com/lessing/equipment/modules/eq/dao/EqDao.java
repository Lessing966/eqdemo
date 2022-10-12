package com.lessing.equipment.modules.eq.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lessing.equipment.modules.eq.entity.EqEntity;
import org.apache.ibatis.annotations.Param;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface EqDao extends BaseMapper<EqEntity> {

    List<EqEntity> selectEqList(String pid, String eSn);

    List<EqEntity> selectEqByRole(@Param("gid") Integer gid,@Param("one") Integer one,@Param("two") Integer two,@Param("did") Integer did);

}
