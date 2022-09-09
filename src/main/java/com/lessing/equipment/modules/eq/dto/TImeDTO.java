//package com.lessing.equipment.modules.eq.dto;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.lessing.equipment.lib.NetSDKLib;
//import lombok.Data;
//
//import java.io.Serializable;
//
//@Data
//public class TImeDTO implements Serializable {
//    @JsonIgnore
//    private NetSDKLib.NET_TIME startTime;
//    @JsonIgnore
//    private NetSDKLib.NET_TIME stopTime;
//
//    public NetSDKLib.NET_TIME getStartTime() {
//        return startTime;
//    }
//
//    public void setStartTime(NetSDKLib.NET_TIME startTime) {
//        this.startTime = startTime;
//    }
//
//    public NetSDKLib.NET_TIME getStopTime() {
//        return stopTime;
//    }
//
//    public void setStopTime(NetSDKLib.NET_TIME stopTime) {
//        this.stopTime = stopTime;
//    }
//
//    @Override
//    public String toString() {
//        return "TImeDTO{" +
//                "startTime=" + startTime +
//                ", stopTime=" + stopTime +
//                '}';
//    }
//}
