package com.lessing.equipment.lib;



import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SavePath {
	private SavePath() {}
	
	private static class SavePathHolder {
		private static SavePath instance = new SavePath();
	}
	
	public static SavePath getSavePath() {
		return SavePathHolder.instance;
	}
	
	String s_captureSavePath = "./Capture/" + new Date() + "/";         // 抓拍图片保存路径
	String s_imageSavePath = "./Image/" + new Date() + "/";    		    // 图片保存路径
	String s_recordFileSavePath = "C:\\Users\\Administrator\\Desktop\\luxiang\\" ;   // 录像保存路径
	String s_lujing = s_recordFileSavePath+ SavePath.getDay();
	String wenjian = "/"+SavePath.getDate();
	String url = SavePath.getDay()+wenjian;


	// 获取当前时间
	public static String getDay() {
		SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
		String date = simpleDate.format(new java.util.Date());
		return date;
	}
	/*
	 * 设置抓图保存路径
	 */
	public String getSaveCapturePath() {	
		File path1 = new File("C:\\Users\\admin\\Desktop\\aaaa\\");
        if (!path1.exists()) {
        	path1.mkdir();
        }
        
	    File path2 = new File("C:\\Users\\admin\\Desktop\\aaaa\\");
	    if (!path2.exists()) {
	        path2.mkdir();
	    }
	    
	    String strFileName = path2.getAbsolutePath() + "/" + SavePath.getDay() + ".jpg";
		
	    return strFileName;
	}
	
	/*
	 * 设置智能交通图片保存路径
	 */
	public String getSaveTrafficImagePath() {
        File path1 = new File("./Image/");
        if (!path1.exists()) {
            path1.mkdir();
        }
    	
        File path = new File(s_imageSavePath);
        if (!path.exists()) {
            path.mkdir();
        }
        
        return s_imageSavePath;
	}
	
	
	/*
	 * 设置录像保存路径
	 */
	public Map<String,String> getSaveRecordFilePath() {
		Map<String,String> map =new HashMap<>();
        File path1 = new File("C:\\Users\\Administrator\\Desktop\\luxiang");
        if (!path1.exists()) {
            path1.mkdir();
        }
        
	    File path2 = new File(s_lujing);
	    if (!path2.exists()) {
	        path2.mkdir();
	    }
		String SavedFileName = s_lujing + wenjian; // 默认保存路径
		map.put("SavedFileName",SavedFileName);
		map.put("FileName",url);
		return map;
	}


	// 获取当前时间
	public static String getDate() {
		SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = simpleDate.format(new java.util.Date()).replace(" ", "_").replace(":", "-");

		return date;
	}
}
