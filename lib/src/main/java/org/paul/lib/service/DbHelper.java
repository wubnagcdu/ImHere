package org.paul.lib.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import org.paul.lib.bean.DomainBean;

public class DbHelper extends SQLiteOpenHelper {


    public DbHelper(Context context) {
        super(context, DBNAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(tableSql);
//        database = getWritableDatabase();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private static final String DBNAME = "huawei_dns";//数据库名
    private static final int VERSION = 1;//数据库版本号
    private String tableName = "ips_info";//表名
    private String tableSql = "CREATE TABLE " + tableName + " (id INTEGER PRIMARY KEY AUTOINCREMENT,domainName TEXT," +
            "ips TEXT,ttl INT,queryTime TIMESTAMP,UNIQUE(domainName))";// 建表语句
    private SQLiteDatabase database;//数据库引用

    private SQLiteDatabase getDatabase(){
        if(null==database){
            database=getWritableDatabase();
        }
        return database;
    }

    public void update(DomainBean domainBean) {
        SQLiteDatabase database = getDatabase();
        database.beginTransaction();
        try {
            //插入（更新）一条数据
            ContentValues contentValues = domainBean.toContentValues();
            database.replace(tableName, null, contentValues);
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

    public DomainBean query(String domain) {
        SQLiteDatabase database = getDatabase();
        database.beginTransaction();
        DomainBean domainBean = null;
        try {
            Cursor query = database.query(tableName, null, "domainName=?", new String[]{domain}, null, null, null);
            if (query.moveToFirst()) {
                String domainName = query.getString(1);
                String[] ips = query.getString(2).split(",");
                long ttl = query.getLong(3);
                long queryTime = query.getLong(4);
                domainBean = new DomainBean(domainName, ips, ttl, queryTime);
            }
            query.close();
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
            return domainBean;
        }
    }

    public void delete(String domain) {
        SQLiteDatabase database = getDatabase();
        database.beginTransaction();
        try {
            database.delete(tableName, "domainName=?", new String[]{domain});
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

    public void clear() {
        SQLiteDatabase database = getDatabase();
        database.beginTransaction();
        try {
            database.delete(tableName, null, null);
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

}
