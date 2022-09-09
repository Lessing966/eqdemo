package com.lessing.equipment.modules.eq.controller;

import com.alibaba.fastjson.JSONObject;
import com.lessing.equipment.common.utils.R;

import com.lessing.equipment.common.utils.RequestJson;
import com.lessing.equipment.lib.RequestList;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/message")
@Api(value = "",tags = "解密图片")
public class EqMessageController {


    @Autowired
    RequestList requestList;

    @Autowired
    RequestJson requestJson;


    @ApiOperation("解密图片")
    @RequestMapping("/mge")
    public R message(@RequestBody JSONObject jsonObject) {
        jsonObject.getString("msgType");

        return R.error(200,"成功");

    }


}