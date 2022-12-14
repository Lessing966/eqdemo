package com.lessing.equipment.modules.eq.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lessing.equipment.common.utils.R;
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

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
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

//    @Value("${openapi.tpurl}")
//    String Url;

    @Value("${openapi.zpurl}")
    String zpurl;
    @Value("${openapi.local}")
    String local;


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
    public List<EqDTO> getEqList(String eSn) throws IOException {
        List<EqDTO> eqDTOList =new ArrayList<>();
        List<ProjectEntity> projectEntities = projectDao.selectList(new QueryWrapper<ProjectEntity>().eq("status",0));
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
                if(StringUtils.isEmpty(e.getUrl())){
                    String s = requestList.DeviceSnap(e);
//                boolean snap = PTZControlUtil.snap(e.getIp(), e.getUsername(), e.getPassword());
                    if(!StringUtils.isEmpty(s)){
                        download(s, local,e.getIp()+".jpg");
                        System.out.println("??????");
//                    String url = Url+e.getIp()+".jpg";
                        e.setUrl(zpurl+e.getIp()+".jpg");
                        eqScaleDao.updateByIp(e.getIp(),e.getUrl());
                    }else {
                        e.setUrl(zpurl+"err"+".png");
                        e.setOffline(true);
                    }
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
    public List<EqEntity> selectEqlistByRole(Integer gid,Integer one,Integer two,Integer did) {
        List<EqEntity> list = eqDao.selectEqByRole(gid,one,two,did);
        return list;
    }

    @Override
    public List<EqDTO> selectEqlistByRoleapp(Integer groupId, Integer noeid, Integer twoid, Integer did,String sn) {
        List<EqDTO> eqDTOList =new ArrayList<>();
        List<ProjectEntity> projectEntities =new ArrayList<>();
        projectEntities = projectDao.selectby(groupId,noeid,twoid,did);
        for(ProjectEntity p:projectEntities){
            EqDTO eqDTO =new EqDTO();
            List<EqEntity> eqEntities = eqDao.selectEqList(String.valueOf(p.getId()),sn);
            for(EqEntity e:eqEntities){
                eqDTO.setAddress(p.getAddress());
                eqDTO.setStatus(p.getStatus());
                eqDTO.setName(p.getName());
                eqDTO.setId(p.getId());
//                if(StringUtils.isEmpty(e.getUrl())){
                    String s = requestList.DeviceSnap(e);
                    if(!StringUtils.isEmpty(s)){
                        try {
                            download(s, local,e.getIp()+".jpg");
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        System.out.println("??????");
                        e.setUrl(zpurl+e.getIp()+".jpg");
                        eqScaleDao.updateByIp(e.getIp(),e.getUrl());
                    }else {
                        e.setUrl(zpurl+"err"+".png");
                        e.setOffline(true);
                    }
//                }
                e.setPAddress(eqDTO.getAddress());
                e.setPName(eqDTO.getName());
                e.setPId(Long.valueOf(eqDTO.getId()));
            }
            eqDTO.setEqList(eqEntities);
            if(null != eqDTO.getId()){
                eqDTOList.add(eqDTO);
            }
        }
        return eqDTOList;
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
    public String add(EqEntity eq) {
        eq.setChannel("0");
        String msg = requestList.addeq(eq);
        if(msg.equals("???????????????")){
            JSONObject jsonObject = requestList.bindDeviceLive(eq);
            redisUtils.set(eq.getIp(),jsonObject.getString("hls"));
            eqDao.insert(eq);
            return msg;
        }else {
            return msg;
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
    public boolean delete(Integer id) {
        EqEntity eq = eqDao.selectOne(new QueryWrapper<EqEntity>().eq("eq_id", id));
        if(!ObjectUtils.isEmpty(eq)){
            redisUtils.delete(eq.getIp());
            EqScaleEntity scale = eqScaleDao.selectOne(new QueryWrapper<EqScaleEntity>().eq("eq_id", eq.getEqId()));
            if(!ObjectUtils.isEmpty(scale)){
                scale.setEqId(null);
                eqScaleDao.update(scale,new QueryWrapper<EqScaleEntity>().eq("eq_id",eq.getEqId()));
            }
            String s = requestList.unBindDevice(eq);
            if(!s.equals("0")){
                return false;
            }
        }
        eqDao.delete(new QueryWrapper<EqEntity>().eq("eq_id", id));
        return true;
    }

    @Override
    public List<EqEntity> getProjList(String uid) {
        List<EqEntity> eqEntities=new ArrayList<>();
        List<ProjectEntity> poj = projectDao.selectList(new QueryWrapper<ProjectEntity>().eq("status",0));
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
        List<ProjectEntity> poj = projectDao.selectList(new QueryWrapper<ProjectEntity>().eq("status",0));
        for(ProjectEntity c : poj) {
            Set<String> newList = new HashSet<>();
            if (!StringUtils.isEmpty(c.getUsername())) {
                String substring = c.getUsername().substring(1, c.getUsername().length() - 1);
                String[] split = substring.split(",");
                List<String> list1 = Arrays.asList(split);
                newList.addAll(list1);

                UserEntity u = userDao.selectOne(new QueryWrapper<UserEntity>()
                        .eq("user_name", c.getHead()));
                UserEntity u1 = userDao.selectOne(new QueryWrapper<UserEntity>()
                        .eq("user_name", c.getHon()));
                if (null != u) {
                    newList.add(String.valueOf(u.getId()));
                }
                if (null != u1) {
                    newList.add(String.valueOf(u1.getId()));
                }
                System.out.println(newList);

                String[] strings1 = newList.toArray(new String[]{});
                    for(int i = 0 ; i <strings1.length ; i++ ){
                        if(strings1[i].equals(uid)){
                            EqDTO eq =new EqDTO();
                            List<EqEntity> eqEntities1 = eqDao.selectEqList(String.valueOf(c.getId()),eSn);
                            for(EqEntity e:eqEntities1){
                                eq.setId(c.getId());
                                eq.setAddress(c.getAddress());
                                eq.setStatus(c.getStatus());
                                eq.setName(c.getName());
                                e.setPName(eq.getName());
                                e.setPId(Long.valueOf(eq.getId()));
                                e.setPAddress(eq.getAddress());
//                                if(StringUtils.isEmpty(e.getUrl())){
                                    String s = requestList.DeviceSnap(e);
                                    if(!StringUtils.isEmpty(s)){
                                        try {
                                            download(s, local,e.getIp()+".jpg");
                                        } catch (IOException ex) {
                                            throw new RuntimeException(ex);
                                        }
                                        System.out.println("??????");
                                        e.setUrl(zpurl+e.getIp()+".jpg");
                                        eqScaleDao.updateByIp(e.getIp(),e.getUrl());
                                    }else {
                                        e.setUrl(zpurl+"err"+".png");
                                        e.setOffline(true);
                                    }
//                                }
                            }
                            eq.setEqList(eqEntities1);
                            if(null != eq.getId()){
                                eqEntities.add(eq);
                            }

                        }
                    }
                }
            }
        return eqEntities;
    }


    
//            for(ProjectEntity c : poj){
//        Set<String> newList = new HashSet<>();
//        if(!StringUtils.isEmpty(c.getUsername())){
//            String substring = c.getUsername().substring(1, c.getUsername().length() - 1);
//            String[] split = substring.split(",");
//            List<String> list1 = Arrays.asList(split);
//            newList.addAll(list1);
//            UserEntity u = userDao.selectOne(new QueryWrapper<UserEntity>()
//                    .eq("user_name",c.getHead()));
//            UserEntity u1 = userDao.selectOne(new QueryWrapper<UserEntity>()
//                    .eq("user_name",c.getHon()));
//            if(null != u){
//                newList.add(String.valueOf(u.getId()));
//            }
//            if(null != u1){
//                newList.add(String.valueOf(u1.getId()));
//            }
//            String[] strings1 = newList.toArray(new String[]{});
//            for(int i = 0 ; i <strings1.length ; i++ ){
//                if(strings1[i].equals(uid)){
//                    EqDTO eq =new EqDTO();
//                    eq.setId(c.getId());
//                    eq.setAddress(c.getAddress());
//                    eq.setStatus(c.getStatus());
//                    eq.setName(c.getName());
//                    List<EqEntity> eqEntities1 = eqDao.selectEqList(String.valueOf(c.getId()),eSn);
//                    for(EqEntity e:eqEntities1){
//                        e.setPName(eq.getName());
//                        e.setPId(Long.valueOf(eq.getId()));
//                        e.setPAddress(eq.getAddress());
////                            boolean snap = PTZControlUtil.snap(e.getIp(), e.getUsername(), e.getPassword());
//                        boolean snap =false;
//                        if(snap){
////                                String url = Url+e.getIp()+".jpg";
////                                eqScaleDao.updateByIp(e.getIp(),url);
//                        }
//                    }
//                    eq.setEqList(eqEntities1);
//                    eqEntities.add(eq);
//                }
//
//            }
//        }
//    }


//    public static String getfilename(String httpurl,String filename){
//        try {
//            //????????????URL???????????????????????????????????????URL??????
//            URL url = new URL(httpurl);
//            //????????????
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            //?????????????????????"GET"
//            conn.setRequestMethod("GET");
//            //?????????????????????10???
//            conn.setConnectTimeout(10 * 1000);
//            //?????????????????????????????????
//            InputStream is = conn.getInputStream();
//            //?????????????????????????????????????????????????????????????????????????????????
//            byte[] data = readInputStream(is);
//            //??????????????????????????????????????????????????????????????????????????????????????????Copy.jpg
//            File imageFile = new File("C:\\Users\\admin\\Desktop\\nginx-1.21.1\\img\\"+filename);
//            //???????????????
//            FileOutputStream outStream = new FileOutputStream(imageFile);
//            //????????????
//            outStream.write(data);
//            //??????????????????????????????
//            outStream.close();
//            return filename;
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//     return "";
//    }
//
//    public static byte[] readInputStream(InputStream inStream) throws Exception {
//        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
//        //????????????Buffer?????????
//        byte[] buffer = new byte[6024];
//        //??????????????????????????????????????????-1???????????????????????????
//        int len;
//        //????????????????????????buffer????????????????????????
//        while ((len = inStream.read(buffer)) != -1) {
//            //???????????????buffer???????????????????????????????????????????????????????????????len?????????????????????
//            outStream.write(buffer, 0, len);
//        }
//        //???????????????
//        inStream.close();
//        //???outStream????????????????????????
//        return outStream.toByteArray();
//    }

    public static void download(String urlString, String savePath, String filename) throws IOException {
        System.out.println("?????????"+new Date().getTime());
        // ??????URL
        URL url = new URL(urlString);
        // ????????????
        URLConnection con = url.openConnection();
        //?????????????????????20s
        con.setConnectTimeout(20 * 1000);
        //????????????????????? ?????????
        File sf = new File(savePath);
        if (!sf.exists()) {
            sf.mkdirs();
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("?????????"+new Date().getTime());
        //jdk 1.7 ?????????????????????
        try (InputStream in = con.getInputStream(); OutputStream out = new FileOutputStream(sf.getPath() + "\\" + filename)) {
            //???????????????
            byte[] buff = new byte[1024];
            int n;
            // ????????????
            while ((n = in.read(buff)) >= 0) {
                out.write(buff, 0, n);
            }
            System.out.println("?????????"+new Date().getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static void main(String[] args) throws Exception {
        download("https://lechangecloud.oss-cn-hangzhou.aliyuncs.com/lechange/7E00E86PAJ94EC6_img/Alarm/0/c254c264ee3c49a881e80d70f7414d97.jpg?Expires=1664439664&OSSAccessKeyId=LTAIP4igXeEjYBoG&Signature=sqaJ1KO8SQLwbi3cwGwWNVoQ%2FvE%3D", "C:\\Users\\admin\\Desktop\\zp\\", "121.png");
    }

}
