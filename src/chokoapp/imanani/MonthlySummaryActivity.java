package chokoapp.imanani;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MonthlySummaryActivity extends Activity {


    private MonthButton button;
    private SQLiteDatabase db;
    private MonthlyWorkSummary summary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.monthly_summary);
        db = (new DBOpenHelper(this)).getWritableDatabase();
        summary = new MonthlyWorkSummary(db);
        button = (MonthButton)findViewById(R.id.monthSelectButton);
        button.addTextChangedListener(new DisplayMonthlySummary());
    }

    public void refreshView() {

        SimpleAdapter adapter =
                new SimpleAdapter(this, summary.getList(), R.layout.monthly_work_summary_list,
                        new String[]{"code", "description", "duration", "percent"},
                        new int[]{R.id.mws_code, R.id.mws_description, R.id.mws_duration, R.id.mws_percent}
                );

        ListView lv = (ListView)findViewById(R.id.monthlyWorkSummaryList);
        lv.setAdapter(adapter);

        TextView tv = (TextView)findViewById(R.id.monthlySummarySumView);
        tv.setText(summary.getTimeStringTotalDuration());
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
