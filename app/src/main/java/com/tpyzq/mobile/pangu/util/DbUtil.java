package com.tpyzq.mobile.pangu.util;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;


import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.db.DataBaseContext;
import com.tpyzq.mobile.pangu.db.MyDbHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * 数据库工具类
 */
public class DbUtil {

    private static DbUtil dbUtil = null;
    private static final String LOG_TAG         = "DbUtil";
    private SQLiteDatabase myDb = null;
    private boolean        mIsInited;
    private static String  DB_FILE_PATH;
    private static String  APP_DATA_PATH;

    public static DbUtil getInstance() {
        if (dbUtil == null) {
            dbUtil = new DbUtil();
        }
        return dbUtil;
    }

    /**
     * @return true if it's inited already, else false.
     */
    public synchronized boolean isInited() {
        return mIsInited;
    }

    /**
     * @return the SQLiteDatabase instance to the database of Wacai.
     */
    public synchronized SQLiteDatabase getDB() {
        if (!mIsInited) {
            init();
        }

        return openDB();
    }

    /**
     * 打开数据库
     * @return SQLiteDatabase.openDatabase(SqliteDataFilePath, null,
            SQLiteDatabase.OPEN_READWRITE);
     */

    private SQLiteDatabase openDB() {
        if (null == myDb || !myDb.isOpen()) {
            try {
                myDb = SQLiteDatabase.openDatabase(getDatabasePath(), null,SQLiteDatabase.OPEN_READWRITE);
            } catch (Exception e) {
            }
        }

        return myDb;
    }


    /**
     * It initialize the framework, including prepare/upgrade the database and so on.
     * Please call once after launch the application.
     * And must call it in a independent thread, to avoid it block the main thread,
     * and it's also suggested to display a progress bar to prompt the user to wait for
     * the finish the initialization.
     * @return True if initialization finished successfully, else return false.
     */
    public synchronized boolean init() {
        if (mIsInited)
            return true;


        File dbFile = new File(getDatabasePath());
        if (!dbFile.exists()) {
            if (!generateDBFile(false)) {
                mIsInited = false;
                return false;
            }
        } else {

            DataBaseContext context = new DataBaseContext(CustomApplication.getContext());
            MyDbHelper dbHelper = new MyDbHelper(context);
            try {
                myDb = dbHelper.getWritableDatabase();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        mIsInited = true;

        return true;
    }

    /**
     * 从Asseass文件夹中读取数据库并copy到sdcard下
     * @param isRemoveOldDB
     * @return
     */
    private boolean generateDBFile(boolean isRemoveOldDB) {
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            //First close the SQLite database first.
            if (myDb != null && myDb.isOpen() ) {
                myDb.close();
                myDb = null;
            }

            //Remove the old file if exist.
            String strDBPath = getDatabasePath();
            File file = new File(strDBPath);
            if (file.exists()) {
                if (isRemoveOldDB) {
                    file.delete();
                } else {
                    return false;
                }
            }

            //Generate a new DB file from the asserts.
            AssetManager am = CustomApplication.getContext().getAssets();
            is = am.open(ConstantUtil.DB_FILE_NAME);
            fos = new FileOutputStream(file);
            byte buf[] = new byte[1024];
            int readLen = 0;
            while (readLen != -1) {
                readLen = is.read(buf, 0, 1024);
                if (readLen > 0) {
                    fos.write(buf, 0, readLen);
                }
            }
            return true;
        } catch(Exception e) {
            return false;
        } finally {
            try {
                if(null != fos)
                    fos.close();
                if(null != is)
                    is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 得到数据库文件地址
     * @return  context.getPackageManager().getPackageInfo(context.getPackageName(), 0).applicationInfo.dataDir + "/" + mydb.db
     */
    public static String getDatabasePath() {
        if (DB_FILE_PATH == null) {
            StringBuilder sb = new StringBuilder(getDataPath());
            sb.append("/");
            sb.append(ConstantUtil.DB_FILE_NAME);
            DB_FILE_PATH = sb.toString();
        }

        return DB_FILE_PATH;
    }

    /**
     * 通过包信息得到以包的文件夹地址作为地址
     * @exception
     * @return String path = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).applicationInfo.dataDir;
     */
    public static String getDataPath() {
        if (APP_DATA_PATH != null)
            return APP_DATA_PATH;

        PackageManager pm = CustomApplication.getContext().getPackageManager();
        String strPackName = CustomApplication.getContext().getPackageName();
        PackageInfo p = null;
        try {
            p = pm.getPackageInfo(strPackName, 0);
            APP_DATA_PATH = p.applicationInfo.dataDir;
            return APP_DATA_PATH;
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

}
