package com.lessing.equipment.modules.sys.service.Impl;


import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lessing.equipment.common.utils.StringUtils;
import com.lessing.equipment.modules.eq.dto.EqListDTO;
import com.lessing.equipment.modules.sys.dao.ArchitectureDao;
import com.lessing.equipment.modules.sys.dao.CompanyDao;
import com.lessing.equipment.modules.sys.dao.DeptDao;
import com.lessing.equipment.modules.sys.dao.GroupDao;
import com.lessing.equipment.modules.sys.dto.ArchitecDTO;
import com.lessing.equipment.modules.sys.dto.ArchitectureDTO;
import com.lessing.equipment.modules.sys.dto.ComopanyNoe;
import com.lessing.equipment.modules.sys.dto.DeleteDTO;
import com.lessing.equipment.modules.sys.entity.CompanyEntity;
import com.lessing.equipment.modules.sys.entity.DeptEntity;
import com.lessing.equipment.modules.sys.entity.GroupEntity;
import com.lessing.equipment.modules.sys.service.ArchitectureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
public class ArchitectureServiceImpl implements ArchitectureService {

    @Autowired
    private ArchitectureDao architectureDao;
    @Autowired
    private CompanyDao companyDao;
    @Autowired
    private GroupDao groupDao;
    @Autowired
    private DeptDao deptDao;

//    @Override
//    public Page<ArchitectureDTO> selectGroupList(ArchitecDTO architec) {
//        Page<ArchitectureDTO> page = new Page<>(architec.getPageNumber(), architec.getPageSize());
//        List<ArchitectureDTO> dtoList = new ArrayList<>();
//        List<GroupEntity> list = groupDao.selectArchitecList(page,architec);
//        for(GroupEntity group: list){
                //集团信息
//                ArchitectureDTO dto =new ArchitectureDTO();
//                dto.setGId(group.getId());
//                dto.setId(group.getId());
//                dto.setStatus(group.getStatus());
//                dto.setName(group.getName());
//                List<ComopanyNoe> noeList = new ArrayList<>();
//            List<CompanyEntity> comlist = companyDao.selectList(new QueryWrapper<CompanyEntity>()
//                            .eq("company_id",0).eq("status",0));
//                for(CompanyEntity com : comlist){
//                        ComopanyNoe noe = new ComopanyNoe();
//                        noe.setName(com.getName());
//                        noe.setStatus(com.getStatus());
//                        noe.setId(com.getId());
//                        noe.setComopanynoe(com.getId());
//                    List<CompanyEntity> companyTwo = companyDao.selectList(new QueryWrapper<CompanyEntity>()
//                                .eq("status", 0));
//                        for(CompanyEntity tow : companyTwo){
//                            List<DeptEntity> deptEntities = deptDao.selectList(new QueryWrapper<DeptEntity>()
//                                        .eq("status", 0));
//                            for(DeptEntity d :deptEntities){
//                                d.setDid(d.getId());
//                            }
//                            tow.setCpList(deptEntities);
//                            tow.setComopanytwo(tow.getId());
//                        }
//                        noe.setCpList(companyTwo);
//                        noeList.add(noe);
//                        dto.setCpList(noeList);
//                    }
//                    dtoList.add(dto);
//        }
//        return page.setRecords(dtoList);
//    }


