//package com.lessing.equipment.sdk;
//
//import com.lessing.equipment.common.utils.SpringUtils;
//import com.lessing.equipment.lib.NetSDKLib;
//import com.lessing.equipment.lib.SavePath;
//import com.lessing.equipment.modules.eq.dao.EqAlrmDao;
//import com.lessing.equipment.modules.eq.entity.EqalrmEnyity;
//import com.sun.jna.NativeLong;
//import com.sun.jna.Pointer;
//
//import com.sun.jna.ptr.IntByReference;
//import org.springframework.stereotype.Component;
//
//import java.awt.*;
//import java.util.*;
//import java.util.List;
//
//@Component
//public class AlarmEventQueue  implements NetSDKLib.fMessCallBack{
//    //下载回调类
//    DownLoadPosCallBackByTime cbTimeDownLoadPos = new DownLoadPosCallBackByTime();
//    NetSDKLib.NET_TIME stTimeStart =new NetSDKLib.NET_TIME();
//    NetSDKLib.NET_TIME stTimeEnd =new NetSDKLib.NET_TIME();
//    //初始化SDK
//    public static NetSDKLib netsdk = NetSDKLib.NETSDK_INSTANCE;
//    private final EventQueue eventQueue = Toolkit.getDefaultToolkit().getSystemEventQueue();
//    EqAlrmDao  eqalrmdao = (EqAlrmDao) SpringUtils.getBean("eqAlrmDao");
//
//    // alarm event info list
//    List<AlarmEventInfo> data = new LinkedList<>();
//    String eqSn;
//    String paddress;
//
//    /**
//     * 动态检测监听事件回调
//     * @param lCommand
//     * @param lLoginID
//     * @param pStuEvent
//     * @param dwBufLen
//     * @param strDeviceIP
//     * @param nDevicePort
//     * @param dwUser
//     * @return
//     */
//    @Override
//    public boolean invoke(int lCommand, NetSDKLib.LLong lLoginID, Pointer pStuEvent, int dwBufLen, String strDeviceIP, NativeLong nDevicePort, Pointer dwUser) {
//        EqalrmEnyity al=new EqalrmEnyity();
//        //动态检测报警
//        switch (lCommand) {
//            case NetSDKLib.NET_MOTION_ALARM_EX:
//             {
//                byte []alarm = new byte[dwBufLen];
//                pStuEvent.read(0, alarm, 0, dwBufLen);
//                for (int i = 0; i < dwBufLen; i++) {
//                    if (alarm[i] == 1) {
//                        AlarmEventInfo alarmEventInfo = new AlarmEventInfo(i, lCommand, AlarmStatus.ALARM_START);
//                        if (!data.contains(alarmEventInfo)) {
//                            data.add(alarmEventInfo);
//                            al.setChn(String.valueOf(alarmEventInfo.getChn()));
//                            al.setType(String.valueOf(alarmEventInfo.getType()));
//                            al.setStatus(1);
//                            al.setStartTime(alarmEventInfo.getDate().getTime());
//                            al.setESn(eqSn);
//                            al.setPAddress(paddress);
//                            eqalrmdao.insert(al);
//                            eventQueue.postEvent(new AlarmListenEvent(this, alarmEventInfo));
//                            stTimeStart.setTime(alarmEventInfo.getDate().get(Calendar.YEAR),
//                                    alarmEventInfo.getDate().get(Calendar.MONTH)+1,
//                                    alarmEventInfo.getDate().get(Calendar.DAY_OF_MONTH),
//                                    alarmEventInfo.getDate().get(Calendar.HOUR_OF_DAY),
//                                    alarmEventInfo.getDate().get(Calendar.MINUTE),
//                                    alarmEventInfo.getDate().get(Calendar.SECOND));
//                            System.err.println("开始时间："+stTimeStart);
//                        }
//                    }else {
//                        AlarmEventInfo alarmEventInfo = new AlarmEventInfo(i, lCommand, AlarmStatus.ALARM_STOP);
//                        if (data.remove(alarmEventInfo)) {
//                            EqalrmEnyity eqal = eqalrmdao.selectEq();
//                            al.setStopTime(alarmEventInfo.getDate().getTime());
//                            al.setId(eqal.getId());
//                            int i1 = eqalrmdao.updateById(al);
//                            eventQueue.postEvent(new AlarmListenEvent(this, alarmEventInfo));
//                            stTimeEnd.setTime(alarmEventInfo.getDate().get(Calendar.YEAR),
//                                    alarmEventInfo.getDate().get(Calendar.MONTH)+1,
//                                    alarmEventInfo.getDate().get(Calendar.DAY_OF_MONTH),
//                                    alarmEventInfo.getDate().get(Calendar.HOUR_OF_DAY),
//                                    alarmEventInfo.getDate().get(Calendar.MINUTE),
//                                    alarmEventInfo.getDate().get(Calendar.SECOND));
//                            System.err.println("结束时间："+stTimeEnd);
//                         if(null != stTimeStart && null != stTimeEnd && i1 !=0){
//                             NetSDKLib.NET_RECORDFILE_INFO[] stFileInfo = (NetSDKLib.NET_RECORDFILE_INFO[])new NetSDKLib.NET_RECORDFILE_INFO().toArray(2000);
//                             IntByReference nFindCount = new IntByReference(0);
//                             boolean bRet = netsdk.CLIENT_QueryRecordFile(lLoginID, 0,
//                                     2, stTimeStart, stTimeEnd, null, stFileInfo,
//                                     stFileInfo.length * stFileInfo[0].size(), nFindCount, 5000, false);
//                             System.out.println(bRet);
//                             if(bRet){
//                                 NetSDKLib.LLong lLong;
//                                 do {
//                                     Map<String, String> saveRecordFilePath = SavePath.getSavePath().getSaveRecordFilePath();
//                                      lLong = netsdk.CLIENT_DownloadByTimeEx(lLoginID, 0, 0,
//                                             stTimeStart, stTimeEnd, saveRecordFilePath.get("SavedFileName")+ ".dav",
//                                             cbTimeDownLoadPos, null, null, null, null);
//                                     if(lLong.longValue() != 0) {
//                                         System.out.println("开始下载");
//                                         ConvertVideo.inputPath= saveRecordFilePath.get("SavedFileName")+ ".dav";
//                                         ConvertVideo.outputPath = saveRecordFilePath.get("SavedFileName")+ ".mp4";
//                                         ConvertVideo.process();
//                                         al.setUrl("192.168.0.115:9090/"+saveRecordFilePath.get("FileName")+".mp4");
//                                         al.setId(eqal.getId());
//                                         al.setStatus(0);
//                                         eqalrmdao.updateById(al);
//                                     } else {
//                                         PTZControlUtil.stopDownLoadRecordFile();
//                                         System.err.println("等待");
//                                     }
//                                 } while (lLong.longValue() == 0);
//                             }
//                         }
//                        }
//                    }
//                }
//                break;
//            }
//            default:
//                break;
//        }
//        return true;
//    }
//}