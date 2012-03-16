package jp.gr.java_conf.choplin_j.imanani;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MonthlyWork {

    private String code;
    private String description;
    private long duration;
    private int percent;

    public MonthlyWork(String code, String description, long duration){
        this.code = code;
        this.description = description;
        this.duration = duration;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public static List<MonthlyWork> queryWorks(SQLiteDatabase db, int year, int month) {

        Cursor cursor = db.rawQuery(
                "    SELECT t.code, t.description, sum(t.duration) " +
                "      FROM daily_task_summary AS t " +
                "INNER JOIN daily_work_summary AS w " +
                "        ON t.daily_work_summary_id = w._id " +
                "     WHERE strftime('%Y%m', date(start_at / 1000, 'unixepoch', 'localtime')) = ? " +
                "  GROUP BY t.code, t.description;",
                new String[] { String.format("%04d%02d", year, month+1) });

        List<MonthlyWork> works = new ArrayList<MonthlyWork>();
        while (cursor.moveToNext()){
            MonthlyWork work = new MonthlyWork(
                cursor.getString(0), cursor.getString(1), TimeUtils.getCutoffMsec(cursor.getLong(2)));
            works.add(work);
        }
        try {
            return works;
        } finally {
            cursor.close();
        }
    }
}