    @Override
    public Set<ArchitectureDTO> selectGroupList1(ArchitecDTO architec) {
        Page<ArchitectureDTO> page = new Page<>(architec.getPageNumber(), architec.getPageSize());
        Set<ArchitectureDTO> dtoList = new LinkedHashSet<>();
        List<CompanyEntity> comlist = companyDao.selectList(new QueryWrapper<CompanyEntity>()
                .eq("company_id",0).eq("status",0));
        for(CompanyEntity noe:comlist){
            List<GroupEntity> list = groupDao.selectArchitecList(page,architec);
            for(GroupEntity group:list){
                LinkedHashSet<ComopanyNoe> noeList = new LinkedHashSet<>();
                LinkedHashSet<CompanyEntity> twolist =new LinkedHashSet<>();
                ComopanyNoe noe1 = new ComopanyNoe();
                if(noe.getGroupId() != null && group.getId() == noe.getGroupId()) {
                    //集团信息
                    ArchitectureDTO dto = new ArchitectureDTO();
                    dto.setGId(group.getId());
                    dto.setId(group.getId());
                    dto.setStatus(group.getStatus());
                    dto.setName(group.getName());
                    noe1.setComopanynoe(noe.getId());
                    noe1.setName(noe.getName());
                    noe1.setId(noe.getId());
                    noe1.setStatus(noe.getStatus());
                    noeList.add(noe1);
                    dto.setCpList(noeList);
                    dtoList.add(dto);
                }else if(noe.getGroupId() == null && group.getId() != noe.getGroupId()){
                    ArchitectureDTO dto1 =new ArchitectureDTO();
                    CompanyEntity entity =new CompanyEntity();
                    ComopanyNoe noe2 = new ComopanyNoe();
                    noe2.setComopanynoe(noe.getId());
                    noe2.setName(noe.getName());
                    noe2.setId(noe.getId());
                    noe2.setStatus(noe.getStatus());
                    List<CompanyEntity> two = companyDao.selectListtwo();
                    for(CompanyEntity t:two){
                        if(t.getCompanyId() != null && noe2.getId() == t.getCompanyId()){
                            entity.setId(t.getId());
                            entity.setComopanytwo(t.getId());
                            entity.setName(t.getName());
                            entity.setStatus(t.getStatus());
                            twolist.add(entity);
                            noe2.setCpList(twolist);
                        }
                    }
                    noeList.add(noe2);
                    dto1.setCpList(noeList);
                    dtoList.add(dto1);
                }else {
                    List<CompanyEntity> list1 = companyDao.selectList(new QueryWrapper<CompanyEntity>()
                            .eq("group_id",group.getId()).eq("company_id",0).eq("status",0));
                   if(list1.size() == 0){
                       ArchitectureDTO dto2 =new ArchitectureDTO();
                       dto2.setGId(group.getId());
                       dto2.setId(group.getId());
                       dto2.setStatus(group.getStatus());
                       dto2.setName(group.getName());
                       dtoList.add(dto2);
                   }
                }
                //二级公司
                List<CompanyEntity> comtwo = companyDao.selectListtwo();
                for(CompanyEntity two:comtwo){
                    LinkedHashSet<ComopanyNoe> noeList2 = new LinkedHashSet<>();
                    LinkedHashSet<CompanyEntity> twolist2 =new LinkedHashSet<>();
                    LinkedHashSet<DeptEntity> deptlist =new LinkedHashSet<>();
                    CompanyEntity entity =new CompanyEntity();
                    if(two.getCompanyId() != null && noe.getId() == two.getCompanyId()){
                        entity.setId(two.getId());
                        entity.setComopanytwo(two.getId());
                        entity.setCompanyId(noe.getId());
                        entity.setName(two.getName());
                        entity.setStatus(two.getStatus());
                        twolist.add(entity);
                        noe1.setCpList(twolist);
                    }else if(two.getCompanyId() == null && noe.getId() != two.getCompanyId()){
                        //集团信息
                        ArchitectureDTO dto =new ArchitectureDTO();
                        ComopanyNoe noe2 = new ComopanyNoe();
                        entity.setId(two.getId());
                        entity.setComopanytwo(two.getId());
                        entity.setName(two.getName());
                        entity.setStatus(two.getStatus());
                        twolist2.add(entity);
                        noe2.setCpList(twolist2);
                        noeList2.add(noe2);
                        dto.setCpList(noeList2);
                        dtoList.add(dto);
                    }
                    //部门
                    List<DeptEntity> dept = deptDao.selectList(new QueryWrapper<DeptEntity>()
                            .eq("status", 0));
                    for(DeptEntity dep:dept){
                        LinkedHashSet<ComopanyNoe> noeLis = new LinkedHashSet<>();
                        LinkedHashSet<CompanyEntity> twolis =new LinkedHashSet<>();
                        LinkedHashSet<DeptEntity> deptlis =new LinkedHashSet<>();
                        if(dep.getCompanyId() != null && two.getId() == dep.getCompanyId()){
                            DeptEntity d=new DeptEntity();
                            d.setDid(dep.getId());
                            d.setName(dep.getName());
                            d.setStatus(dep.getStatus());
                            d.setCompanyId(two.getId());
                            d.setId(dep.getId());
                            deptlist.add(d);
                            entity.setCpList(deptlist);
                        }else if(dep.getCompanyId() == null && two.getId() != dep.getCompanyId()){
                            ArchitectureDTO dto =new ArchitectureDTO();
                            ComopanyNoe noe2 = new ComopanyNoe();
                            CompanyEntity com =new CompanyEntity();
                            DeptEntity d=new DeptEntity();
                            d.setDid(dep.getId());
                            d.setName(dep.getName());
                            d.setStatus(dep.getStatus());
                            d.setCompanyId(two.getId());
                            d.setId(dep.getId());
                            deptlis.add(d);
                            com.setCpList(deptlis);
                            twolis.add(com);
                            noe2.setCpList(twolis);
                            noeLis.add(noe2);
                            dto.setCpList(noeLis);
                            dtoList.add(dto);
                        }
                    }
                }
            }
        }
        return dtoList;
    }

