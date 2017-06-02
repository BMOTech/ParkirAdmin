package com.tomcat.parkiradmin.DB;
/*
	copyright Albert Alfrianta @TOMCAT INC
	June 2015
	Bogor, Indonesia
*/
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBCreate extends SQLiteOpenHelper {
    Context context;

    private static final String createTblParkir=
            "CREATE TABLE "+DBLocal.tblParkir+" ( " +
                    DBLocal.columnId+" TEXT PRIMARY KEY, " +
                    DBLocal.columnLatitude+" DECIMAL(10,8), " +
                    DBLocal.columnLongitude+" DECIMAL(11,8), " +
                    DBLocal.columnName+" TEXT, " +
                    DBLocal.columnAddress+" TEXT, " +
                    DBLocal.columnPrice+" TEXT" +
                    ");";

    public DBCreate(Context context) {
        super(context, DBLocal.dbName, null, DBLocal.dbVersion);
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + DBLocal.tblParkir);
        onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createTblParkir);
        Log.d("DBLocal oncreate", "1");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        context.deleteDatabase(DBLocal.dbName);
        onCreate(db);
        // TODO Auto-generated method stub
        //db.execSQL("DROP TABLE IF EXISTS " + tblName);
        //onCreate(db);
    }
}
