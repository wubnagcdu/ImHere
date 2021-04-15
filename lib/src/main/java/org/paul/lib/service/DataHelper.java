package org.paul.lib.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import org.paul.lib.bean.DomainBean;

public class DataHelper extends SQLiteOpenHelper {

    private static final String DATA_BASE_NAME= "huawei_dns";
    private static final String TABLE="ips_info";
    private static final String CREATE_TABLE=
            "create table "+ TABLE +" (id integer primary key autoincrement, domainName varchar(225),"+
            "ips varchar, ttl int,queryTime timestamp,UNIQUE(domainName))";
    public DataHelper(Context context,int version) {
        super(context, DATA_BASE_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        if (oldVersion == 1 && newVersion == 2) {
//            db.execSQL("ALTER TABLE " + DATA_BASE_NAME + " ADD COLUMN age INTEGER DEFAULT 20");
//        }
    }
    public void update(DomainBean bean){
        SQLiteDatabase writableDatabase = getWritableDatabase();
        try {
            writableDatabase.beginTransaction();
            ContentValues contentValues=bean.getContentValues();
            writableDatabase.replace(TABLE,null,contentValues);
            writableDatabase.setTransactionSuccessful();
        }finally {
            writableDatabase.endTransaction();
        }
    }
    public void delete(String... domains){
        SQLiteDatabase writableDatabase = getWritableDatabase();
        String whereClause ="domainName=?";
        try {
            writableDatabase.beginTransaction();
            writableDatabase.delete(TABLE,whereClause,domains);
            writableDatabase.setTransactionSuccessful();
        }finally {
            writableDatabase.endTransaction();
        }
    }
    public DomainBean query(String... domains){
        SQLiteDatabase writableDatabase = getWritableDatabase();
        String selection = "domainName=?";
        String orderBy = "queryTime DESC";
        DomainBean result=null;
        try {
            writableDatabase.beginTransaction();
            Cursor cursor = writableDatabase.query(TABLE, new String[]{"id", "domainName", "ips", "ttl", "queryTime"},
                    selection,
                    domains, null, null, orderBy);
            if(cursor.moveToFirst()){
                String domainName = cursor.getString(1);
                String[] ips = cursor.getString(2).split(",");
                long ttl = cursor.getInt(3);
                long queryTime = cursor.getInt(4);
                result=new DomainBean(domainName,ips,ttl,queryTime);

            }
            cursor.close();
            writableDatabase.setTransactionSuccessful();
        }finally {
            writableDatabase.endTransaction();
            return result;
        }
    }
    public void clear(){
        SQLiteDatabase writableDatabase = getWritableDatabase();
        try {
            writableDatabase.beginTransaction();
            writableDatabase.delete(TABLE,null,null);
            writableDatabase.setTransactionSuccessful();
        }finally {
            writableDatabase.endTransaction();
        }
    }
}
