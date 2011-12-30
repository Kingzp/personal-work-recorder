package chokoapp.imanani;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class TaskRecord {
    public static final String TABLE_NAME = "task_records";
    public static final String[] COLUMNS = {
        "work_id INTEGER",
        "start_time INTEGER",
        "code TEXT",
        "description TEXT"
    };

    public static Cursor findByDate(SQLiteDatabase db, long date) {
        return db.rawQuery("SELECT task_records.code task_code," +
                           "       task_records.description task_desc," +
                           "       SUM(CASE WHEN EXISTS (SELECT *" +
                           "                               FROM task_records tr_for_end" +
                           "                              WHERE task_records.work_id = tr_for_end.work_id" +
                           "                                AND task_records.start_time < tr_for_end.start_time)" +
                           "                THEN (SELECT MIN(tr_for_end.start_time)" +
                           "                        FROM task_records tr_for_end" +
                           "                       WHERE task_records.work_id = tr_for_end.work_id" +
                           "                         AND task_records.start_time < tr_for_end.start_time)" +
                           "                ELSE work_records.end_time END" +
                           "             - task_records.start_time) duration" +
                           "  FROM work_records LEFT OUTER JOIN  task_records" +
                           "    ON work_records._id = work_id" +
                           " WHERE date(work_records.start_time / 1000, 'unixepoch', 'localtime') = ?" +
                           " GROUP BY task_records.code;",
                           new String[] { (new SimpleDateFormat("yyyy-MM-dd")).format(new Date(date)) });
    }

}