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

    protected MonthlyWorkSummary(){};

    public MonthlyWorkSummary(SQLiteDatabase db, int year, int month) {
        works = new ArrayList<MonthlyWork>();
        this.db = db;
        this.year = year;
        this.month = month;
    }

    public void queryWorks() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String fromDate = sdf.format(TimeUtils.getFirstDay(year, month).getTime());
        String toDate = sdf.format(TimeUtils.getLastDay(year, month).getTime());

        long totalDuration = 0;
        int totalPercent = 0;

        Cursor cursor = db.rawQuery(
                "    SELECT t.code, t.description, sum(t.duration) " +
                "      FROM daily_task_summary AS t " +
                "INNER JOIN daily_work_summary AS w " +
                "        ON t.daily_work_summary_id = w.id " +
                "     WHERE ? <= w.start_at AND  w.start_at <= ? " +
                "  GROUP BY t.code, t.description;",
                new String[] { fromDate, toDate });

        try {
            while (cursor.moveToNext()){
                MonthlyWork work = new MonthlyWork(
                        cursor.getString(0), cursor.getString(1), cursor.getLong(2));
                 works.add(work);
                 totalDuration += work.getDuration();
            }
        } finally {
            cursor.close();
        }

        if ( works.size() != 0) {
            Iterator<MonthlyWork> iterator = works.iterator();
            while(iterator.hasNext()) {
                MonthlyWork work = (MonthlyWork)iterator.next();
                if (totalDuration == 0) {
                    work.setPercent(100);
                } else {
                    work.setPercent(Math.round(work.getDuration()/totalDuration * 100));
                    totalPercent += work.getPercent();
                }
            }
            //割合の補正
            if (totalPercent != 100) {
                MonthlyWork work = works.get(works.size());
                int adjustedPercent = work.getPercent() + (100 - totalPercent);
                work.setPercent(adjustedPercent);
            }
        }
    }

    public ArrayList<HashMap<String, String>> getList() {
        ArrayList<HashMap<String, String>> list
            = new ArrayList<HashMap<String, String>>();

        if ( works.size() != 0) {
            Iterator<MonthlyWork> iterator = works.iterator();
            while(iterator.hasNext()) {
                MonthlyWork work = (MonthlyWork)iterator.next();
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("code", work.getCode());
                map.put("description", work.getDescription());
                map.put("duration", Long.toString(work.getDuration()));
                map.put("percent", Integer.toString(work.getPercent()));
                list.add(map);
            }
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
}
