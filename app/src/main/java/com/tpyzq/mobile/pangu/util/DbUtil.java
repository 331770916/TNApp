package com.tpyzq.mobile.pangu.util;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;

import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.db.MyDbHelper;

/**
 * 数据库工具类
 */
public class DbUtil {

    private static DbUtil dbUtil = null;
    private volatile SQLiteDatabase myDb = null;
    private static String DB_FILE_PATH;
    private static String APP_DATA_PATH;
    private static MyDbHelper sDbHelper;

    public static DbUtil getInstance() {
        if (dbUtil == null) {
            dbUtil = new DbUtil();
        }
        return dbUtil;
    }


    /**
     * @return the SQLiteDatabase instance to the database of Wacai.
     */
    public synchronized static SQLiteDatabase getDB() {
        if (sDbHelper == null){
            sDbHelper = new MyDbHelper(CustomApplication.getContext());
        }
        return sDbHelper.getDb();
    }

    /**
     * 打开数据库
     * @return SQLiteDatabase.openDatabase(SqliteDataFilePath, null,
            SQLiteDatabase.OPEN_READWRITE);
     */


    /**
     * It initialize the framework, including prepare/upgrade the database and so on.
     * Please call once after launch the application.
     * And must call it in a independent thread, to avoid it block the main thread,
     * and it's also suggested to display a progress bar to prompt the user to wait for
     * the finish the initialization.
     * @return True if initialization finished successfully, else return false.
     */

    /**
     * 得到数据库文件地址
     *
     * @return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).applicationInfo.dataDir + "/" + mydb.db
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
     *
     * @return String path = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).applicationInfo.dataDir;
     * @throws
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
