package com.youngpatola.bplogger.data.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.youngpatola.bplogger.data.BPLog;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by youngpatola on 04/03/2018.
 */

public class SqlDbHelper extends SQLiteOpenHelper {
    private static final String TAG = SqlDbHelper.class.getSimpleName();

    SqlDbHelper(Context context) {
        super(context, SqlTable.DB_NAME, null, SqlTable.DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(SqlTable.MAKE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SqlTable.TABLE_NAME);
        this.onCreate(sqLiteDatabase);
    }

    boolean insertLog(String tableName, ContentValues values) {
        SQLiteDatabase ref = this.getWritableDatabase();
        boolean wat = ref.insert(tableName, null, values) == -1;
        Log.d(TAG, "insertLog: " + wat);
        return wat;
    }

    int updateLog(String tableName, ContentValues values, String id) {
        Log.d(TAG, "updateLog: tableName(" + tableName + "), values(" + values.toString() + "), id(" + id + ")");
        SQLiteDatabase ref = this.getWritableDatabase();
        int v=  ref.update(tableName, values,
                SqlTable.COL_ID + " =? ",
                new String[] {id});
        Log.d("databae", "updateLog frm helper: " + v);
        return v;
    }

    BPLog getLog(String id) {
        Log.d(TAG, "getLog: id(" + id + ")");
        BPLog bpLog;
        SQLiteDatabase ref = this.getReadableDatabase();
        Cursor cursor = ref.query(SqlTable.TABLE_NAME, SqlTable.COLUMNS,
                "id=?", new String[]{id}, null, null, null);
        if (cursor!=null && cursor.moveToFirst()) {
            bpLog = new BPLog();
            bpLog.setId(Integer.parseInt(cursor.getString(0)));
            Log.d(TAG, "getLog: getID(" + bpLog.getId() + ")");
            bpLog.setDatetime(Long.parseLong(cursor.getString(1)));
            bpLog.setSystolic(cursor.getString(2));
            bpLog.setDiastolic(cursor.getString(3));
            bpLog.setHeartRate(cursor.getString(4));
            bpLog.setIndicator(cursor.getString(5));
            bpLog.setFeel(cursor.getString(6));
            bpLog.setPosition(cursor.getString(7));
            bpLog.setArm(cursor.getString(8));
            bpLog.setNote(cursor.getString(9));
            cursor.close();
            return bpLog;
        }
        return null;
    }

    boolean deleteLog(String id) {
        SQLiteDatabase ref = this.getWritableDatabase();
        return ref.delete(SqlTable.TABLE_NAME, "id = ?", new String[]{String.valueOf(id)}) > 0;
    }

    List<BPLog> getAllLogs(){
        List<BPLog> bpLogList = new LinkedList<BPLog>();
        SQLiteDatabase ref = this.getReadableDatabase();
        String queryAll = "SELECT * FROM " + SqlTable.TABLE_NAME + " ORDER BY " + SqlTable.COL_DATE + " DESC ";
        Cursor cursor = ref.rawQuery(queryAll, null);
        BPLog bpLog = null;
        if (cursor!=null && cursor.moveToFirst()) {
            do {
                bpLog = new BPLog();
                bpLog.setId(Integer.parseInt(cursor.getString(0)));
                bpLog.setDatetime(Long.parseLong(cursor.getString(1)));
                bpLog.setSystolic(cursor.getString(2));
                bpLog.setDiastolic(cursor.getString(3));
                bpLog.setHeartRate(cursor.getString(4));
                bpLog.setIndicator(cursor.getString(5));
                bpLog.setFeel(cursor.getString(6));
                bpLog.setPosition(cursor.getString(7));
                bpLog.setArm(cursor.getString(8));
                bpLog.setNote(cursor.getString(9));
                bpLogList.add(bpLog);
            } while (cursor.moveToNext());
        }
        if (cursor!=null ) cursor.close();
        return bpLogList;
    }

}
