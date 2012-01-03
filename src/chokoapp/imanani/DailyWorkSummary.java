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

    public void update(DateTimeView startView, DateTimeView endView) {
        if ( !startView.isEmpty() ) start_at = startView.getTime();
        if ( !endView.isEmpty() )   end_at   = endView.getTime();
    }

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

    public QueryResult save(SQLiteDatabase db) {
        if ( isEmpty() ) {
            return QueryResult.NOTHING;
        }

        if ( nowRecording() ) {
            return QueryResult.NOTHING;
        }

        ContentValues val = new ContentValues();
        val.put("start_at", start_at);
        val.put("end_at", end_at);
        if ( existInDatabase() ) {
            int num = db.update(TABLE_NAME, val, "_id = ?",
                                new String[] { String.format("%d", _id) });
            return num == 1 ? QueryResult.SUCCESS : QueryResult.FAIL;
        } else {
            long id = db.insert(TABLE_NAME, null, val);
            if ( id == -1 ) {
                return QueryResult.FAIL;
            } else {
                this._id = id;
                return QueryResult.SUCCESS;
            }
        }
    }

    public void resetFromWorkRecord(SQLiteDatabase db, long date) {
        DailyWorkSummary resetDate = WorkRecord.findByDate(db, date);
        if ( !resetDate.isEmpty() ) {
            this.start_at = resetDate.getStartAt();
            this.end_at = resetDate.getEndAt();
        }
    }
}