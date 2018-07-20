package com.youngpatola.bplogger.data.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.youngpatola.bplogger.data.BPLog;

import org.joda.time.DateTime;

import java.io.FileWriter;
import java.util.List;

/**
 * Created by youngpatola on 04/03/2018.
 */

public class SqlController {
    private static final String TAG = "SqlController";
    private Context context;
    private SqlDbHelper sqlDbHelper;

    public SqlController(Context context) {
        this.context = context;
        sqlDbHelper = new SqlDbHelper(context);
    }

    public boolean saveData(BPLog bpLog) {
        ContentValues values = new ContentValues();
        values.put(SqlTable.COL_DATE, bpLog.getDatetime());
        values.put(SqlTable.COL_SYS, bpLog.getSystolic());
        values.put(SqlTable.COL_DIA, bpLog.getDiastolic());
        values.put(SqlTable.COL_HR, bpLog.getHeartRate());
        values.put(SqlTable.COL_INDI, bpLog.getIndicator());
        values.put(SqlTable.COL_FEEL, bpLog.getFeel());
        values.put(SqlTable.COL_POS, bpLog.getPosition());
        values.put(SqlTable.COL_ARM, bpLog.getArm());
        values.put(SqlTable.COL_NOTE, bpLog.getNote());
        return sqlDbHelper.insertLog(SqlTable.TABLE_NAME, values);
    }

    public int updateData(BPLog bpLog) {
        ContentValues values = new ContentValues();
        values.put(SqlTable.COL_DATE, bpLog.getDatetime());
        values.put(SqlTable.COL_SYS, bpLog.getSystolic());
        values.put(SqlTable.COL_DIA, bpLog.getDiastolic());
        values.put(SqlTable.COL_HR, bpLog.getHeartRate());
        values.put(SqlTable.COL_INDI, bpLog.getIndicator());
        values.put(SqlTable.COL_FEEL, bpLog.getFeel());
        values.put(SqlTable.COL_POS, bpLog.getPosition());
        values.put(SqlTable.COL_ARM, bpLog.getArm());
        values.put(SqlTable.COL_NOTE, bpLog.getNote());
        String id = String.valueOf(bpLog.getId());
        Log.d(TAG, "updateData: id(" + id + "), raw get id(" + bpLog.getId() + ")" );
        return sqlDbHelper.updateLog(SqlTable.TABLE_NAME, values, id);
    }

    public BPLog getData(String id) {
        return sqlDbHelper.getLog(id);
    }

    public boolean deleteData(String id) {
        return sqlDbHelper.deleteLog(id);
    }

    public List<BPLog> getList() {
        return sqlDbHelper.getAllLogs();
    }

    public void getFileForCSV(String fileName, ExportToCSVCallback callback) {
        FileWriter fileWriter;
        SQLiteDatabase ref =  sqlDbHelper.getReadableDatabase();
        String queryAll = "SELECT * FROM " + SqlTable.TABLE_NAME + " ORDER BY " + SqlTable.COL_DATE + " DESC ";
        Cursor cursor = ref.rawQuery(queryAll, null);
        try {
            fileWriter = new FileWriter(fileName);
            if (cursor != null) {
                cursor.moveToFirst();
                for (int i = 0; i < cursor.getCount(); i++) {
                    for (int j = 0; j < cursor.getColumnNames().length; j++) {
                        // Too hacky and specific, but this if conditional just checks if j
                        // is the second column (Cursor is base zero). In my database, the
                        // second column is the date time. So if it is, the function
                        // 'formattedDate' converts it to a more readable text.
                        if (j == 1) {
                            Log.d(TAG, "\ngetFileForCSV: "+ cursor.getPosition() + " (" +cursor.getString(j) + ")");
                            String date = formattedDate(Long.parseLong(cursor.getString(j)));
                            fileWriter.append(date).append(";");
                        } else {
                            fileWriter.append(cursor.getString(j)).append(";");
                        }
                    }
                    fileWriter.append("\n");
                    cursor.moveToNext();
                }
                cursor.close();
                ref.close();
            }
            fileWriter.close();
            callback.csvCreatedSuccess(cursor!=null);
        }catch (Exception e) {
            callback.csvCreatedFailure(e);
        }
    }

    private String formattedDate(long date) {
        DateTime epochToDate = new DateTime(date * 1000);
        return epochToDate.toString("MMM d h:mm a");
    }

    public interface ExportToCSVCallback {
        void csvCreatedSuccess(boolean isNotEmpty);
        void csvCreatedFailure(Exception e);
    }

}
