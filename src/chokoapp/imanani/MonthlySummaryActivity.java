package chokoapp.imanani;

import android.app.ListActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import java.util.List;
import java.util.Observable;
import java.util.Observer;


public class MonthlySummaryActivity extends ListActivity implements Observer {


    private MonthButton monthSelectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.monthly_summary);

        monthSelectButton = (MonthButton)findViewById(R.id.monthSelectButton);
        monthSelectButton.addTextChangedListener(new DisplayMonthlySummary());

    }

    @Override
    public void update(Observable o, Object arg) {

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
            long date = monthSelectButton.getTime();
 /*
            MonthlyWorkSummary mws = MonthlyWorkSummary.findByDate(db, date);
            if ( fetchedWorkSummary.isEmpty() ) {
                fetchedWorkSummary = WorkRecord.findByDate(db, date);
            }
            dailyWorkSummary.copy(fetchedWorkSummary);

            List<DailyTaskSummary> dailyTaskSummaries =
                dailyWorkSummary.existInDatabase() ?
                    DailyTaskSummary.findById(db, dailyWorkSummary.getId()) :
                    TaskRecord.findByDate(db, date);

            taskSummaryAdapter.setDailyTaskSummaries(dailyTaskSummaries);
            update(null, null);
        */
        }

    }
}
