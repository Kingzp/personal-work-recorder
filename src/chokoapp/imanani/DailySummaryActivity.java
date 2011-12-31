package chokoapp.imanani;

import java.util.Calendar;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
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
        dateSelectButton.addTextChangedListener(new DisplaySummary(dateSelectButton, this));

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
        datePicker.updateDate(dateSelectButton.getYear(),
                              dateSelectButton.getMonth(),
                              dateSelectButton.getDay());
        dateSelector.show();
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
        private DateButton dateButton;
        private ListActivity act;

        public DisplaySummary(DateButton dateButton, ListActivity act) {
            this.dateButton = dateButton;
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
            long work_summary_id = updateDailyWorkTable();
            updateDailyTaskTable(work_summary_id);

            Cursor daily_work_summary_cursor = DailyWorkSummary.findByDate(db, dateButton.getTime());
            if ( daily_work_summary_cursor.moveToFirst() ) {
                if ( daily_work_summary_cursor.isNull(2) ) {
                    setTime(startTimeView, endTimeView, totalTimeView,
                            daily_work_summary_cursor.getLong(1));
                } else {
                    setTime(startTimeView, endTimeView, totalTimeView,
                            daily_work_summary_cursor.getLong(1),
                            daily_work_summary_cursor.getLong(2));
                }
            } else {
                setTime(startTimeView, endTimeView, totalTimeView);
            }
            daily_work_summary_cursor.close();

            Cursor daily_task_summary_cursor = DailyTaskSummary.findById(db, work_summary_id);
            act.setListAdapter(new TaskSummaryAdapter(act, daily_task_summary_cursor));
        }

        private void setTime(DateTimeView startView, DateTimeView endView, TextView totalView,
                             long start, long end) {
            startView.setTime(start);
            endView.setTime(end);
        }

        private void setTime(DateTimeView startView, DateTimeView endView, TextView totalView,
                             long start) {
            startView.setTime(start);
            endView.clearTime();
        }

        private void setTime(DateTimeView startView, DateTimeView endView, TextView totalView) {
            startView.clearTime();
            endView.clearTime();
        }

        private long updateDailyWorkTable() {
            Cursor work_record_cursor = WorkRecord.findByDate(db, dateButton.getTime());
            Cursor daily_work_summary_cursor = DailyWorkSummary.findByDate(db, dateButton.getTime());
            long daily_work_summary_id = -1;
            if ( daily_work_summary_cursor.moveToFirst() ) {   // already has a summary record
                if ( work_record_cursor.moveToFirst() ) {
                    if ( work_record_cursor.isNull(1) ) {
                        DailyWorkSummary.setEndTimeNull(db, dateButton.getTime());
                    } else {
                        DailyWorkSummary.setEndTime(db, dateButton.getTime(),
                                                    work_record_cursor.getLong(1));
                    }
                }
                daily_work_summary_id = daily_work_summary_cursor.getLong(0);
            } else {                                           // no summary record
                if ( work_record_cursor.moveToFirst() ) {      // i have recorded the work
                    if ( work_record_cursor.isNull(1) ) {      // but i am now recording the work
                        daily_work_summary_id =
                            DailyWorkSummary.setStartTime(db, work_record_cursor.getLong(0));
                    } else {
                        daily_work_summary_id =
                            DailyWorkSummary.setStartEndTime(db, work_record_cursor.getLong(0),
                                                             work_record_cursor.getLong(1));
                    }
                }
            }
            try {
                return daily_work_summary_id;
            } finally {
                daily_work_summary_cursor.close();
                work_record_cursor.close();
            }
        }

        private void updateDailyTaskTable(long work_summary_id) {
            Cursor task_record_cursor = TaskRecord.findByDate(db, dateButton.getTime());
            db.beginTransaction();
            try {
                db.delete(DailyTaskSummary.TABLE_NAME,
                          "daily_work_summary_id = ?",
                          new String[] { String.format("%d", work_summary_id) } );
                while ( task_record_cursor.moveToNext() ) {
                    ContentValues val = new ContentValues();
                    val.put("code" , task_record_cursor.getString(0));
                    val.put("description", task_record_cursor.getString(1));
                    val.put("duration", task_record_cursor.getLong(2));
                    val.put("daily_work_summary_id", work_summary_id);
                    db.insert(DailyTaskSummary.TABLE_NAME, null, val);
                }
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
            task_record_cursor.close();
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
