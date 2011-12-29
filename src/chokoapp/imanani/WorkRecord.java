package chokoapp.imanani;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class WorkRecord {
    public static final String TABLE_NAME = "work_records";
    public static final String[] COLUMNS = {
        "start_time INTEGER",
        "end_time INTEGER"
    };

    public static Cursor findByDate(SQLiteDatabase db, long date) {
        return db.rawQuery("SELECT MIN(start_time), " +
                           "       CASE WHEN EXISTS (SELECT _id " +
                           "                           FROM work_records " +
                           "                          WHERE date(start_time / 1000, 'unixepoch', 'localtime') = ? " +
                           "                            AND end_time IS NULL) THEN NULL " +
                           "            ELSE MAX(end_time) END " +
                           "  FROM work_records " +
                           " WHERE date(start_time / 1000, 'unixepoch', 'localtime') = ? " +
                           " GROUP BY date(start_time / 1000, 'unixepoch', 'localtime');",
                           new String[] { (new SimpleDateFormat("yyyy-MM-dd")).format(new Date(date)),
                                          (new SimpleDateFormat("yyyy-MM-dd")).format(new Date(date)) });
    }
}