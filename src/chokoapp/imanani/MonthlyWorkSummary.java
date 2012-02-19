package chokoapp.imanani;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MonthlyWorkSummary {

    private ArrayList<MonthlyWork> works;
    private SQLiteDatabase db;
    private int year;
    private int month;
    private long totalDuration;

    public MonthlyWorkSummary(SQLiteDatabase db) {
        this.db = db;
    }

    public void queryWorks(int year, int month) {

        if (this.year == year && this.month == month) {
            return;
        }

        this.year = year;
        this.month = month;
        works = new ArrayList<MonthlyWork>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        String period = sdf.format(TimeUtils.getFirstDay(year, month).getTime());

        totalDuration = 0;
        int totalPercent = 0;

        Cursor cursor = db.rawQuery(
                "    SELECT t.code, t.description, sum(t.duration) " +
                "      FROM daily_task_summary AS t " +
                "INNER JOIN daily_work_summary AS w " +
                "        ON t.daily_work_summary_id = w._id " +
                "     WHERE strftime('%Y-%m', date(start_at / 1000, 'unixepoch', 'localtime')) = ? " +
                "  GROUP BY t.code, t.description;",
                new String[] { period });

        try {
            while (cursor.moveToNext()){
                MonthlyWork work = new MonthlyWork(
                        cursor.getString(0), cursor.getString(1), TimeUtils.getCutoffMsec(cursor.getLong(2)));
                 works.add(work);
                 totalDuration += work.getDuration();
            }
        } finally {
            cursor.close();
        }

        for (int i = 0; i <= works.size() - 1; i++) {
            MonthlyWork work = works.get(i);
            if (totalDuration == 0) {
                work.setPercent(100);
            } else {
                work.setPercent((int)(work.getDuration() * 100 / totalDuration));
            }
            totalPercent += work.getPercent();
        }

        if ( works.size() != 0) {
            MonthlyWork work = works.get(works.size() - 1);
            work.adjustPercent(totalPercent);
        }
    }

    public ArrayList<MonthlyWork> getWorks() {
        return works;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public long getTotalDuration() {
        return totalDuration;
    }
}
