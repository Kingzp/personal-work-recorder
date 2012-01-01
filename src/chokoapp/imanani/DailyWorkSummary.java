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

    private long _id;
    private long start_at;
    private long end_at;

    public DailyWorkSummary() {
        this._id = this.start_at = this.end_at = 0;
    }
    public DailyWorkSummary(long _id, long start_at, long end_at) {
        this._id = _id;
        this.start_at = start_at;
        this.end_at = end_at;
    }

    public long getId() { return _id; }
    public long getStartAt() { return start_at; }
    public long getEndAt() { return end_at; }

    public boolean nowRecording() { return end_at == 0; }
    public boolean existInDatabase() { return _id != 0; }
    public boolean isEmpty() { return _id == 0 && start_at == 0 && end_at == 0; }

    public static DailyWorkSummary findByDate(SQLiteDatabase db, long date) {
        Cursor daily_work_summary_cursor =
            db.query(TABLE_NAME,
                     new String[] { "_id", "start_at", "end_at" },
                     "date(start_at / 1000, 'unixepoch', 'localtime') = ?",
                     new String[] { (new SimpleDateFormat("yyyy-MM-dd")).format(new Date(date)) },
                     null, null, null);
        try {
            if ( daily_work_summary_cursor.moveToFirst() ) {
                if ( daily_work_summary_cursor.isNull(2) ) {
                    return new DailyWorkSummary(daily_work_summary_cursor.getLong(0),
                                                daily_work_summary_cursor.getLong(1),
                                                0);
                } else {
                    return new DailyWorkSummary(daily_work_summary_cursor.getLong(0),
                                                daily_work_summary_cursor.getLong(1),
                                                daily_work_summary_cursor.getLong(2));
                }
            } else {
                return new DailyWorkSummary();
            }
        } finally {
            daily_work_summary_cursor.close();
        }
    }

    public static void setEndTime(SQLiteDatabase db, long date, long endTime) {
        ContentValues val = new ContentValues();
        val.put("end_at", endTime);
        db.update(TABLE_NAME,
                  val,
                  "date(start_at / 1000, 'unixepoch', 'localtime') = ?",
                  new String[] { (new SimpleDateFormat("yyyy-MM-dd")).format(new Date(date)) });
    }

    public static void setEndTimeNull(SQLiteDatabase db, long date) {
        ContentValues val = new ContentValues();
        val.putNull("end_at");
        db.update(TABLE_NAME,
                  val,
                  "date(start_at / 1000, 'unixepoch', 'localtime') = ?",
                  new String[] { (new SimpleDateFormat("yyyy-MM-dd")).format(new Date(date)) });
    }

    public static long setStartTime(SQLiteDatabase db, long start) {
        ContentValues val = new ContentValues();
        val.put("start_at", start);
        return db.insert(TABLE_NAME, null, val);
    }

    public static long setStartEndTime(SQLiteDatabase db, long start, long end) {
        ContentValues val = new ContentValues();
        val.put("start_at", start);
        val.put("end_at", end);
        return db.insert(TABLE_NAME, null, val);
    }

}