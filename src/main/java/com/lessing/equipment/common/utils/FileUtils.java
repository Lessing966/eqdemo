package com.lessing.equipment.common.utils;

import java.io.File;

public class FileUtils {

    /*
     * 设置抓图保存路径
     */
    public static String getSaveCapturePath(String eqSn) {
        File path1 = new File("C:\\Users\\Administrator\\Desktop\\tupian\\");
        if (!path1.exists()) {
            path1.mkdir();
        }

        File path2 = new File("C:\\Users\\Administrator\\Desktop\\tupian\\");
        if (!path2.exists()) {
            path2.mkdir();
        }

        String strFileName = path2.getAbsolutePath() + "/" + eqSn + ".jpg";

        return strFileName;
    }
}
