package chokoapp.imanani;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DailyTaskSummary extends Observable {
    public static final String TABLE_NAME = "daily_task_summary";
    public static final String[] COLUMNS = {
        "code TEXT",
        "description TEXT",
        "duration INTEGER",
        "daily_work_summary_id INTEGER"
    };

    private long _id;
    private String code;
    private String description;
    private long duration;
    private long daily_work_summary_id;

    public DailyTaskSummary(long _id, String code, String description,
                            long duration, long daily_work_summary_id) {
        this._id = _id;
        this.code = code;
        this.description = description;
        this.duration = duration;
        this.daily_work_summary_id = daily_work_summary_id;
    }

    public DailyTaskSummary(Task task) {
        this._id = 0;
        this.code = task.getCode();
        this.description = task.getDescription();
        this.duration = 0;
        this.daily_work_summary_id = 0;
    }

    public long getId() { return _id; }
    public String getCode() { return code; }
    public String getDescription() { return description; }
    public long getDuration() { return duration; }
    public long getDailyWorkSummaryId() { return daily_work_summary_id; }

    public QueryResult save(SQLiteDatabase db, long daily_work_summary_id) {
        ContentValues val = new ContentValues();
        val.put("code", code);
        val.put("description", description);
        val.put("duration", duration);
        val.put("daily_work_summary_id", daily_work_summary_id);
        long id = db.insert(TABLE_NAME, null, val);
        if ( id == -1 ) {
            return QueryResult.FAIL;
        } else {
            this._id = id;
            this.daily_work_summary_id = daily_work_summary_id;
            return QueryResult.SUCCESS;
        }
    }

    public void setDuration(long duration) {
        if ( duration < 0 ) return;
        if ( this.duration == duration ) return;

        this.duration = duration;
        setChanged();
        notifyObservers();
    }

    public void up() {
        setDuration(TimeUtils.up(getDuration()));
    }

    public void down() {
        setDuration(TimeUtils.down(getDuration()));
    }

    public void autoAdjust() {
        setDuration(TimeUtils.adjustTime(getDuration()));
    }

    @SuppressWarnings("serial")
    public static List<DailyTaskSummary> findById(SQLiteDatabase db, final long daily_work_summary_id) {
        final Cursor daily_task_summary_cursor =
            db.query(TABLE_NAME,
                     new String[] {"_id", "code", "description", "duration"},
                     "daily_work_summary_id = ?",
                     new String[] { String.format("%d", daily_work_summary_id) },
                     null, null, null);
        try {
            return new ArrayList<DailyTaskSummary>() {{
                    while ( daily_task_summary_cursor.moveToNext() ) {
                        add(new DailyTaskSummary(daily_task_summary_cursor.getLong(0),
                                                 daily_task_summary_cursor.getString(1),
                                                 daily_task_summary_cursor.getString(2),
                                                 daily_task_summary_cursor.getLong(3),
                                                 daily_work_summary_id));
                    }
                }};
        } finally {
            daily_task_summary_cursor.close();
        }
    }

    public void saveToSharedPreference(Context context) {
        SharedPreferences pref =
            context.getSharedPreferences(this.toString(), 
                                         Context.MODE_PRIVATE|Context.MODE_WORLD_WRITEABLE);
        Editor e = pref.edit();
        e.putLong("_id", getId());
        e.putString("code", getCode());
        e.putString("description", getDescription());
        e.putLong("duration", getDuration());
        e.putLong("daily_work_summary_id", getDailyWorkSummaryId());
        e.commit();
   }

    @SuppressWarnings("serial")
    public static List<DailyTaskSummary>
        restoreFromSharedPreference(final Context context, final String dailyTaskSummaries) {
        return new ArrayList<DailyTaskSummary>() {{
                for ( String dailyTaskSummary : dailyTaskSummaries.split(":") ) {
                    SharedPreferences prefx =
                        context.getSharedPreferences(dailyTaskSummary,
                                                     Context.MODE_PRIVATE|Context.MODE_WORLD_READABLE);
                    long _id = prefx.getLong("_id", -1);
                    String code = prefx.getString("code", null);
                    String description = prefx.getString("description", null);
                    long duration = prefx.getLong("duration", -1);
                    long daily_work_summary_id = prefx.getLong("daily_work_summary_id", -1);
                    if ( _id >= 0 && code != null && description != null &&
                         duration >= 0 && daily_work_summary_id >= 0 ) {
                        add(new DailyTaskSummary(_id, code, description, duration, daily_work_summary_id));
                    }
                }
            }};
    }
}