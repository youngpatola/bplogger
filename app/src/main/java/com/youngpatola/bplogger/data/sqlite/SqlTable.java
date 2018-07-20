package com.youngpatola.bplogger.data.sqlite;

/**
 * Created by youngpatola on 04/03/2018.
 */

public class SqlTable {
    public static final int DB_VER = 1;
    public static final String DB_NAME = "bp_db";
    public static final String TABLE_NAME = "bplogs_table";
    public static final String COL_ID = "id";
    public static final String COL_DATE = "date";
    public static final String COL_SYS = "systolic";
    public static final String COL_DIA = "diastolic";
    public static final String COL_HR = "heartrate";

    public static final String COL_INDI = "indicator";
    public static final String COL_FEEL = "feeling";
    public static final String COL_POS = "position";
    public static final String COL_ARM = "arm";
    public static final String COL_NOTE = "note";

    public static final String[] COLUMNS =  {
            COL_ID, COL_DATE, COL_SYS, COL_DIA, COL_HR,
            COL_INDI, COL_FEEL, COL_POS, COL_ARM, COL_NOTE};

    public static final String MAKE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_DATE + " TEXT, "
            + COL_SYS + " TEXT, "
            + COL_DIA + " TEXT, "
            + COL_HR + " TEXT, "
            + COL_INDI + " TEXT, "
            + COL_FEEL + " TEXT, "
            + COL_POS + " TEXT, "
            + COL_ARM + " TEXT, "
            + COL_NOTE + " TEXT) ";
}
