package chokoapp.imanani;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import java.util.Calendar;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

public class DailySummaryActivity extends ListActivity implements Observer {
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
    private TimeView differenceTimeView;
    private DailyTaskSummary dummyTask;

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
        startTimeView.addTextChangedListener(new DisplayTotal());

        endTimeView = (DateTimeView)findViewById(R.id.endTimeView);
        endTimeView.addTextChangedListener(new DisplayTotal());

        totalTimeView = (TextView)findViewById(R.id.totalTimeView);
        differenceTimeView = (TimeView)findViewById(R.id.differenceTimeView);

        dummyTask = new DailyTaskSummary(0, "Dummy",
                                         "for notify that change startTime or endTime", 0, 0);
        dummyTask.addObserver(this);

        startTimeUp = (UpButton)findViewById(R.id.startTimeUp);
        startTimeUp.setupListeners(startTimeView, dummyTask);

        startTimeDown = (DownButton)findViewById(R.id.startTimeDown);
        startTimeDown.setupListeners(startTimeView, dummyTask);

        endTimeUp = (UpButton)findViewById(R.id.endTimeUp);
        endTimeUp.setupListeners(endTimeView, dummyTask);

        endTimeDown = (DownButton)findViewById(R.id.endTimeDown);
        endTimeDown.setupListeners(endTimeView, dummyTask);

        dailyWorkSummary = new DailyWorkSummary();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(getApplication()).inflate(R.menu.daily_summary_save, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return dailyWorkSummary.isEmpty() ? false : true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.summaryReset:
            resetSummary();
            return true;
        case R.id.summaryAdjust:
            return true;
        case R.id.summarySave:
            saveTable();
            return true;
        case R.id.summarySend:
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if ( startTimeView.isEmpty() || endTimeView.isEmpty() ) return;

        long diff = calculateWorkTotal() - calculateTaskTotal();
        differenceTimeView.setTime(diff);
        if ( 0 <= diff && diff < 1000 ) {
            differenceTimeView.setTextColor(Color.GREEN);
        } else {
            differenceTimeView.setTextColor(Color.RED);
        }
    }

    public void selectDate(View v) {
        if ( dateSelectButton.dateSelected() ) {
            datePicker.updateDate(dateSelectButton.getYear(),
                                  dateSelectButton.getMonth(),
                                  dateSelectButton.getDay());
        }
        dateSelector.show();
    }

    public void resetSummary() {
        long date = dateSelectButton.getTime();
        dailyWorkSummary.resetFromWorkRecord(db, date);
        updateDisplayTime();
        List<DailyTaskSummary> dailyTaskSummaries = TaskRecord.findByDate(db, date);
        for ( DailyTaskSummary dailyTaskSummary : dailyTaskSummaries ) {
            dailyTaskSummary.addObserver(this);
        }
        setListAdapter(new TaskSummaryAdapter(this, dailyTaskSummaries));
        update(null, null);
    }

    public void saveTable() {
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
        private DailySummaryActivity act;

        public DisplaySummary(DailySummaryActivity act) {
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

            for ( DailyTaskSummary dailyTaskSummary : dailyTaskSummaries ) {
                dailyTaskSummary.addObserver(act);
            }
            act.setListAdapter(new TaskSummaryAdapter(act, dailyTaskSummaries));
            update(null, null);
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

    private class DisplayTotal implements TextWatcher {
        @Override
        public void afterTextChanged(Editable e) {
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            long totalSeconds = calculateWorkTotal() / 1000;
            long sec = totalSeconds % 60;
            long min = (totalSeconds / 60) % 60;
            long hor = totalSeconds / (60 * 60);
            totalTimeView.setText(String.format("%02d:%02d:%02d", hor, min, sec));
        }
    }

    public long calculateWorkTotal() {
        if ( startTimeView.isEmpty() || endTimeView.isEmpty() ) {
            return 0;
        } else {
            return endTimeView.getTime() - startTimeView.getTime();
        }
    }

    public long calculateTaskTotal() {
        TaskSummaryAdapter adapter = (TaskSummaryAdapter)getListAdapter();
        int count = adapter.getCount();
        long sum = 0;
        for ( int i = 0 ; i < count ; i++ ) {
            sum += adapter.getItem(i).getDuration();
        }
        return sum;
    }
}