package chokoapp.imanani;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DailyTaskSummary {
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

    public long getId() { return _id; }
    public String getCode() { return code; }
    public String getDescription() { return description; }
    public long getDuration() { return duration; }
    public long getDailyWorkSummaryId() { return daily_work_summary_id; }

    public static List<DailyTaskSummary> findById(SQLiteDatabase db, long daily_work_summary_id) {
        Cursor daily_task_summary_cursor =
            db.query(TABLE_NAME,
                     new String[] {"_id", "code", "description", "duration"},
                     "daily_work_summary_id = ?",
                     new String[] { String.format("%d", daily_work_summary_id) },
                     null, null, null);
        List<DailyTaskSummary> ret = new ArrayList<DailyTaskSummary>();
        while ( daily_task_summary_cursor.moveToNext() ) {
            ret.add(new DailyTaskSummary(daily_task_summary_cursor.getLong(0),
                                         daily_task_summary_cursor.getString(1),
                                         daily_task_summary_cursor.getString(2),
                                         daily_task_summary_cursor.getLong(3),
                                         daily_work_summary_id));
        }
        daily_task_summary_cursor.close();
        return ret;
    }
}