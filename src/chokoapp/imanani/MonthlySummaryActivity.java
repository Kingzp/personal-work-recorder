package chokoapp.imanani;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ListView;
import android.widget.SimpleAdapter;


import java.util.Observable;
import java.util.Observer;


public class MonthlySummaryActivity extends Activity {


    private MonthButton button;
    private SQLiteDatabase db;
    private MonthlyWorkSummary summary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.monthly_summary);
        db = (new DBOpenHelper(this)).getWritableDatabase();
        button = (MonthButton)findViewById(R.id.monthSelectButton);
        button.addTextChangedListener(new DisplayMonthlySummary());

    }

    public void update(Observable o, Object arg) {

        SimpleAdapter adapter =
                new SimpleAdapter(this, summary.getList(), R.layout.monthly_work_summary_list,
                        new String[]{"code", "description", "duration", "percent"},
                        new int[]{R.id.mws_code, R.id.mws_description, R.id.mws_duration, R.id.mws_percent}
                );

        ListView lv = (ListView)findViewById(R.id.monthlyWorkSummaryList);
        lv.setAdapter(adapter);
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

            if (summary == null ||
                button.getYear()  != summary.getYear() ||
                button.getMonth() != summary.getMonth() ) {

                summary = new MonthlyWorkSummary(db, button.getYear(), button.getMonth());
                summary.queryWorks();
                update(null, null);
            }
        }

    }
}
