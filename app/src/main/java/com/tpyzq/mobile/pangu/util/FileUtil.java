package com.tpyzq.mobile.pangu.util;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * Created by longfeng on 2017/3/1.
 * 文件操作工具类
 */

public class FileUtil {

    /**
     * 创建文件夹
     *
     * @param dirName
     * @throws IOException
     */
    public static void createFolder(String dirName) throws IOException {
        // 判断sd卡是否存在
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File dir = new File(dirName);
            // 判断文件夹是否存在，不存在则创建
            if (!dir.exists()) {
                dir.mkdir();
            }
        }
    }

    /**
     * 删除文件夹
     *
     * @param file
     */
    public static void deleteFile(File file) throws IOException {
        if (file.exists()) { // 判断文件是否存在
            if (file.isFile()) { // 判断是否是文件
                file.delete();                      //  删除文件
            } else if (file.isDirectory()) {            // 判断是否是文件夹
                File files[] = file.listFiles();         // 获取文件夹下所有文件
                for (int i = 0; i < files.length; i++) {        // 遍历文件夹下所有文件
                    deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
                }
            }
            file.delete();
        } else {
        }
    }
    private static int NEGATIVE_ONE = -1;
    /**
     * Get MD5 of one file:hex string,test OK!
     *
     * @param file
     * @return
     */
    public static String getFileMD5(File file) {
        if (!file.exists() || !file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != NEGATIVE_ONE) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());
        return bigInt.toString(16);
    }

    /***
     * @param filepath
     * @return
     */
    public static String getFileMD5(String filepath) {
        File file = new File(filepath);
        return getFileMD5(file);
    }

}
