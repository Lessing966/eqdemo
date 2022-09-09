package com.lessing.equipment.sdk;



import com.alibaba.fastjson.JSONObject;
import com.lessing.equipment.common.utils.R;
import com.lessing.equipment.common.utils.SpringUtils;
import com.lessing.equipment.lib.RequestList;
import com.lessing.equipment.modules.eq.dao.EqScaleDao;
import com.lessing.equipment.modules.eq.dto.OperationDTO;

import com.lessing.equipment.modules.eq.entity.EqEntity;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;

@Api(value = "SDK操作Controller",tags = "SDK操作摄像头接口")
@RequestMapping(value = "/eq/sdk")
@RestController
@Log4j2
public class PTZControlUtil {

    @Autowired
    RequestList requestList;


//    static Logger  logger = LogManager.getLogger(PTZControlUtil.class);
//    //初始化SDK
//    public static NetSDKLib netsdk = NetSDKLib.NETSDK_INSTANCE;
//    // 设备信息
//    public static NetSDKLib.NET_DEVICEINFO_Ex m_stDeviceInfo = new NetSDKLib.NET_DEVICEINFO_Ex();
//    // 登陆句柄
//    public static NetSDKLib.LLong m_hLoginHandle = new NetSDKLib.LLong(0);
//    // 设备断线通知回调
//    private static DisConnect disConnect = new DisConnect();
//    // 网络连接恢复
//    private static HaveReConnect haveReConnect = new HaveReConnect();
//
//    private DownLoadPosCallBackByTime m_DownLoadPosByTime = new DownLoadPosCallBackByTime(); // 录像下载进度
//    private static boolean bInit    = false;
//    private static boolean bLogopen = false;
//
//    public static void stopDownLoadRecordFile() {
//        if (m_hLoginHandle.longValue() == 0) {
//            return;
//        }
//        netsdk.CLIENT_StopDownload(m_hLoginHandle);
//    }
//
//    // 设备断线回调: 通过 CLIENT_Init 设置该回调函数，当设备出现断线时，SDK会调用该函数
//    private static class DisConnect implements NetSDKLib.fDisConnect {
//        public void invoke(NetSDKLib.LLong m_hLoginHandle, String pchDVRIP, int nDVRPort, Pointer dwUser) {
//            System.out.printf("Device[%s] Port[%d] DisConnect!\n", pchDVRIP, nDVRPort);
//        }
//    }
//
//    // 网络连接恢复，设备重连成功回调
//    // 通过 CLIENT_SetAutoReconnect 设置该回调函数，当已断线的设备重连成功时，SDK会调用该函数
//    private static class HaveReConnect implements NetSDKLib.fHaveReConnect {
//        public void invoke(NetSDKLib.LLong m_hLoginHandle, String pchDVRIP, int nDVRPort, Pointer dwUser) {
//            System.out.printf("ReConnect Device[%s] Port[%d]\n", pchDVRIP, nDVRPort);
//        }
//    }

//    @ApiOperation("操作摄像头")
//    @RequestMapping(value = "/upControl", method = RequestMethod.POST)
//    public void upControl(@RequestBody OperationDTO operation) {
//        try {
//            //先初始化SDK
//            PTZControlUtil.init(disConnect, haveReConnect);
//            //若未登录 先登录
//            if (m_hLoginHandle.longValue() == 0) {
//                PTZControlUtil.login(operation.getIp(), 37777, operation.getUsername(), operation.getPassword());
//            }
//            //开始移动
//            if (m_hLoginHandle.longValue() != 0) {
//                //这里的 1 是摄像头的移动速度
//                start(0, operation.getParam(), 1);
//            }
//            System.out.println("操作完成！");
//            //停止移动
//            stop(0, operation.getParam());
//            //退出
//            PTZControlUtil.logout();
//            System.out.println("退出登录！。。。");
//            //释放资源
//            PTZControlUtil.cleanup();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


