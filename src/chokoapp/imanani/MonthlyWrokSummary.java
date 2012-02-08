package chokoapp.imanani;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MonthlyWrokSummary {

    private ArrayList<HashMap<String, String>> data;

    public void findByMonth(SQLiteDatabase db, int year, int month) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String fromDate = sdf.format(TimeUtils.getFirstDay(year, month).getTime());
        String toDate = sdf.format(TimeUtils.getLastDay(year, month).getTime());


//        Cursor csr =
//            db.query(TABLE_NAME,
//                     new String[] { "_id", "start_at", "end_at" },
//                     "date(start_at / 1000, 'unixepoch', 'localtime') = ?",
//                     new String[] { (new SimpleDateFormat("yyyy-MM-dd")).format(new Date(date)) },
//                     null, null, null);
//        try {
//            if ( daily_work_summary_cursor.moveToFirst() ) {
//                if ( daily_work_summary_cursor.isNull(2) ) {
//                    return new DailyWorkSummary(daily_work_summary_cursor.getLong(0),
//                                                daily_work_summary_cursor.getLong(1),
//                                                0);
//                } else {
//                    return new DailyWorkSummary(daily_work_summary_cursor.getLong(0),
//                                                daily_work_summary_cursor.getLong(1),
//                                                daily_work_summary_cursor.getLong(2));
//                }
//            } else {
//                return new DailyWorkSummary();
//            }
//        } finally {
//            daily_work_summary_cursor.close();
//        }
    }

}