    @Override
    public List<GroupEntity> getGroupList() {
        return groupDao.selectList(new QueryWrapper<>());
    }


    @Override
    @Transactional
    public boolean add(ArchitecDTO architecDTO) {
        GroupEntity group =new GroupEntity();
        CompanyEntity company = new CompanyEntity();
        DeptEntity dept =new DeptEntity();
        try {
            if(architecDTO.getGid() !=null
                    || architecDTO.getDid() !=null
                    ||architecDTO.getCompanytwoId()!=null
                    || architecDTO.getCompanyoneId()!=null)
            {
                if(!StringUtils.isEmpty(architecDTO.getGName())){
                    //先查看集团是否存在
                    GroupEntity groupEntity = groupDao.selectOne(new QueryWrapper<GroupEntity>()
                            .eq("name", architecDTO.getGName()).eq("status",0));
                    if(null == groupEntity){
                        //如果集团为空 就进行新增 如果不为空 不会进行新增
                        group.setName(architecDTO.getGName());
                        group.setStatus(architecDTO.getStatus());
                        groupDao.insert(group);
                    }else{
                        //如果不为空 就查询传来的集团信息
                        group = groupDao.selectOne(new QueryWrapper<GroupEntity>()
                                .eq("name", architecDTO.getGName()).eq("status",0));
                    }
                }
                //判断一级公司是否存在
                if(!StringUtils.isEmpty(architecDTO.getCnameOne())){
                    CompanyEntity companyEntity = companyDao.selectOne(new QueryWrapper<CompanyEntity>().eq("name",architecDTO.getCnameOne())
                            .eq("company_id",0).eq("status",0));
                    if(null == companyEntity) {
                        company.setName(architecDTO.getCnameOne());
                        company.setGroupId(group.getId());
                        company.setCompanyId(0);
                        company.setStatus(architecDTO.getStatus());
                        companyDao.insert(company);
                    }else {
                        companyEntity.setGroupId(group.getId());
                        companyEntity.setId(architecDTO.getCompanyoneId());
                        companyEntity.setStatus(architecDTO.getStatus());
                        companyDao.updateById(companyEntity);
                        company =companyDao.selectOne(new QueryWrapper<CompanyEntity>().eq("name",architecDTO.getCnameOne())
                                .eq("company_id",0).eq("status",0));
                    }
                }
                //二级公司
                if(!StringUtils.isEmpty(architecDTO.getCnameTwo())){
                    CompanyEntity compan = companyDao.selectOne(new QueryWrapper<CompanyEntity>()
                            .eq("name",architecDTO.getCnameTwo()).eq("status",0));
                    if(null == compan) {
                        company.setName(architecDTO.getCnameTwo());
                        company.setGroupId(group.getId());
                        company.setCompanyId(company.getId());
                        company.setStatus(architecDTO.getStatus());
                        companyDao.insert(company);
                    }else {
                        compan.setGroupId(group.getId());
                        compan.setCompanyId(company.getId());
                        compan.setId(architecDTO.getCompanytwoId());
                        compan.setStatus(architecDTO.getStatus());
                        companyDao.updateById(compan);
                        company =companyDao.selectOne(new QueryWrapper<CompanyEntity>()
                                .eq("name",architecDTO.getCnameTwo()).eq("status",0));
                    }
                }
                //部门校验
                if(!StringUtils.isEmpty(architecDTO.getDeptName())){
                    DeptEntity deptEntity = deptDao.selectOne(new QueryWrapper<DeptEntity>()
                            .eq("name", architecDTO.getDeptName()).eq("status", 0));
                    if(null == deptEntity){
                        dept.setCompanyId(company.getId());
                        dept.setName(architecDTO.getDeptName());
                        dept.setStatus(architecDTO.getStatus());
                        deptDao.insert(dept);
                    }else {
                        deptEntity.setCompanyId(company.getId());
                        deptEntity.setId(architecDTO.getDid());
                        deptEntity.setStatus(architecDTO.getStatus());
                        deptDao.updateById(deptEntity);
                    }
                }
            }else {
                if(!StringUtils.isEmpty(architecDTO.getGName())){
                    //先查看集团是否存在
                    GroupEntity groupEntity = groupDao.selectOne(new QueryWrapper<GroupEntity>()
                            .eq("name", architecDTO.getGName()).eq("status",0));
                    if(null == groupEntity){
                        //如果集团为空 就进行新增 如果不为空 不会进行新增
                        group.setName(architecDTO.getGName());
                        group.setStatus(architecDTO.getStatus());
                        groupDao.insert(group);
                    }else{
                        //如果不为空 就查询传来的集团信息
                        group = groupDao.selectOne(new QueryWrapper<GroupEntity>()
                                .eq("name", architecDTO.getGName()).eq("status",0));
                    }
                }
                //判断一级公司是否存在
                if(!StringUtils.isEmpty(architecDTO.getCnameOne())){
                    CompanyEntity companyEntity = companyDao.selectOne(new QueryWrapper<CompanyEntity>().eq("name",architecDTO.getCnameOne())
                            .eq("company_id",0).eq("status",0));
                    if(null == companyEntity) {
                        company.setName(architecDTO.getCnameOne());
                        company.setGroupId(group.getId());
                        company.setCompanyId(0);
                        company.setStatus(architecDTO.getStatus());
                        companyDao.insert(company);
                    }else {
                        company =companyDao.selectOne(new QueryWrapper<CompanyEntity>().eq("name",architecDTO.getCnameOne())
                                .eq("company_id",0).eq("status",0));
                    }
                }
                //二级公司
                if(!StringUtils.isEmpty(architecDTO.getCnameTwo())){
                    CompanyEntity compan = companyDao.selectOne(new QueryWrapper<CompanyEntity>()
                            .eq("name",architecDTO.getCnameTwo()).eq("status",0));
                    if(null == compan) {
                        company.setName(architecDTO.getCnameTwo());
                        company.setGroupId(group.getId());
                        company.setCompanyId(company.getId());
                        company.setStatus(architecDTO.getStatus());
                        companyDao.insert(company);
                    }else {
                        company =companyDao.selectOne(new QueryWrapper<CompanyEntity>()
                                .eq("name",architecDTO.getCnameTwo()).eq("status",0));
                    }
                }
                //部门校验
                if(!StringUtils.isEmpty(architecDTO.getDeptName())){
                    DeptEntity deptEntity = deptDao.selectOne(new QueryWrapper<DeptEntity>()
                            .eq("name", architecDTO.getDeptName()).eq("status", 0));
                    if(null == deptEntity){
                        dept.setCompanyId(company.getId());
                        dept.setName(architecDTO.getDeptName());
                        dept.setStatus(architecDTO.getStatus());
                        deptDao.insert(dept);
                    }
                }
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }


//    @Override
//    public List<ArchitectureDTO> selectAllListByGid(Integer gid) {
//        List<ArchitectureDTO> dtoList = new ArrayList<>();
//        List<GroupEntity> groupEntities = groupDao.selectList(new QueryWrapper<GroupEntity>().eq("id",gid).eq("status",0));
//        for(GroupEntity groupEntity: groupEntities){
//            //集团信息
//            ArchitectureDTO dto =new ArchitectureDTO();
//            dto.setGId(groupEntity.getId());
//            dto.setStatus(groupEntity.getStatus());
//            dto.setName(groupEntity.getName());
//            LinkedHashSet<ComopanyNoe> noeList = new LinkedHashSet<>();
//            List<CompanyEntity> comlist = companyDao.selectList(new QueryWrapper<CompanyEntity>()
//                    .eq("group_id",dto.getGId()).eq("company_id",0).eq("status",0));
//            for(CompanyEntity com : comlist){
//                ComopanyNoe noe = new ComopanyNoe();
//                noe.setName(com.getName());
//                noe.setStatus(com.getStatus());
//                noe.setId(com.getId());
//                List<CompanyEntity> companyTwo = companyDao.selectList(new QueryWrapper<CompanyEntity>()
//                        .eq("company_id", noe.getId()).eq("status", 0));
//                for(CompanyEntity tow : companyTwo){
//                    List<DeptEntity> deptEntities = deptDao.selectList(new QueryWrapper<DeptEntity>()
//                            .eq("company_id", tow.getId()).eq("status", 0));
////                    tow.setCpList(deptEntities);
//                }
////                noe.setCpList(companyTwo);
//                noeList.add(noe);
//                dto.setCpList(noeList);
//            }
//            dtoList.add(dto);
//        }
//        return dtoList;
//    }

    @Override
    @Transactional
    public boolean update(ArchitecDTO dto) {
        GroupEntity group =new GroupEntity();
        CompanyEntity company = new CompanyEntity();
        CompanyEntity comtwo = new CompanyEntity();
        try {
            //修改集团信息
              if(!StringUtils.isEmpty(dto.getGName())){
                  group.setName(dto.getGName());
                  group.setStatus(dto.getStatus());
                  group.setId(dto.getGid());
                  groupDao.updateById(group);
                 if(dto.getStatus() != null){
                     company.setStatus(dto.getStatus());
                     companyDao.update(company,new QueryWrapper<CompanyEntity>()
                             .eq("group_id",group.getId()).eq("company_id",0));
                     List<CompanyEntity> companyEntities = companyDao.selectList(new QueryWrapper<CompanyEntity>()
                             .eq("group_id",group.getId()).eq("company_id",0));
                     for(CompanyEntity c:companyEntities){
                         List<CompanyEntity> company1 = companyDao.selectList(new QueryWrapper<CompanyEntity>()
                                 .eq("company_id",c.getId()));
                         for(CompanyEntity c1:company1){
                             companyDao.update(company,new QueryWrapper<CompanyEntity>()
                                     .eq("id",c1.getId()));
                             DeptEntity dept =new DeptEntity();
                             dept.setStatus(dto.getStatus());
                             deptDao.update(dept,new QueryWrapper<DeptEntity>().eq("company_id",c1.getId()));
                         }
                     }
                 }
              }
            //修改一级公司
            if(!StringUtils.isEmpty(dto.getCnameOne())){
                company.setName(dto.getCnameOne());
                company.setStatus(dto.getStatus());
                company.setId(dto.getCompanyoneId());
                companyDao.update(company,new QueryWrapper<CompanyEntity>()
                        .eq("id",company.getId()).eq("company_id",0));
                if(dto.getStatus() !=null){
                    List<CompanyEntity> companyEntities = companyDao.selectList(new QueryWrapper<CompanyEntity>()
                            .eq("company_id", company.getId()));
                    for(CompanyEntity c:companyEntities){
                        c.setStatus(dto.getStatus());
                        companyDao.updateById(c);
                        DeptEntity dept =new DeptEntity();
                        dept.setStatus(dto.getStatus());
                        deptDao.update(dept,new QueryWrapper<DeptEntity>().eq("company_id",c.getId()));
                    }
                }
            }
            //修改二级公司
            if(!StringUtils.isEmpty(dto.getCnameTwo())){
                comtwo.setName(dto.getCnameTwo());
                comtwo.setStatus(dto.getStatus());
                comtwo.setId(dto.getCompanytwoId());
                companyDao.updateById(comtwo);
                if(dto.getStatus() !=null){
                    DeptEntity dept =new DeptEntity();
                    dept.setStatus(dto.getStatus());
                    deptDao.update(dept,new QueryWrapper<DeptEntity>().eq("company_id",comtwo.getId()));
                }
            }
            //修改部门
            if(!StringUtils.isEmpty(dto.getDeptName())){
                DeptEntity dept =new DeptEntity();
                dept.setId(dto.getDid());
                dept.setName(dto.getDeptName());
                dept.setStatus(dto.getStatus());
                deptDao.updateById(dept);
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(DeleteDTO dto) {
       try {
           if(null != dto.getGid()){
               //删除集团
               groupDao.deleteById(dto.getGid());
               //删除集团下的公司
               List<CompanyEntity> company = companyDao.selectList(new QueryWrapper<CompanyEntity>()
                       .eq("group_id", dto.getGid()).eq("status",0));
               for(CompanyEntity com : company){
                   //查询下属公司 并查询下属部门 并删除
                   List<DeptEntity> dept = deptDao.selectList(new QueryWrapper<DeptEntity>()
                           .eq("company_id", com.getId()).eq("status",0));
                   companyDao.deleteById(com.getId());
                   for(DeptEntity dep:dept){
                       deptDao.deleteById(dep.getId());
                   }
               }
           }else if(null != dto.getComopanynoe()){
                   //查询下属公司 并查询下属部门 并删除
               List<CompanyEntity> company = companyDao.selectList(new QueryWrapper<CompanyEntity>()
                       .eq("company_id", dto.getComopanynoe()).eq("status",0));
                   companyDao.deleteById(dto.getComopanynoe());
                for(CompanyEntity c: company){
                    List<DeptEntity> dept = deptDao.selectList(new QueryWrapper<DeptEntity>()
                            .eq("company_id", c.getId()).eq("status",0));
                    companyDao.deleteById(c.getId());
                    for(DeptEntity dep:dept){
                        deptDao.deleteById(dep.getId());
                    }
                }
           }else if(null != dto.getComopanytwo()){
               List<DeptEntity> dept = deptDao.selectList(new QueryWrapper<DeptEntity>()
                       .eq("company_id", dto.getComopanytwo()).eq("status",0));
               companyDao.deleteById(dto.getComopanytwo());
               for(DeptEntity dep:dept){
                   deptDao.deleteById(dep.getId());
               }
           }else if(null != dto.getDid()){
               deptDao.deleteById(dto.getDid());
           }
       }catch (Exception e){
           e.printStackTrace();
           return false;
       }
       return true;
    }

    @Override
    public List<GroupEntity> getGroupOneList() {
        List<GroupEntity> status = groupDao.selectList(new QueryWrapper<GroupEntity>());
        for(GroupEntity g:status){
            g.setGid(g.getId());
        }
        return status;
    }

    @Override
    public List<ComopanyNoe> getComBiGid(Integer gid) {
        List<ComopanyNoe> comopanyNoes=new ArrayList<>();
        List<CompanyEntity> companyEntities = companyDao.selectList(new QueryWrapper<CompanyEntity>()
                .eq("group_id", gid)
                .eq("company_id", 0));
        for(CompanyEntity c: companyEntities){
            ComopanyNoe noe =new ComopanyNoe();
            noe.setComopanynoe(c.getId());
            noe.setName(c.getName());
            noe.setStatus(c.getStatus());
            noe.setId(c.getId());
            comopanyNoes.add(noe);
        }
        return comopanyNoes;
    }

    @Override
    public List<ComopanyNoe> getComBiGidAll() {
        List<ComopanyNoe> comopanyNoes=new ArrayList<>();
        List<CompanyEntity>  companyEntities =  companyDao.selectList(new QueryWrapper<CompanyEntity>()
                .eq("company_id",0));
        for(CompanyEntity c: companyEntities){
            ComopanyNoe noe =new ComopanyNoe();
            noe.setComopanynoe(c.getId());
            noe.setName(c.getName());
            noe.setStatus(c.getStatus());
            noe.setId(c.getId());
            noe.setGroupId(c.getGroupId());
            comopanyNoes.add(noe);
        }
        return comopanyNoes;
    }

    @Override
    public List<CompanyEntity> getComTwo(Integer oneid) {
        List<CompanyEntity> company=new ArrayList<>();
        List<CompanyEntity> companyEntities = companyDao.selectList(new QueryWrapper<CompanyEntity>()
                .eq("company_id", oneid));
        for(CompanyEntity companyEntity : companyEntities){
            CompanyEntity cc=new CompanyEntity();
            cc.setComopanytwo(companyEntity.getId());
            cc.setStatus(companyEntity.getStatus());
            cc.setName(companyEntity.getName());
            cc.setGroupId(companyEntity.getGroupId());
            cc.setCompanyId(companyEntity.getCompanyId());
            cc.setId(companyEntity.getId());
            company.add(cc);
        }
        return company;

    }

    @Override
    public List<CompanyEntity> getComTwoAll() {
        List<CompanyEntity> company=new ArrayList<>();
        List<CompanyEntity> companyEntities = companyDao.selectListtwos();
        for(CompanyEntity companyEntity : companyEntities){
            CompanyEntity cc=new CompanyEntity();
            cc.setComopanytwo(companyEntity.getId());
            cc.setStatus(companyEntity.getStatus());
            cc.setName(companyEntity.getName());
            cc.setGroupId(companyEntity.getGroupId());
            cc.setCompanyId(companyEntity.getCompanyId());
            cc.setId(companyEntity.getId());
            company.add(cc);
        }
        return company;
    }

    @Override
    public List<DeptEntity> getDeptList(Integer twoid) {
        List<DeptEntity> deptEntities = deptDao.selectList(new QueryWrapper<DeptEntity>()
                .eq("company_id", twoid));
        for(DeptEntity d:deptEntities){
            d.setDid(d.getId());
        }
        return deptEntities;

    }

    @Override
    public List<DeptEntity> getDeptListAll() {
        List<DeptEntity> status = deptDao.selectList(new QueryWrapper<DeptEntity>());
        for(DeptEntity de:status){
            de.setDid(de.getId());
        }
        return status;
    }

    @Override
    public List<GroupEntity> getGroupOneByRole(Integer groupId) {
        return groupDao.selectList(new QueryWrapper<GroupEntity>()
                .eq("id",groupId));
    }

    @Override
    public List<ComopanyNoe> getComByRole(Integer companyoneId) {
        List<ComopanyNoe> comopanyNoes=new ArrayList<>();
        List<CompanyEntity> companyEntities = companyDao.selectList(new QueryWrapper<CompanyEntity>()
                .eq("id", companyoneId).eq("company_id", 0).eq("status", 0));
        for(CompanyEntity c: companyEntities){
            ComopanyNoe noe =new ComopanyNoe();
            noe.setComopanynoe(c.getId());
            noe.setName(c.getName());
            noe.setStatus(c.getStatus());
            noe.setId(c.getId());
            noe.setGroupId(c.getGroupId());
            comopanyNoes.add(noe);
        }
        return comopanyNoes;
    }

}