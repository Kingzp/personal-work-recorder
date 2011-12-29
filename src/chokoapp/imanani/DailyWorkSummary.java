package chokoapp.imanani;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DailyWorkSummary {
    public static final String TABLE_NAME = "daily_work_summary";
    public static final String[] COLUMNS = {
        "start_at INTEGER",
        "end_at INTEGER"
    };

    public static Cursor findByDate(SQLiteDatabase db, long date) {
        return db.query(TABLE_NAME,
                        new String[] { "start_at", "end_at" },
                        "date(start_at / 1000, 'unixepoch', 'localtime') = ?",
                        new String[] { (new SimpleDateFormat("yyyy-MM-dd")).format(new Date(date)) },
                        null, null, null);
    }

    public static void setEndTime(SQLiteDatabase db, long date, long endTime) {
        ContentValues val = new ContentValues();
        val.put("end_at", endTime);
        db.update(TABLE_NAME,
                  val,
                  "date(start_at / 1000, 'unixepoch', 'localtime') = ?",
                  new String[] { (new SimpleDateFormat("yyyy-MM-dd")).format(new Date(date)) });
    }

    public static void setStartTime(SQLiteDatabase db, long start) {
        ContentValues val = new ContentValues();
        val.put("start_at", start);
        db.insert(TABLE_NAME, null, val);
    }

    public static void setStartEndTime(SQLiteDatabase db, long start, long end) {
        ContentValues val = new ContentValues();
        val.put("start_at", start);
        val.put("end_at", end);
        db.insert(TABLE_NAME, null, val);
    }

}