   @ApiOperation("操作摄像头")
   @PostMapping("/upControl")
    public R upControl(@RequestBody OperationDTO operation) {
        String code = requestList.controlMovePTZ(operation);
        if(code.equals("0")){
            return R.ok();
        }
        return R.error();
    }
//
//
//
//
//    @ApiOperation("小程序开始移动")
//    @RequestMapping(value = "/start", method = RequestMethod.POST)
//    public void start(@RequestBody OperationDTO operation) {
//        System.out.println("请求移动");
//        //先初始化SDK
//        PTZControlUtil.init(disConnect, haveReConnect);
//        //若未登录 先登录
//        if (m_hLoginHandle.longValue() == 0) {
//            PTZControlUtil.login(operation.getIp(), 37777, operation.getUsername(), operation.getPassword());
//        }
//        //开始移动
//        if (m_hLoginHandle.longValue() != 0) {
//            System.out.println("开始移动");
//            //这里的 1 是摄像头的移动速度
//            start(0, Integer.parseInt(operation.getParam()), 1);
//        }
//    }
//
//    @ApiOperation("小程序停止移动")
//    @RequestMapping(value = "/stop", method = RequestMethod.POST)
//    public void stop(@RequestBody OperationDTO operation) {
//        System.out.println("请求停止");
//        //先初始化SDK
////        PTZControlUtil.init(disConnect, haveReConnect);
//        //若未登录 先登录
//        if (m_hLoginHandle.longValue() == 0) {
//            PTZControlUtil.login(operation.getIp(), 37777, operation.getUsername(), operation.getPassword());
//        }
//        //停止移动
//        if (m_hLoginHandle.longValue() != 0) {
//            //停止移动
//            System.out.println("停止移动");
//            stop(0, Integer.parseInt(operation.getParam()));
//        }
//        PTZControlUtil.logout();
//        PTZControlUtil.cleanup();
//    }
//
//
//    private static void start(int channelid, int param1, int param2) {
//        if (m_hLoginHandle.longValue() != 0) {
//            netsdk.CLIENT_DHPTZControlEx(m_hLoginHandle,
//                    channelid, param1,
//                    param1, param2, 0, 0);
//        } else {
//            System.out.println("登录句柄不存在！");
//        }
//    }
//
//    private static void stop(int channelid, int param1) {
//        netsdk.CLIENT_DHPTZControlEx(m_hLoginHandle, channelid, param1,
//                0, 0, 0, 1);
//    }

//    /**
//     * 设备抓拍
//     *
//     * @return
//     */
//    public static boolean snap(String ip, String username, String password) {
//        //先初始化SDK
//        PTZControlUtil.init(disConnect, haveReConnect);
//        //登录
//        boolean login = PTZControlUtil.login(ip, 37777, username, password);
//        if (!login) {
//            return false;
//        } else {
//            NetSDKLib.SNAP_PARAMS stuSnapParams = new NetSDKLib.SNAP_PARAMS();
//            stuSnapParams.Channel = 0;            // channel
//            stuSnapParams.mode = 0;                // capture picture mode
//            stuSnapParams.Quality = 3;                // picture quality
//            stuSnapParams.InterSnap = 0;    // timer capture picture time interval
//            stuSnapParams.CmdSerial = 0;            // request serial
//            IntByReference reserved = new IntByReference(0);
//            if (!netsdk.CLIENT_SnapPictureEx(m_hLoginHandle, stuSnapParams, reserved)) {
//                System.err.printf(ip + "：CLIENT_SnapPictureEx Failed!");
//                PTZControlUtil.logout();
//                return false;
//            } else {
//                System.out.println(ip + "：CLIENT_SnapPictureEx success");
//            }
//            return true;
//        }
//    }
//
//    /**
//     * \if ENGLISH_LANG
//     * start alarm listen
//     * \else
//     * 向设备订阅报警
//     * \endif
//     */
//    public static void startListen(String ip, String username, String password) {
//        logger.info("开始监听事件列表");
//        PTZControlUtil.init(disConnect, haveReConnect);
//
//        boolean login = PTZControlUtil.login(ip, 37777, username, password);
//        if(!login){
//            return;
//        }
//        if (!netsdk.CLIENT_StartListenEx(m_hLoginHandle)) {
//            System.err.printf("CLIENT_StartListenEx Failed! Errer.");
//        } else {
//            System.out.println("CLIENT_StartListenEx success.");
//
//        }
//    }
//
//
//    /**
//     * \if ENGLISH_LANG
//     * Login Device
//     * \else
//     * 登录设备
//     * \endif
//     */
//    public static boolean login(String m_strIp, int m_nPort, String m_strUser, String m_strPassword) {
//        //入参
//        NetSDKLib.NET_IN_LOGIN_WITH_HIGHLEVEL_SECURITY pstInParam = new NetSDKLib.NET_IN_LOGIN_WITH_HIGHLEVEL_SECURITY();
//        pstInParam.nPort = m_nPort;
//        pstInParam.szIP = m_strIp.getBytes();
//        pstInParam.szPassword = m_strPassword.getBytes();
//        pstInParam.szUserName = m_strUser.getBytes();
//        //出参
//        NetSDKLib.NET_OUT_LOGIN_WITH_HIGHLEVEL_SECURITY pstOutParam = new NetSDKLib.NET_OUT_LOGIN_WITH_HIGHLEVEL_SECURITY();
//        pstOutParam.stuDeviceInfo = m_stDeviceInfo;
//        m_hLoginHandle = netsdk.CLIENT_LoginWithHighLevelSecurity(pstInParam, pstOutParam);
//        if (m_hLoginHandle.longValue() == 0) {
//            System.err.println(m_strIp + "登录失败");
//        } else {
//            //在登录成功之后 设置远程抓拍回调方法
//            SnapModele snapModele = new SnapModele();
//            snapModele.ip = m_strIp;
//            PTZControlUtil.setSnapRevCallBack(snapModele);
//            //设置报警回调
//            AlarmEventQueue alarm = new AlarmEventQueue();
//            EqEntity geteq = PTZControlUtil.geteq(m_strIp);//根据ip查询设备和项目 传入回调中 （知道是那个设备的事件回调）
//            alarm.eqSn = geteq.getCameraSn();
//            alarm.paddress = geteq.getPAddress();
//            netsdk.CLIENT_SetDVRMessCallBack(alarm, null);
//            System.out.println("登录成功！ [ " + m_strIp + " ]");
//        }
//        return m_hLoginHandle.longValue() == 0 ? false : true;
//    }
//
//    /**
//     * \if ENGLISH_LANG
//     * Logout Device
//     * \else
//     * 登出设备
//     * \endif
//     */
//    public static boolean logout() {
//        if(m_hLoginHandle.longValue() == 0) {
//            return false;
//        }
//        boolean bRet = netsdk.CLIENT_Logout(m_hLoginHandle);
//        if(bRet) {
//            m_hLoginHandle.setValue(0);
//        }
//        return bRet;
//    }
//
//    /**
//     * \if ENGLISH_LANG
//     * Set Capture Picture Callback
//     * \else
//     * 设置抓图回调函数
//     * \endif
//     */
//    public static void setSnapRevCallBack(NetSDKLib.fSnapRev cbSnapReceive){
//        netsdk.CLIENT_SetSnapRevCallBack(cbSnapReceive, null);
//    }
//
//
//    //根据ip查询设备和项目
//    public static EqEntity geteq(String ip){
//        EqScaleDao eqalrmdao = (EqScaleDao) SpringUtils.getBean("eqScaleDao");
//       return eqalrmdao.selectgetEq(ip);
//    }
//
//
//    /**
//     * \if ENGLISH_LANG
//     * Init
//     * \else
//     * 初始化
//     * \endif
//     */
//    public static boolean init(NetSDKLib.fDisConnect disConnect, NetSDKLib.fHaveReConnect haveReConnect) {
//        bInit = netsdk.CLIENT_Init(disConnect, null);
//        if(!bInit) {
//            System.out.println("Initialize SDK failed");
//            return false;
//        }
//
//        //打开日志，可选
//        NetSDKLib.LOG_SET_PRINT_INFO setLog = new NetSDKLib.LOG_SET_PRINT_INFO();
//        File path = new File("./sdklog/");
//        if (!path.exists()) {
//            path.mkdir();
//        }
//        String logPath = path.getAbsoluteFile().getParent() + "\\sdklog\\" + LoginModule.getDate()  + ".log";
//        setLog.nPrintStrategy = 0;
//        setLog.bSetFilePath = 1;
//        System.arraycopy(logPath.getBytes(), 0, setLog.szLogFilePath, 0, logPath.getBytes().length);
//        System.out.println(logPath);
//        setLog.bSetPrintStrategy = 1;
//        bLogopen = netsdk.CLIENT_LogOpen(setLog);
//        if(!bLogopen ) {
//            System.err.println("Failed to open NetSDK log");
//        }
//
//        // 设置断线重连回调接口，设置过断线重连成功回调函数后，当设备出现断线情况，SDK内部会自动进行重连操作
//        // 此操作为可选操作，但建议用户进行设置
//        netsdk.CLIENT_SetAutoReconnect(haveReConnect, null);
//
//        //设置登录超时时间和尝试次数，可选
//        int waitTime = 5000; //登录请求响应超时时间设置为5S
//        int tryTimes = 1;    //登录时尝试建立链接1次
//        netsdk.CLIENT_SetConnectTime(waitTime, tryTimes);
//
//
//        // 设置更多网络参数，NET_PARAM的nWaittime，nConnectTryNum成员与CLIENT_SetConnectTime
//        // 接口设置的登录设备超时时间和尝试次数意义相同,可选
//        NetSDKLib.NET_PARAM netParam = new NetSDKLib.NET_PARAM();
//        netParam.nConnectTime = 10000;      // 登录时尝试建立链接的超时时间
//        netParam.nGetConnInfoTime = 3000;   // 设置子连接的超时时间
//        netsdk.CLIENT_SetNetworkParam(netParam);
//        System.err.println("初始化完成！");
//        return true;
//    }
//
//    /**
//     * \if ENGLISH_LANG
//     * CleanUp
//     * \else
//     * 清除环境
//     * \endif
//     */
//    public static void cleanup() {
//        if(bLogopen) {
//            netsdk.CLIENT_LogClose();
//        }
//
//        if(bInit) {
//            netsdk.CLIENT_Cleanup();
//        }
//    }

}