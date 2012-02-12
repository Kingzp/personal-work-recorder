package chokoapp.imanani;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String fromDate = sdf.format(TimeUtils.getFirstDay(year, month).getTime());
        String toDate = sdf.format(TimeUtils.getLastDay(year, month).getTime());

        totalDuration = 0;
        int totalPercent = 0;

        Cursor cursor = db.rawQuery(
                "    SELECT t.code, t.description, sum(t.duration) " +
                "      FROM daily_task_summary AS t " +
                "INNER JOIN daily_work_summary AS w " +
                "        ON t.daily_work_summary_id = w._id " +
                "     WHERE strftime('%s', ?) * 1000  <= w.start_at AND  w.start_at <= strftime('%s', ?) * 1000" +
                "  GROUP BY t.code, t.description;",
                new String[] { fromDate, toDate });

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

        for (int i = 0; i <= works.size() - 2; i++) {
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
            work.setPercent(100 - totalPercent);
        }
    }

    public ArrayList<HashMap<String, String>> getList() {
        ArrayList<HashMap<String, String>> list
            = new ArrayList<HashMap<String, String>>();

        Iterator<MonthlyWork> iterator = works.iterator();
        while(iterator.hasNext()) {
            MonthlyWork work = iterator.next();
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("code", work.getCode());
            map.put("description", work.getDescription());
            map.put("duration", TimeUtils.getTimeString(work.getDuration()));
            map.put("percent", Integer.toString(work.getPercent()) + "%");
            list.add(map);
        }

        return list;
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

    public String getTimeStringTotalDuration() {
        return TimeUtils.getTimeString(totalDuration);
    }
}
