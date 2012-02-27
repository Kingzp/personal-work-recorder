package chokoapp.imanani;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

public class MonthlySummaryActivity extends Activity {
    private static final int DAILY_SUMMARY_REQUEST = 1;
    private CalendarView calendarView;
    private MonthlySummaryAdapter adapter;
    private CachedMonth cache;

    private static class CachedMonth {
        private int year;
        private int month;
        public CachedMonth(int year, int month) {
            this.year = year;
            this.month = month;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.monthly_summary);

        adapter = new MonthlySummaryAdapter(this);

        ListView lv = (ListView)findViewById(R.id.monthlyWorkSummaryList);
        lv.setAdapter(adapter);

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

        cache = (CachedMonth)getLastNonConfigurationInstance();
        if ( cache == null ) {
            int year = calendarView.getYear();
            int month = calendarView.getMonth();
            cache = new CachedMonth(year, month);

        } else {
            calendarView.setMonth(cache.year, cache.month);
        }

        adapter.queryWorks(cache.year, cache.month);
        calendarView.setTotalDuration(adapter.getTotalDuration());
    }

    @Override
    public Object onRetainNonConfigurationInstance() {
        return new CachedMonth(calendarView.getYear(), calendarView.getMonth());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DAILY_SUMMARY_REQUEST) {
            if (resultCode == RESULT_OK) {
                Serializable date = data.getSerializableExtra("updatedDate");
                if ( date != null && date instanceof Date) {
                    calendarView.updateCell((Date)date);
                    adapter.queryWorks(calendarView.getYear(),
                                       calendarView.getMonth());
                    calendarView.setTotalDuration(adapter.getTotalDuration());
                }
            }
        }
    }

    private class DisplayMonthlySummary implements CalendarView.OnMonthSelectListener {
        @Override
        public void onSelectMonth(int year, int month) {
            adapter.queryWorks(year, month);
            calendarView.setTotalDuration(adapter.getTotalDuration());
        }
    }
}
