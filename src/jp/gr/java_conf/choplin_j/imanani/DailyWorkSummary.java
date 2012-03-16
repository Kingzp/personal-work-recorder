package jp.gr.java_conf.choplin_j.imanani;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Observable;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DailyWorkSummary extends Observable {
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
    public long getTotal() {
        if ( isEmpty() ) return 0;
        if ( nowRecording() ) return 0;
        return end_at - start_at;
    }

    public boolean nowRecording() { return end_at == 0; }
    public boolean existInDatabase() { return _id != 0; }
    public boolean isEmpty() { return _id == 0 && start_at == 0 && end_at == 0; }

    public void setStartAt(long start) {
        if ( start < 0 ) return;
        if ( this.start_at == start ) return;

        this.start_at = start;
        setChanged();
        notifyObservers();
    }
    public void setEndAt(long end) {
        if ( end < 0 ) return;
        if ( this.end_at == end ) return;

        this.end_at = end;
        setChanged();
        notifyObservers();
    }

    public void startTimeUp() {
        if ( isEmpty() ) return;
        if ( nowRecording() ) return;

        setStartAt(TimeUtils.up(getStartAt()));
    }

    public void startTimeDown() {
        if ( isEmpty() ) return;
        if ( nowRecording() ) return;

        setStartAt(TimeUtils.down(getStartAt()));
    }

    public void endTimeUp() {
        if ( isEmpty() ) return;
        if ( nowRecording() ) return;

        setEndAt(TimeUtils.up(getEndAt()));
    }

    public void endTimeDown() {
        if ( isEmpty() ) return;
        if ( nowRecording() ) return;

        setEndAt(TimeUtils.down(getEndAt()));
    }

    public void autoAdjust() {
        if ( isEmpty() ) return;
        if ( nowRecording() ) return;

        setStartAt(TimeUtils.adjustTime(getStartAt()));
        setEndAt(TimeUtils.adjustTime(getEndAt()));
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
        if ( resetDate.isEmpty() ) return;

        setStartAt(resetDate.getStartAt());
        setEndAt(resetDate.getEndAt());
    }

    public void copy(DailyWorkSummary other) {
        this._id = other._id;

        setStartAt(other.start_at);
        setEndAt(other.end_at);
    }

    public void saveToSharedPreference(Context context) {
        SharedPreferences pref =
            context.getSharedPreferences("dailyWorkSummary",
                                         Context.MODE_PRIVATE|Context.MODE_WORLD_WRITEABLE);
        Editor e = pref.edit();
        if ( isEmpty() ) {
            e.clear();
        } else {
            e.putLong("_id", getId());
            e.putLong("start_at", getStartAt());
            e.putLong("end_at", getEndAt());
        }
        e.commit();
    }

    public void restoreFromSharedPreference(Context context) {
        SharedPreferences pref =
            context.getSharedPreferences("dailyWorkSummary",
                                         Context.MODE_PRIVATE|Context.MODE_WORLD_READABLE);
        long _id = pref.getLong("_id", -1);
        long start_at = pref.getLong("start_at", -1);
        long end_at = pref.getLong("end_at", -1);
        if ( _id >= 0 && start_at >= 0 && end_at >= 0 ) {
            copy(new DailyWorkSummary(_id, start_at, end_at));
        }
    }

    public boolean hasConsistentDetails(SQLiteDatabase db) {
        if (!existInDatabase()) return false;

        List<DailyTaskSummary> dailyTaskSummaries =
            DailyTaskSummary.findById(db, _id);
        long time = 0;
        for (DailyTaskSummary dailyTaskSummary : dailyTaskSummaries) {
            time += dailyTaskSummary.getDuration();
        }
        return Math.abs(time - getTotal()) < 1000;
    }
}