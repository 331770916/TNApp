package com.tpyzq.mobile.pangu.db.util;

import java.io.Closeable;
import java.io.IOException;

/**
 * **************************************************************
 * <p>
 * **************************************************************
 * Authors:huweidong on 2017/6/22 0022 11:06
 * Email：huwwds@gmail.com
 */
public class CloseUtils {
    public static void close(Closeable... closeables) {
        if (closeables==null)return;
        for (Closeable closeable : closeables) {
            if (closeable!=null){
                try {
                    closeable.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
