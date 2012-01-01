package chokoapp.imanani;

import java.util.List;

import java.util.Calendar;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

public class DailySummaryActivity extends ListActivity {
    private SQLiteDatabase db;
    private DatePicker datePicker;
    private AlertDialog dateSelector;
    private DateButton dateSelectButton;
    private DateTimeView startTimeView;
    private DateTimeView endTimeView;
    private TextView totalTimeView;
    private UpButton startTimeUp;
    private DownButton startTimeDown;
    private UpButton endTimeUp;
    private DownButton endTimeDown;
    private DailyWorkSummary dailyWorkSummary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daily_summay);
        db = (new DBOpenHelper(this)).getWritableDatabase();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        datePicker = new DatePicker(this);
        builder.setView(datePicker);
        builder.setTitle(getResources().getString(R.string.select_date));
        builder.setPositiveButton(android.R.string.ok, new SetDate());
        builder.setNegativeButton(android.R.string.cancel, null);
        dateSelector = builder.create();

        dateSelectButton = (DateButton)findViewById(R.id.dateSelectButton);
        dateSelectButton.addTextChangedListener(new DisplaySummary(this));

        startTimeView = (DateTimeView)findViewById(R.id.startTimeView);
        endTimeView = (DateTimeView)findViewById(R.id.endTimeView);
        totalTimeView = (TextView)findViewById(R.id.totalTimeView);
        startTimeView.addTextChangedListener(new CalculateTotal(startTimeView,
                                                                endTimeView,
                                                                totalTimeView));
        endTimeView.addTextChangedListener(new CalculateTotal(startTimeView,
                                                              endTimeView,
                                                              totalTimeView));

        startTimeUp = (UpButton)findViewById(R.id.startTimeUp);
        startTimeUp.setupListeners(startTimeView);

        startTimeDown = (DownButton)findViewById(R.id.startTimeDown);
        startTimeDown.setupListeners(startTimeView);

        endTimeUp = (UpButton)findViewById(R.id.endTimeUp);
        endTimeUp.setupListeners(endTimeView);

        endTimeDown = (DownButton)findViewById(R.id.endTimeDown);
        endTimeDown.setupListeners(endTimeView);
    }

    public void selectDate(View v) {
        if ( dateSelectButton.dateSelected() ) {
            datePicker.updateDate(dateSelectButton.getYear(),
                                  dateSelectButton.getMonth(),
                                  dateSelectButton.getDay());
        }
        dateSelector.show();
    }

    public void resetSummary(View v) {
        long date = dateSelectButton.getTime();
        dailyWorkSummary.resetFromWorkRecord(db, date);
        updateDisplayTime();
        setListAdapter(new TaskSummaryAdapter(this, TaskRecord.findByDate(db, date)));
    }

    public void saveTable(View v) {
        dailyWorkSummary.update(startTimeView, endTimeView);

        db.beginTransaction();
        try {
            if ( dailyWorkSummary.save(db) != QueryResult.SUCCESS ) {
                return;
            }
            db.delete(DailyTaskSummary.TABLE_NAME,
                      "daily_work_summary_id = ?",
                      new String[] { String.format("%d", dailyWorkSummary.getId()) });

            TaskSummaryAdapter adapter = (TaskSummaryAdapter)getListAdapter();
            int count = adapter.getCount();
            for ( int i = 0 ; i < count ; i++ ) {
                if ( adapter.getItem(i).save(db, dailyWorkSummary.getId()) 
                     != QueryResult.SUCCESS ) {
                    return;
                }
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    private class SetDate implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, datePicker.getYear());
            cal.set(Calendar.MONTH, datePicker.getMonth());
            cal.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
            dateSelectButton.setDate(cal.getTime());
        }
    }

    private class DisplaySummary implements TextWatcher {
        private ListActivity act;

        public DisplaySummary(ListActivity act) {
            this.act = act;
        }

        @Override
        public void afterTextChanged(Editable e) {
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            long date = dateSelectButton.getTime();
            dailyWorkSummary = DailyWorkSummary.findByDate(db, date);
            if ( dailyWorkSummary.isEmpty() ) {
                dailyWorkSummary = WorkRecord.findByDate(db, date);
            }

            updateDisplayTime();

            List<DailyTaskSummary> dailyTaskSummaries =
                dailyWorkSummary.existInDatabase() ?
                    DailyTaskSummary.findById(db, dailyWorkSummary.getId()) :
                    TaskRecord.findByDate(db, date);

            act.setListAdapter(new TaskSummaryAdapter(act, dailyTaskSummaries));
        }
    }

    private void updateDisplayTime() {
        if ( dailyWorkSummary.isEmpty() ) {
            startTimeView.clearTime();
            endTimeView.clearTime();
        } else {
            if ( dailyWorkSummary.nowRecording() ) {
                startTimeView.setTime(dailyWorkSummary.getStartAt());
                endTimeView.clearTime();
            } else {
                startTimeView.setTime(dailyWorkSummary.getStartAt());
                endTimeView.setTime(dailyWorkSummary.getEndAt());
            }
        }
    }

    private class CalculateTotal implements TextWatcher {
        private DateTimeView startView;
        private DateTimeView endView;
        private TextView totalView;

        public CalculateTotal(DateTimeView startView, DateTimeView endView, TextView totalView) {
            this.startView = startView;
            this.endView = endView;
            this.totalView = totalView;
        }

        @Override
        public void afterTextChanged(Editable e) {
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if ( startTimeView.isEmpty() || endTimeView.isEmpty() ) {
                totalView.setText("00:00:00");
            } else {
                long totalSeconds = (endView.getTime() - startView.getTime()) / 1000;
                long sec = totalSeconds % 60;
                long min = (totalSeconds / 60) % 60;
                long hor = totalSeconds / (60 * 60);
                totalView.setText(String.format("%02d:%02d:%02d", hor, min, sec));
            }
        }
    }
}
