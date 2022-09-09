//package com.lessing.equipment.sdk;
//
//import com.lessing.equipment.lib.NetSDKLib;
//import com.sun.jna.Pointer;
//
//
//public class DownLoadPosCallBackByTime implements NetSDKLib.fTimeDownLoadPosCallBack{
//
//    @Override
//    public void invoke(NetSDKLib.LLong lLoginID, final int dwTotalSize, final int dwDownLoadSize, int index, NetSDKLib.NET_RECORDFILE_INFO.ByValue recordfileinfo, Pointer dwUser) {
//        System.out.println("正在下载");
//        if(dwDownLoadSize == -1) {
//            //下载完成停止下载
//            DownLoadRecordModule.stopDownLoadRecordFile(lLoginID);
//        }
//
//
//    }
//
//}
