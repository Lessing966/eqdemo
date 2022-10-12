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
                String s = requestList.DeviceSnap(e);
//                boolean snap = PTZControlUtil.snap(e.getIp(), e.getUsername(), e.getPassword());
                if(!StringUtils.isEmpty(s)){
                    download(s, local,e.getIp()+".jpg");
                    System.out.println("结束");
//                    String url = Url+e.getIp()+".jpg";
                    e.setUrl(zpurl+e.getIp()+".jpg");
                    eqScaleDao.updateByIp(e.getIp(),e.getUrl());
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
        if(msg.equals("操作成功。")){
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
//                                String url = Url+e.getIp()+".jpg";
//                                eqScaleDao.updateByIp(e.getIp(),url);
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

//    public static String getfilename(String httpurl,String filename){
//        try {
//            //定义一个URL对象，就是你想下载的图片的URL地址
//            URL url = new URL(httpurl);
//            //打开连接
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            //设置请求方式为"GET"
//            conn.setRequestMethod("GET");
//            //超时响应时间为10秒
//            conn.setConnectTimeout(10 * 1000);
//            //通过输入流获取图片数据
//            InputStream is = conn.getInputStream();
//            //得到图片的二进制数据，以二进制封装得到数据，具有通用性
//            byte[] data = readInputStream(is);
//            //创建一个文件对象用来保存图片，默认保存当前工程根目录，起名叫Copy.jpg
//            File imageFile = new File("C:\\Users\\admin\\Desktop\\nginx-1.21.1\\img\\"+filename);
//            //创建输出流
//            FileOutputStream outStream = new FileOutputStream(imageFile);
//            //写入数据
//            outStream.write(data);
//            //关闭输出流，释放资源
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
//        //创建一个Buffer字符串
//        byte[] buffer = new byte[6024];
//        //每次读取的字符串长度，如果为-1，代表全部读取完毕
//        int len;
//        //使用一个输入流从buffer里把数据读取出来
//        while ((len = inStream.read(buffer)) != -1) {
//            //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
//            outStream.write(buffer, 0, len);
//        }
//        //关闭输入流
//        inStream.close();
//        //把outStream里的数据写入内存
//        return outStream.toByteArray();
//    }

    public static void download(String urlString, String savePath, String filename) throws IOException {
        System.out.println("开始："+new Date().getTime());
        // 构造URL
        URL url = new URL(urlString);
        // 打开连接
        URLConnection con = url.openConnection();
        //设置请求超时为20s
        con.setConnectTimeout(20 * 1000);
        //文件路径不存在 则创建
        File sf = new File(savePath);
        if (!sf.exists()) {
            sf.mkdirs();
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("中间："+new Date().getTime());
        //jdk 1.7 新特性自动关闭
        try (InputStream in = con.getInputStream(); OutputStream out = new FileOutputStream(sf.getPath() + "\\" + filename)) {
            //创建缓冲区
            byte[] buff = new byte[1024];
            int n;
            // 开始读取
            while ((n = in.read(buff)) >= 0) {
                out.write(buff, 0, n);
            }
            System.out.println("结束："+new Date().getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static void main(String[] args) throws Exception {
        download("https://lechangecloud.oss-cn-hangzhou.aliyuncs.com/lechange/7E00E86PAJ94EC6_img/Alarm/0/c254c264ee3c49a881e80d70f7414d97.jpg?Expires=1664439664&OSSAccessKeyId=LTAIP4igXeEjYBoG&Signature=sqaJ1KO8SQLwbi3cwGwWNVoQ%2FvE%3D", "C:\\Users\\admin\\Desktop\\zp\\", "121.png");
    }

}
