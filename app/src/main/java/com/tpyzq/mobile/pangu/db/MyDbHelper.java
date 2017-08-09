package com.tpyzq.mobile.pangu.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.tpyzq.mobile.pangu.data.UserEntity;
import com.tpyzq.mobile.pangu.db.util.CursorUtils;
import com.tpyzq.mobile.pangu.log.capture.CrashHandler;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.keyboard.KeyEncryptionUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static com.tpyzq.mobile.pangu.db.Db_PUB_USERS.entity;

/**
 *
 */

public class MyDbHelper extends SQLiteOpenHelper {

    private static int version = 5;
    private static final String TAG = "MyDbHelper";
    private Context mContext;
    private static SQLiteDatabase sqLiteDatabase;

    public MyDbHelper(Context context) {
//        super(context, Environment.getExternalStorageDirectory()+"/"+ConstantUtil.DB_FILE_NAME, null, version);
        super(context, ConstantUtil.DB_FILE_NAME, null, version);
        mContext = context;
    }

    public SQLiteDatabase getDb(){
        if (sqLiteDatabase==null)
            sqLiteDatabase=getWritableDatabase();
        return sqLiteDatabase;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        sqLiteDatabase=db;
        executeAssetsSQL(db, "sql/crebas2.sql");
        initOriginData(db);
        compatiblePreDb();
    }

    private void compatiblePreDb() {
        try {
            String packageName = mContext.getPackageName();
            if (!new File("/data/data/"+packageName+"/mydb.db").exists()){
                return;
            }
            SQLiteDatabase db = SQLiteDatabase.openDatabase("/data/data/"+packageName+"/mydb.db", null, SQLiteDatabase.OPEN_READWRITE);
            Cursor cursor = db.rawQuery("select * from PUB_USERS where USERID=1", null);

            if (cursor != null && cursor.moveToNext()) {
                entity.setUserId(CursorUtils.getString(cursor, "USERID"));
                entity.setUsername(CursorUtils.getString(cursor, "USERNAME"));
                entity.setPickname(CursorUtils.getString(cursor, "PICKNAME"));
                entity.setMobile(KeyEncryptionUtils.localDecryptMobile(CursorUtils.getString(cursor, "MOBILE")));
                entity.setPlugins(CursorUtils.getString(cursor, "PIUGINS"));
                entity.setRefreshTime(CursorUtils.getString(cursor, "EXCOLUMN00"));
                entity.setScno(CursorUtils.getString(cursor, "REGISTERID"));
                entity.setScno(CursorUtils.getString(cursor, "SCNO"));
                entity.setKeyboard(CursorUtils.getString(cursor, "KEYBOARD"));
                entity.setCertification(CursorUtils.getString(cursor, "CERTIFICATION"));

                //存资金账号
                List<UserEntity> list=new ArrayList<>();
                UserEntity userEntity = new UserEntity();
                userEntity.setTradescno(CursorUtils.getString(cursor, "TRADESCNO"));
                list.add(userEntity);
                List<UserEntity> userEntities = KeyEncryptionUtils.localDecryptTradescno(list);
                if (userEntities!=null&&userEntities.size()>0&&userEntities.get(0)!=null)
                    entity.setTradescno(userEntities.get(0).getTradescno());

                entity.setUsertype(CursorUtils.getString(cursor, "EXCOLUMN01"));
            }
            if (cursor!=null)
                cursor.close();

            Db_PUB_USERS.updateUserById(entity.getUserId());
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void initOriginData(SQLiteDatabase db) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", 1);
        db.insert("USER", null, contentValues);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
                Log.e(TAG, "v1-v5 update");//执行sql/update/v1_v5.sql
//                executeAssetsSQL(db,"sql/update/v1_v5.sql");
                break;
            case 3:
                Log.e(TAG, "v3-v5 update");
                break;
            case 4:
                Log.e(TAG, "v4-v5 update");
                break;
        }
    }

    private void executeAssetsSQL(SQLiteDatabase db, String schemaName) {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(mContext.getAssets()
                    .open(schemaName)));

            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            String[] sqls = sb.toString().split(";");
            for (String sql : sqls) {
                db.execSQL(sql);
            }
        } catch (IOException e) {
            e.printStackTrace();
            CrashHandler.getInstance(mContext).handleException(e);
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (IOException e) {
                e.printStackTrace();
                CrashHandler.getInstance(mContext).handleException(e);
            }
        }
    }

}
