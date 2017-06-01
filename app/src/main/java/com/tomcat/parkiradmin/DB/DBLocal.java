package com.tomcat.parkiradmin.DB;
/*
    copyright
        - Albert Alfrianta @TOMCAT INC
    June 2015
    Bogor, Indonesia

*/
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBLocal extends SQLiteOpenHelper {

    public static final int dbVersion = 1;
    public static final String dbName = "parkir";
    public static final String tblParkir = "parkir";

    //parkir column name
    public static final String columnId = "id";
    public static final String columnLatitude = "latitude";
    public static final String columnLongitude = "longitude";
    public static final String columnName = "name";
    public static final String columnAddress = "address";
    public static final String columnPrice = "price";


    public DBLocal(Context context) {
        super(context, dbName, null, dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
    }

    public Cursor getListParkir(){
        Log.d("Dblocal getListParkir", "");
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "SELECT * FROM " + tblParkir + ";";
            Cursor res = db.rawQuery(query, null);
            Log.d("dbL getListParkir", "success");
            return res;
            //db.close();
        } catch (Exception e){
            Log.e("dbL getListParkir", e.toString());
            return null;
        }
    }
/*
    //##for new user login
    public boolean searchUser(String id){
        boolean exist=false;
        Log.i("dbL searchUser", "1");
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "SELECT " +uId+ " " +
                    "FROM " +tblUsers+ " " +
                    "WHERE " +uId+ " = '" +id+ "' " +
                    ";";
            Cursor res = db.rawQuery(query, null);
            Log.i("dbL searchFriend", "success");
            if(res!=null)
                exist= true;
        } catch (Exception e){
            Log.e("dbL searchFriend failed", e.toString());
            exist=false;
        }
        return exist;
    }

    public boolean query(String sql){
        Log.d("Dblocal query", "");
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            db.execSQL(sql);
            Log.d("dbL query", "success");
            return true;
        } catch (Exception e){
            Log.e("dbL query failed", e.toString());
            return false;
        }
    }

    public String gNew(String category){
        Log.d("Dblocal gNew", "");
        String date=null;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "SELECT " +category+ " FROM " + tblNew + ";";
            Cursor res = db.rawQuery(query, null);
            if(res!=null) {
                res.moveToFirst();
                date=res.getString(0);
            }
            Log.d("dbL gNew", "success");
            return date;
        } catch (Exception e){
            Log.e("dbL gNew failed", e.toString());
            return null;
        }
    }

    public Cursor gNewAll(){
        Log.d("Dblocal gNewAll", "");
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "SELECT * FROM " + tblNew + ";";
            Cursor res = db.rawQuery(query, null);
            Log.d("dbL gNewAll", "success");
            return res;
            //db.close();
        } catch (Exception e){
            Log.e("dbL gNewAll failed", e.toString());
            return null;
        }
    }
    public String gNewChat(String date){    //getNewChatUser. Untuk memeriksa apakah ada data baru untuk pesan masuk
        String dateNew;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "SELECT " + nChatUser1 + " FROM " + tblNew + ";";
            Cursor res = db.rawQuery(query, null);
            if (res.getCount() != 0) {
                res.moveToFirst();
                dateNew=res.getString(0);
                if (dateNew.equals(date)) {     //jika date baru=date yang terakhir dilihat. Berarti belum ada databaru
                    Log.i("DBLocal gNChatUser2", "success");
                    dateNew=null;   //return null
                }
            }
            if (!res.isClosed())
                res.close();
            //db.close();
            Log.i("dbL gNChatUser2", "success");
        } catch (Exception e){
            Log.e("dbL gNChatUser2 failed", e.toString());
            date=null;
        }
        return date;
    }

    public boolean uNew(String category, String date){
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            ContentValues values= new ContentValues();
            values.put(category, date);
            db.update(tblNew, values, null, null);
            Log.d("DBLocal uNew", category + " " + date);
            return true;
        } catch (Exception e){
            Log.e("dbL uNew failed", e.toString());
            return false;
        }
    }

    public boolean uNew2(String date){
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            ContentValues values= new ContentValues();
            values.put(nChatUser2, date);
            db.update(tblNew, values, null, null);

            Log.i("dbL uNew2", "success");
            return true;
        } catch (Exception e){
            Log.e("dbL uNew2 failed", e.toString());
            return false;
        }
    }

    */
}
