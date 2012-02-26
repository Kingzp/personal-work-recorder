package chokoapp.imanani;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;

public class MonthlySummaryActivity extends Activity {
    private static final int DAILY_SUMMARY_REQUEST = 1;
    private CalendarView calendarView;
    private MonthlyWorkSummary summary;
    private MonthlySummaryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.monthly_summary);
        adapter = new MonthlySummaryAdapter(this);

        calendarView = (CalendarView)findViewById(R.id.calendarView);
        calendarView.setOnMonthSelectListener(new DisplayMonthlySummary());
        calendarView.setOnDateClickListener(new CalendarView.OnDateClickListener() {
                @Override
                public void onDateClick(int year, int month, int date) {
                    Intent intent = new Intent(MonthlySummaryActivity.this,
                                               DailySummaryActivity.class);
                    Calendar cal = Calendar.getInstance();
                    cal.set(year, month, date);
                    intent.putExtra("selectedDate", cal.getTime());
                    startActivityForResult(intent, DAILY_SUMMARY_REQUEST);
                }
            });

        summary = (MonthlyWorkSummary)getLastNonConfigurationInstance();

        if (summary == null) {
            SQLiteDatabase db = (new DBOpenHelper(this)).getWritableDatabase();
            summary = new MonthlyWorkSummary(db);
            summary.queryWorks(calendarView.getYear(), calendarView.getMonth());
        } else {
            calendarView.display(summary.getYear(), summary.getMonth());
        }

        calendarView.setTotalDuration(summary.getTotalDuration());
        refreshView();
     }

    @Override
    public Object onRetainNonConfigurationInstance() {
        return summary;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DAILY_SUMMARY_REQUEST) {
            if (resultCode == RESULT_OK) {
                Serializable date = data.getSerializableExtra("updatedDate");
                if ( date != null && date instanceof Date) {
                    calendarView.updateCell((Date)date);
                }
            }
        }
    }

    public void refreshView() {

        ArrayList<MonthlyWork>works = summary.getWorks();
        adapter.setList(works);

        ListView lv = (ListView)findViewById(R.id.monthlyWorkSummaryList);
        lv.setAdapter(adapter);
    }

    private class DisplayMonthlySummary implements CalendarView.OnMonthSelectListener {
        @Override
        public void onSelectMonth(int year, int month) {
            summary.queryWorks(year, month);
            calendarView.setTotalDuration(summary.getTotalDuration());
            refreshView();
        }
    }
}
