package chokoapp.imanani;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

import android.app.AlertDialog;
import android.app.ListActivity;
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
    private Cursor summaryCursor;
    private DatePicker datePicker;
    private AlertDialog dateSelector;
    private DateButton dateSelectButton;
    private TextView startTimeView;
    private TextView endTimeView;
    private TextView totalTimeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daily_summay);
        db = (new DBOpenHelper(this)).getWritableDatabase();
        summaryCursor = db.rawQuery("SELECT _id, code, description, '99:99:99' FROM task_records", null);

        setListAdapter(new TaskSummaryAdapter(this, summaryCursor));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        datePicker = new DatePicker(this);
        builder.setView(datePicker);
        builder.setTitle(getResources().getString(R.string.select_date));
        builder.setPositiveButton(android.R.string.ok, new SetDate());
        builder.setNegativeButton(android.R.string.cancel, null);
        dateSelector = builder.create();

        dateSelectButton = (DateButton)findViewById(R.id.dateSelectButton);
        dateSelectButton.addTextChangedListener(new DisplaySummary(dateSelectButton));

        startTimeView = (TextView)findViewById(R.id.startTimeView);
        endTimeView = (TextView)findViewById(R.id.endTimeView);
        totalTimeView = (TextView)findViewById(R.id.totalTimeView);
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

        public DisplaySummary(DateButton dateButton) {
            this.dateButton = dateButton;
        }

        @Override
        public void afterTextChanged(Editable e) {
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            updateDailyWorkTable();

            Cursor daily_work_summary_cursor = DailyWorkSummary.findByDate(db, dateButton.getTime());
            if ( daily_work_summary_cursor.moveToFirst() ) {
                if ( daily_work_summary_cursor.isNull(1) ) {
                    setTime(startTimeView, endTimeView, totalTimeView,
                            daily_work_summary_cursor.getLong(0));
                } else {
                    setTime(startTimeView, endTimeView, totalTimeView,
                            daily_work_summary_cursor.getLong(0),
                            daily_work_summary_cursor.getLong(1));
                }
            } else {
                setTime(startTimeView, endTimeView, totalTimeView);
            }
        }

        private void setTime(TextView startView, TextView endView, TextView totalView,
                             long start, long end) {
            Date startDate = new Date();
            startDate.setTime(start);
            Date endDate = new Date();
            endDate.setTime(end);
            SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            startView.setText(df.format(startDate));
            endView.setText(df.format(endDate));
            long totalSeconds = (end - start) / 1000;
            long sec = totalSeconds % 60;
            long min = (totalSeconds / 60) % 60;
            long hor = totalSeconds / (60 * 60);
            totalView.setText(String.format("%02d:%02d:%02d", hor, min, sec));
        }

        private void setTime(TextView startView, TextView endView, TextView totalView,
                             long start) {
            Date startDate = new Date();
            startDate.setTime(start);
            SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            startView.setText(df.format(startDate));
            endView.setText("9999/99/99 99:99:99");
            totalView.setText("99:99:99");
        }

        private void setTime(TextView startView, TextView endView, TextView totalView) {
            startView.setText("9999/99/99 99:99:99");
            endView.setText("9999/99/99 99:99:99");
            totalView.setText("99:99:99");
        }

        private void updateDailyWorkTable() {
            Cursor work_record_cursor = WorkRecords.findByDate(db, dateButton.getTime());
            Cursor daily_work_summary_cursor = DailyWorkSummary.findByDate(db, dateButton.getTime());
            if ( daily_work_summary_cursor.moveToFirst() ) {   // already has a summary record
                if ( daily_work_summary_cursor.isNull(1) ) {   // but the record has not ended
                    if ( work_record_cursor.moveToFirst() &&
                         !work_record_cursor.isNull(1) ) {     // if we have a latest info, update it.
                        DailyWorkSummary.setEndTime(db, dateButton.getTime(), work_record_cursor.getLong(1));
                    }
                }
            } else {                                           // no summary record
                if ( work_record_cursor.moveToFirst() ) {      // i have recorded the work
                    if ( work_record_cursor.isNull(1) ) {      // but i am now recording the work
                        DailyWorkSummary.setStartTime(db, work_record_cursor.getLong(0));
                    } else {
                        DailyWorkSummary.setStartEndTime(db, work_record_cursor.getLong(0),
                                                         work_record_cursor.getLong(1));
                    }
                }
            }
            daily_work_summary_cursor.close();
            work_record_cursor.close();
        }

    }
}
