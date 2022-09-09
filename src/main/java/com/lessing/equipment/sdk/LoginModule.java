//package com.lessing.equipment.sdk;
//
//
//import com.lessing.equipment.lib.ErrorCode;
//import com.lessing.equipment.lib.NetSDKLib;
//
//import java.io.File;
//import java.text.SimpleDateFormat;
//
///**
// * 登陆接口实现
// * 主要有 ：初始化、登陆、登出功能
// */
//public class LoginModule {
//
//	public static NetSDKLib netsdk = NetSDKLib.NETSDK_INSTANCE;
//
//	// 设备信息
//	public static NetSDKLib.NET_DEVICEINFO_Ex m_stDeviceInfo = new NetSDKLib.NET_DEVICEINFO_Ex();
//
//	// 登陆句柄
//	public static NetSDKLib.LLong m_hLoginHandle = new NetSDKLib.LLong(0);
//
//	private static boolean bInit    = false;
//	private static boolean bLogopen = false;
//
//	/**
//	 * \if ENGLISH_LANG
//	 * Init
//	 * \else
//	 * 初始化
//	 * \endif
//	 */
//	public static boolean init(NetSDKLib.fDisConnect disConnect, NetSDKLib.fHaveReConnect haveReConnect) {
//		bInit = netsdk.CLIENT_Init(disConnect, null);
//		if(!bInit) {
//			System.out.println("Initialize SDK failed");
//			return false;
//		}
//
//		//打开日志，可选
//		NetSDKLib.LOG_SET_PRINT_INFO setLog = new NetSDKLib.LOG_SET_PRINT_INFO();
//        File path = new File("./sdklog/");
//        if (!path.exists()) {
//            path.mkdir();
//        }
//		String logPath = path.getAbsoluteFile().getParent() + "\\sdklog\\" + LoginModule.getDate()  + ".log";
//		setLog.nPrintStrategy = 0;
//		setLog.bSetFilePath = 1;
//		System.arraycopy(logPath.getBytes(), 0, setLog.szLogFilePath, 0, logPath.getBytes().length);
//		System.out.println(logPath);
//		setLog.bSetPrintStrategy = 1;
//		bLogopen = netsdk.CLIENT_LogOpen(setLog);
//		if(!bLogopen ) {
//			System.err.println("Failed to open NetSDK log");
//		}
//
//		// 设置断线重连回调接口，设置过断线重连成功回调函数后，当设备出现断线情况，SDK内部会自动进行重连操作
//		// 此操作为可选操作，但建议用户进行设置
//		netsdk.CLIENT_SetAutoReconnect(haveReConnect, null);
//
//		//设置登录超时时间和尝试次数，可选
//		int waitTime = 5000; //登录请求响应超时时间设置为5S
//		int tryTimes = 1;    //登录时尝试建立链接1次
//		netsdk.CLIENT_SetConnectTime(waitTime, tryTimes);
//
//
//		// 设置更多网络参数，NET_PARAM的nWaittime，nConnectTryNum成员与CLIENT_SetConnectTime
//		// 接口设置的登录设备超时时间和尝试次数意义相同,可选
//		NetSDKLib.NET_PARAM netParam = new NetSDKLib.NET_PARAM();
//		netParam.nConnectTime = 10000;      // 登录时尝试建立链接的超时时间
//		netParam.nGetConnInfoTime = 3000;   // 设置子连接的超时时间
//		netsdk.CLIENT_SetNetworkParam(netParam);
//
//		return true;
//	}
//
//	/**
//	 * \if ENGLISH_LANG
//	 * CleanUp
//	 * \else
//	 * 清除环境
//	 * \endif
//	 */
//	public static void cleanup() {
//		if(bLogopen) {
//			netsdk.CLIENT_LogClose();
//		}
//
//		if(bInit) {
//			netsdk.CLIENT_Cleanup();
//		}
//	}
//
//
//
//	// 获取当前时间
//	public static String getDate() {
//		SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String date = simpleDate.format(new java.util.Date()).replace(" ", "_").replace(":", "-");
//
//		return date;
//	}
//
//	/**
//	 * 获取接口错误码和错误信息，用于打印
//	 * @return
//	 */
//	public static String getErrorCodePrint() {
//		return "\n{error code: (0x80000000|" + (LoginModule.netsdk.CLIENT_GetLastError() & 0x7fffffff) +").参考  NetSDKLib.java }"
//				+ " - {error info:" + ErrorCode.getErrorCode(LoginModule.netsdk.CLIENT_GetLastError()) + "}\n";
//	}
//}
