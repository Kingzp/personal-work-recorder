package chokoapp.imanani;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ListView;
import android.widget.TextView;

public class MonthlySummaryActivity extends Activity {


    private MonthButton button;
    private MonthlyWorkSummary summary;
    private MonthlySummaryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.monthly_summary);
        adapter = new MonthlySummaryAdapter(this);
        button = (MonthButton)findViewById(R.id.monthSelectButton);
        button.addTextChangedListener(new DisplayMonthlySummary());

        summary = (MonthlyWorkSummary)getLastNonConfigurationInstance();

        if (summary == null) {
            SQLiteDatabase db = (new DBOpenHelper(this)).getWritableDatabase();
            summary = new MonthlyWorkSummary(db);
        } else if (summary.isQeryed()) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, summary.getYear());
            cal.set(Calendar.MONTH, summary.getMonth());
            button.setDate(summary.getYear(), summary.getMonth());
            button.setButtonText();
            refreshView();
        }
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

        TextView tv = (TextView)findViewById(R.id.monthlySummarySumView);
        tv.setText(TimeUtils.getMonthlyTimeString(this, summary.getTotalDuration()));
    }

    private class DisplayMonthlySummary implements TextWatcher {
        @Override
        public void afterTextChanged(Editable e) {
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            summary.queryWorks(button.getYear(), button.getMonth());
            refreshView();
        }
    }
}
