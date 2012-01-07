package chokoapp.imanani;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import java.util.Calendar;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Toast;

public class DailySummaryActivity extends ListActivity implements Observer {
    private SQLiteDatabase db;
    private DatePicker datePicker;
    private AlertDialog dateSelector;
    private DateButton dateSelectButton;
    private DateTimeView startTimeView;
    private DateTimeView endTimeView;
    private TimeView totalTimeView;
    private DailyWorkSummary dailyWorkSummary;
    private TimeView differenceTimeView;
    private FooterView footerView;
    private TaskSummaryAdapter taskSummaryAdapter;

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
        dateSelectButton.addTextChangedListener(new DisplaySummary());

        startTimeView = (DateTimeView)findViewById(R.id.startTimeView);
        ((ManipulateButton)findViewById(R.id.startTimeUp))
            .setManipulator(new Manipulator() {
                    @Override
                    public void execute() {
                        dailyWorkSummary.startTimeUp();
                        update(null, null);
                    }
                });
        ((ManipulateButton)findViewById(R.id.startTimeDown))
            .setManipulator(new Manipulator() {
                    @Override
                    public void execute() {
                        dailyWorkSummary.startTimeDown();
                        update(null, null);
                    }
                });

        endTimeView = (DateTimeView)findViewById(R.id.endTimeView);
        ((ManipulateButton)findViewById(R.id.endTimeUp))
            .setManipulator(new Manipulator() {
                    @Override
                    public void execute() {
                        dailyWorkSummary.endTimeUp();
                        update(null, null);
                    }
                });
        ((ManipulateButton)findViewById(R.id.endTimeDown))
            .setManipulator(new Manipulator() {
                    @Override
                    public void execute() {
                        dailyWorkSummary.endTimeDown();
                        update(null, null);
                    }
                });

        totalTimeView = (TimeView)findViewById(R.id.totalTimeView);
        differenceTimeView = (TimeView)findViewById(R.id.differenceTimeView);

        dailyWorkSummary = new DailyWorkSummary();

        ListView listView = getListView();
        footerView = new FooterView(this);
        listView.addFooterView(footerView);
        listView.setOnItemClickListener(new PopupTaskList());

        taskSummaryAdapter = new TaskSummaryAdapter(this);
        setListAdapter(taskSummaryAdapter);
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
            resetSummary(dateSelectButton.getTime());
            return true;
        case R.id.summaryAdjust:
            autoAdjust();
            return true;
        case R.id.summarySave:
            saveTable();
            return true;
        case R.id.summarySend:
            sendMail();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
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

        totalTimeView.setTime(dailyWorkSummary.getTotal());

        long diff = dailyWorkSummary.getTotal() - calculateTaskTotal();
        differenceTimeView.setTime(diff);
        if ( 0 <= diff && diff < 1000 ) {
            differenceTimeView.setTextColor(Color.GREEN);
        } else {
            differenceTimeView.setTextColor(Color.RED);
        }

