//package com.lessing.equipment.sdk;
//
//import com.lessing.equipment.common.utils.FileUtils;
//import com.lessing.equipment.lib.NetSDKLib;
//import com.sun.jna.Pointer;
//
//import javax.imageio.ImageIO;
//import java.awt.image.BufferedImage;
//import java.io.ByteArrayInputStream;
//import java.io.File;
//
//public class SnapModele  implements NetSDKLib.fSnapRev{
//    BufferedImage bufferedImage = null;
//
//    String ip ;
//
//    @Override
//    public boolean invoke(NetSDKLib.LLong lLoginID, Pointer pBuf, int RevLen, int EncodeType, int CmdSerial, Pointer dwUser) {
//        if (pBuf != null && RevLen > 0) {
//            String strFileName = FileUtils.getSaveCapturePath(ip);
//            byte[] buf = pBuf.getByteArray(0, RevLen);
//            ByteArrayInputStream byteArrInput = new ByteArrayInputStream(buf);
//            try {
//                bufferedImage = ImageIO.read(byteArrInput);
//                if(bufferedImage == null) {
//                    System.err.println("生成图片失败");
//                    return false;
//                }
//                ImageIO.write(bufferedImage, "jpg", new File(strFileName));
//                System.out.println("生成图片成功");
//            } catch (Exception e) {
//                e.printStackTrace();
//                return false;
//            }
//        }
//        return true;
//    }
//
//
//}
