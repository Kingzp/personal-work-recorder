package chokoapp.imanani;

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

    public static Cursor findById(SQLiteDatabase db, long daily_work_summary_id) {
        return db.query(TABLE_NAME,
                        new String[] {"_id", "code", "description", "duration"},
                        "daily_work_summary_id = ?",
                        new String[] { String.format("%d", daily_work_summary_id) },
                        null, null, null);
    }
}