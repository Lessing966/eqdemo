package com.lessing.equipment.modules.sys.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lessing.equipment.common.utils.*;
import com.lessing.equipment.modules.eq.dto.EqListDTO;
import com.lessing.equipment.modules.eq.entity.EqEntity;
import com.lessing.equipment.modules.eq.service.EqService;
import com.lessing.equipment.modules.sys.dto.UserListDTO;
import com.lessing.equipment.modules.sys.service.ProjectService;
import com.lessing.equipment.modules.sys.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;


@RestController
@Api(value = "摄像头Controller",tags = "设备管理API")
@RequestMapping(value = "/sys/eq")
public class CameraController {

    @Autowired
    private EqService eqService;
    @Autowired
    private ProjectService projectService;

    @RequestMapping(value = "/syseqList",method = RequestMethod.POST)
    @ApiOperation("设备列表")
    public R getEqList(@RequestBody EqListDTO entity){
        Page<EqListDTO> list = eqService.selectEqList(entity);
        return R.ok().put("data",list);
    }

    @RequestMapping(value = "/getpoj",method = RequestMethod.GET)
    @ApiOperation("查询所有项目")
    public R getpoj(){
        return R.ok().put("data",projectService.selectpojname());
    }


    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @ApiOperation("新增设备")
    public R add(@RequestBody EqEntity eq){
        String msg = eqService.add(eq);
        if(msg.equals("操作成功。")){
            return R.ok().put("test",msg);
        }
        return R.error().put("test",msg);
    }

    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @ApiOperation("编辑设备")
    public R update(@RequestBody EqEntity eqEntity){
        if(eqService.update(eqEntity)){
            return R.ok();
        }
        return R.error();
    }

    @RequestMapping(value = "/delete",method = RequestMethod.GET)
    @ApiOperation("删除设备")
    public R delete(Integer id){
        boolean delete = eqService.delete(id);
        if(delete){
            return R.ok();
        }
        return R.error("删除失败");
    }

    @RequestMapping(value="/eqDownloadExcel",method = RequestMethod.GET)
    @ApiOperation("下载模板")
    public ResponseEntity<Resource> eqDownloadExcel(HttpServletResponse response) {
        try {
            ClassPathResource cpr = new ClassPathResource("/template/"+"监控设备信息.xlsx");
            InputStream is = cpr.getInputStream();
            Workbook workbook = new XSSFWorkbook(is);
            String fileName = "监控设备信息.xlsx";
            ExcelTemplateUtil.downLoadExcel(fileName, response, workbook);

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return new ResponseEntity<Resource>(HttpStatus.OK);
    }

    @RequestMapping(value = "/eqimportExcel",method = RequestMethod.POST)
    @ApiOperation("一键导入")
    public R eqimportExcel(@RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            //导入转换成实体的list
            List<EqEntity> eqEntities = ExcelUtils.readExcel("", EqEntity.class, file);
            if(eqEntities.size()==0){
                return R.error("识别文件数据失败！");
            }
            for(EqEntity eq : eqEntities){
                if(StringUtils.isEmpty(eq.getCameraSn()) ||
                        StringUtils.isEmpty(eq.getIp()) ||
                        StringUtils.isEmpty(eq.getUsername()) || StringUtils.isEmpty(eq.getPassword())){
                    return R.error("字段全为必填项");
                }
              String add = eqService.add(eq);
                if(!add.equals("操作成功。")){
                    return R.error().put("test",add);
                }
            }
            return R.ok();
        }catch (Exception e){
            e.printStackTrace();
            return R.error();
        }
    }

}