        footerView.setVisibility(dailyWorkSummary.isEmpty() ? View.GONE : View.VISIBLE);
    }

    public void selectDate(View v) {
        if ( dateSelectButton.dateSelected() ) {
            datePicker.updateDate(dateSelectButton.getYear(),
                                  dateSelectButton.getMonth(),
                                  dateSelectButton.getDay());
        }
        dateSelector.show();
    }

    public void resetSummary(long date) {
        dailyWorkSummary.resetFromWorkRecord(db, date);
        taskSummaryAdapter.setDailyTaskSummaries(TaskRecord.findByDate(db, date));
        update(null, null);
    }

    public boolean isValid(String error_message) {
        if ( differenceTimeView.getTime() < 0 || differenceTimeView.getTime() >= 1000 ) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.error))
                .setMessage(error_message)
                .setPositiveButton(android.R.string.ok, null)
                .create().show();
            return false;
        } else {
            return true;
        }
    }

    public boolean saveTable() {
        if ( !isValid(getString(R.string.cannot_save_summary)) ) return false;

        db.beginTransaction();
        try {
            if ( dailyWorkSummary.save(db) != QueryResult.SUCCESS ) {
                Toast.makeText(this, getString(R.string.failed_to_save), Toast.LENGTH_LONG).show();
                return false;
            }
            db.delete(DailyTaskSummary.TABLE_NAME,
                      "daily_work_summary_id = ?",
                      new String[] { String.format("%d", dailyWorkSummary.getId()) });

            int count = taskSummaryAdapter.getCount();
            for ( int i = 0 ; i < count ; i++ ) {
                if ( taskSummaryAdapter.getItem(i).save(db, dailyWorkSummary.getId())
                     != QueryResult.SUCCESS ) {
                    Toast.makeText(this, getString(R.string.failed_to_save), Toast.LENGTH_LONG).show();
                    return false;
                }
            }

            db.setTransactionSuccessful();
            return true;
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
        @Override
        public void afterTextChanged(Editable e) {
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            long date = dateSelectButton.getTime();
            DailyWorkSummary fetchedWorkSummary = DailyWorkSummary.findByDate(db, date);
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
        }
    }

    public long calculateTaskTotal() {
        int count = taskSummaryAdapter.getCount();
        long sum = 0;
        for ( int i = 0 ; i < count ; i++ ) {
            sum += taskSummaryAdapter.getItem(i).getDuration();
        }
        return sum;
    }

    private void autoAdjust() {
        dailyWorkSummary.autoAdjust();
        taskSummaryAdapter.autoAdjust();
        update(null, null);
    }

    private void sendMail() {
        if ( !isValid(getString(R.string.cannot_send_report)) ) return;

        if ( !saveTable() ) return;

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        String mailSubject = String.format("[%s]%s(%s)",
                                           getString(R.string.app_name),
                                           getString(R.string.daily_work_report),
                                           TimeUtils.getDateString(dailyWorkSummary.getStartAt()));
        intent.putExtra(Intent.EXTRA_SUBJECT, mailSubject);
        intent.putExtra(Intent.EXTRA_TEXT, mailBody());
        try {
            startActivity(Intent.createChooser(intent, "send mail..."));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, getString(R.string.no_email_client), Toast.LENGTH_SHORT).show();
        }
    }

    private String mailBody() {
        StringBuilder builder = new StringBuilder();
        builder.append(getString(R.string.work_date) + "\n")
            .append(TimeUtils.getDateString(dailyWorkSummary.getStartAt()) + "\n")
            .append("\n")
            .append(getString(R.string.start_time) + "\t" +
                    getString(R.string.end_time) + "\n")
            .append(TimeUtils.getTimeString(dailyWorkSummary.getStartAt()) + "\t" +
                    TimeUtils.getTimeStringFrom(dailyWorkSummary.getEndAt(),
                                                dailyWorkSummary.getStartAt()) + "\n")
            .append("\n")
            .append(getString(R.string.task_code) + "\t" +
                    getString(R.string.task_name) + "\t" +
                    getString(R.string.duration) + "\n");

        int count = taskSummaryAdapter.getCount();
        for ( int i = 0 ; i < count ; i++ ) {
            DailyTaskSummary dailyTaskSummary = taskSummaryAdapter.getItem(i);
            builder.append(dailyTaskSummary.getCode() + "\t" +
                           dailyTaskSummary.getDescription() + "\t" +
                           TimeUtils.getTimeString(dailyTaskSummary.getDuration()) +
                           "\n");
        }

        return builder.toString();
    }

    private class PopupTaskList implements OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if ( view == footerView ) {
                List<CharSequence> items = new ArrayList<CharSequence>();
                List<Task> tasks = Task.findAll(db);
                List<Task> tasksNotInTheAdapter = new ArrayList<Task>();
                for ( Task task : tasks ) {
                    if ( !taskSummaryAdapter.contains( task.getCode() ) ) {
                        tasksNotInTheAdapter.add(task);
                    }
                }
                for ( Task task : tasksNotInTheAdapter ) {
                    items.add(task.toString());
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle(R.string.add_a_missing_task)
                    .setItems(items.toArray(new CharSequence[0]),
                              new AddMissingTask(DailySummaryActivity.this, tasksNotInTheAdapter))
                    .create().show();
            }
        }

        private class AddMissingTask implements OnClickListener {
            private DailySummaryActivity act;
            private List<Task> tasks;

            public AddMissingTask(DailySummaryActivity act, List<Task> tasks) {
                this.act = act;
                this.tasks = tasks;
            }

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String code = tasks.get(which).getCode();
                Task task = Task.findByCode(db, code);
                taskSummaryAdapter.add(new DailyTaskSummary(task));
                act.update(null, null);
            }
        }
    }
}
