package jp.gr.java_conf.choplin_j.imanani;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "pwr.db";
    private static final int DB_VERSION = 2;

    public DBOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createTableSql(Task.TABLE_NAME, Task.COLUMNS));
        db.execSQL(createTableSql(TaskRecord.TABLE_NAME, TaskRecord.COLUMNS));
        db.execSQL(createTableSql(WorkRecord.TABLE_NAME, WorkRecord.COLUMNS));
        db.execSQL(createTableSql(DailyWorkSummary.TABLE_NAME, DailyWorkSummary.COLUMNS));
        db.execSQL(createTableSql(DailyTaskSummary.TABLE_NAME, DailyTaskSummary.COLUMNS));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + Task.TABLE_NAME);
        db.execSQL("drop table if exists " + TaskRecord.TABLE_NAME);
        db.execSQL("drop table if exists " + WorkRecord.TABLE_NAME);
        db.execSQL("drop table if exists " + DailyWorkSummary.TABLE_NAME);
        db.execSQL("drop table if exists " + DailyTaskSummary.TABLE_NAME);
        onCreate(db);
    }

    private String createTableSql(String table, String... columns) {
        return String.format(
                "CREATE TABLE %s (_id INTEGER PRIMARY KEY AUTOINCREMENT, %s);",
                table, join(columns));
    }

    private String join(String[] args) {
        StringBuilder sb = new StringBuilder(args[0]);
        for (int i = 1; i < args.length; i++) {
            sb.append(", ");
            sb.append(args[i]);
        }
        return sb.toString();
    }
}
