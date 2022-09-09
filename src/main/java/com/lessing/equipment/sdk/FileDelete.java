package com.lessing.equipment.sdk;

import java.io.File;

public class FileDelete {
    private final static String FS = System.getProperty("file.separator");
    private static final String TEMP_PATH = "C:\\Users\\admin\\Desktop\\aaaa";
    private static boolean isRunning = false;

    public static boolean deletefile(){
        if (!isRunning) {
            isRunning = true;
            System.out.println("要删除图片文件所在路径为" + TEMP_PATH);
            File fileTemp = new File(TEMP_PATH);
            // 判断文件是否存在
            boolean falg = false;
            falg = fileTemp.exists();
            if (falg) {
                if (true == fileTemp.isDirectory()) {
                    String[] png = fileTemp.list();
                    for (int i = 0; i < png.length; i++) {
                        if (true == png[i].endsWith("jpg")) {
                            File file = new File(TEMP_PATH + FS + png[i]);
                            if (true == file.isFile()) {
                                boolean flag = false;
                                flag = file.delete();
                                if (flag) {
                                    System.out.println("成功删除无效图片文件:" + file.getName());
                                }
                            }
                        }
                    }
                }
            } else {
               return false;
            }
            isRunning = false;
        }
        return true;
    }

}
