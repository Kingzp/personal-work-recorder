package chokoapp.imanani;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

public class DBOpenHelper extends SQLiteOpenHelper {
    private Context context;
    private static final String DB_NAME = "pwr.db";
    private static final int DB_VERSION = 2;

    public DBOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createTableSql("tasks", "code TEXT", "description TEXT"));
        db.execSQL(createTableSql("task_records", "work_id INTEGER",
                "start_time INTEGER", "code TEXT", "description TEXT"));
        db.execSQL(createTableSql("work_records", "start_time INTEGER",
                "end_time INTEGER"));
        loadSampleTask(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists tasks");
        db.execSQL("drop table if exists task_records");
        db.execSQL("drop table if exists work_records");
        onCreate(db);
    }

    private void loadSampleTask(SQLiteDatabase db) {
        try {
            InputStream is = context.getAssets().open("sample_tasks.csv");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            SQLiteStatement insert_statement = db.compileStatement(Task
                    .insert_sql());
            db.beginTransaction();
            try {
                while ((line = br.readLine()) != null) {
                    insert_statement.bindString(1, line.split(",")[0]);
                    insert_statement.bindString(2, line.split(",")[1]);
                    insert_statement.execute();
                }
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
