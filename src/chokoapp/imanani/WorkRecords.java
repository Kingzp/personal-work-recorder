package chokoapp.imanani;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class WorkRecords {
    public static final String TABLE_NAME = "work_records";
    public static final String[] COLUMNS = {
        "start_time INTEGER",
        "end_time INTEGER"
    };

    public static Cursor findByDate(SQLiteDatabase db, long date) {
        return db.query(TABLE_NAME,
                        new String[] {"MIN(start_time)", "MAX(end_time)"},
                        "date(start_time / 1000, 'unixepoch', 'localtime') = ?",
                        new String[] { (new SimpleDateFormat("yyyy-MM-dd")).format(new Date(date)) },
                        "date(start_time / 1000, 'unixepoch', 'localtime')",
                        null, null);
    }

}