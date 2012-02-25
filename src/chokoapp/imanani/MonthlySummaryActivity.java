package chokoapp.imanani;

import java.util.ArrayList;
import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;

public class MonthlySummaryActivity extends Activity {

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

        summary = (MonthlyWorkSummary)getLastNonConfigurationInstance();

        if (summary == null) {
            SQLiteDatabase db = (new DBOpenHelper(this)).getWritableDatabase();
            summary = new MonthlyWorkSummary(db);
        }
        if (summary.hasQueried()) {
            calendarView.display(summary.getYear(), summary.getMonth());
        } else {
            summary.queryWorks(calendarView.getYear(), calendarView.getMonth());
        }
        refreshView();
     }

    @Override
    public Object onRetainNonConfigurationInstance() {
        return summary;
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
            refreshView();
        }
    }
}
