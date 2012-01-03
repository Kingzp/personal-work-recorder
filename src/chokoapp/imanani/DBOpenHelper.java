package chokoapp.imanani;

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
        db.execSQL(createTableSql("tasks", "code TEXT", "description TEXT"));
        db.execSQL(createTableSql("task_records", "work_id INTEGER",
                "start_time INTEGER", "code TEXT", "description TEXT"));
        db.execSQL(createTableSql("work_records", "start_time INTEGER",
                "end_time INTEGER"));
        db.execSQL(createTableSql(DailyWorkSummary.TABLE_NAME, DailyWorkSummary.COLUMNS));
        db.execSQL(createTableSql(DailyTaskSummary.TABLE_NAME, DailyTaskSummary.COLUMNS));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists tasks");
        db.execSQL("drop table if exists task_records");
        db.execSQL("drop table if exists work_records");
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
