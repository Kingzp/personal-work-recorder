package chokoapp.imanani;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import android.content.res.AssetManager;
import android.text.TextUtils;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "pwr.db";
    private static final int DB_VERSION = 2;

    private static Context context;

    public DBOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        DBOpenHelper.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createTableSql(Task.TABLE_NAME, Task.COLUMNS));
        db.execSQL(createTableSql(TaskRecord.TABLE_NAME, TaskRecord.COLUMNS));
        db.execSQL(createTableSql(WorkRecord.TABLE_NAME, WorkRecord.COLUMNS));
        db.execSQL(createTableSql(DailyWorkSummary.TABLE_NAME, DailyWorkSummary.COLUMNS));
        db.execSQL(createTableSql(DailyTaskSummary.TABLE_NAME, DailyTaskSummary.COLUMNS));

        insertDemoData(db);
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

    private static void insertDemoData(SQLiteDatabase db) {
        AssetManager manager = context.getResources().getAssets();
        try {
            insertTasks(db, manager);
            insertWorkRecords(db, manager);
            insertTaskRecords(db, manager);
            insertWorkSummary(db, manager);
            insertTaskSummary(db, manager);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void insertTasks(SQLiteDatabase db, AssetManager manager)
        throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(manager.open("tasks.txt")));
        String line;
        while ((line = br.readLine()) != null) {
            String [] task = TextUtils.split(line, ",");
            if (task.length == 3) {
                db.execSQL(
                    "INSERT INTO tasks (_id, code, description) VALUES (?, ?, ?)",
                    TextUtils.split(line, ","));
            }
        }
    }

    private static void insertWorkRecords(SQLiteDatabase db, AssetManager manager)
        throws IOException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss z");
        BufferedReader br = new BufferedReader(new InputStreamReader(manager.open("work_records.txt")));
        String line;
        while ((line = br.readLine()) != null) {
            String[] work = TextUtils.split(line, ",");
            if (work.length == 3) {
                try {
                    db.execSQL(
                        "INSERT INTO work_records (_id, start_time, end_time) VALUES (?, ?, ?)",
                        new String[] {
                            work[0],
                            String.valueOf(df.parse(work[1]).getTime()),
                            String.valueOf(df.parse(work[2]).getTime())
                        }
                    );
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private static void insertTaskRecords(SQLiteDatabase db, AssetManager manager)
        throws IOException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss z");
        BufferedReader br = new BufferedReader(new InputStreamReader(manager.open("task_records.txt")));
        String line;
        while ((line = br.readLine()) != null) {
            String[] task = TextUtils.split(line, ",");
            if (task.length == 5) {
                try {
                    db.execSQL(
                        "INSERT INTO task_records (_id, work_id, start_time, code, description) " +
                        "VALUES (?, ?, ?, ?, ?)",
                        new String[] {
                            task[0],
                            task[1],
                            String.valueOf(df.parse(task[2]).getTime()),
                            task[3],
                            task[4]
                        }
                    );
                } catch (ParseException e) {}
            }
        }
    }
    private static void insertWorkSummary(SQLiteDatabase db, AssetManager manager)
        throws IOException{
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss z");
        BufferedReader br = new BufferedReader(new InputStreamReader(manager.open("daily_work_summary.txt")));
        String line;
        while ((line = br.readLine()) != null) {
            String[] work = TextUtils.split(line, ",");
            if (work.length == 3) {
                try {
                    db.execSQL(
                        "INSERT INTO daily_work_summary (_id, start_at, end_at) " +
                        "VALUES (?, ?, ?)",
                        new String[] {work[0],
                                      String.valueOf(df.parse(work[1]).getTime()),
                                      String.valueOf(df.parse(work[2]).getTime())
                        }
                    );
                } catch (ParseException e) {}
            }
        }
    }

    private static void insertTaskSummary(SQLiteDatabase db, AssetManager manager)
        throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(manager.open("daily_task_summary.txt")));
        String line;
        while ((line = br.readLine()) != null) {
            String[] task = TextUtils.split(line, ",");
            if (task.length == 5) {
                try {
                    db.execSQL(
                        "INSERT INTO daily_task_summary " +
                        "(_id, code, description, duration, daily_work_summary_id) " +
                        "VALUES (?, ?, ?, ?, ?)",
                        new String[] {
                            task[0],
                            task[1],
                            task[2],
                            String.valueOf(Integer.parseInt(task[3].replaceAll("m$", "")) * 60 * 1000),
                            task[4]
                        }
                    );
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
