package chokoapp.imanani;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import chokoapp.imanani.TaskInputView.OnTaskChangedListener;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MainActivity extends Activity {
    private SQLiteDatabase db;
    private TimeKeeper timeKeeper;
    private Ticker ticker;
    private TaskSelectionSpinner spinner;
    private TaskInputView taskInputView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.main);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
                R.layout.title_bar);
        displayToday();
        db = (new DBOpenHelper(this)).getWritableDatabase();
        timeKeeper = new TimeKeeper(
                (TextView) findViewById(R.id.totalTimeView),
                (TextView) findViewById(R.id.durationView));
        timeKeeper.init(db);
        ticker = new Ticker(timeKeeper);

        taskInputView = (TaskInputView)findViewById(R.id.taskInputView);
        taskInputView.setOnTaskChangedListener(new OnTaskChangedListener() {
                @Override
                public void onChanged(Task task) {
                    setupSpinner();
                    spinner.setSelection(task);
                }
            });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(getApplication()).inflate(R.menu.transition, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.dailySummaryMenu:
            startActivity(new Intent(this, DailySummaryActivity.class));
            return true;
        case R.id.monthlySummaryMenu:
            return true;
        case R.id.defineTaskMenu:
            Intent intent = new Intent(this, TaskListActivity.class);
            if (timeKeeper.nowRecording()) {
                intent.putExtra("chokoapp.imanani.cannotDeleteId", 
                                timeKeeper.getCurrentTaskId());
            }
            startActivity(intent);
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setupSpinner();
        ticker.start();
        taskInputView.setAdapter(new TaskCompleteAdapter(this, db));
    }

    @Override
    public void onPause() {
        super.onPause();
        ticker.stop();
    }

    private void displayToday() {
        TextView v = (TextView) findViewById(R.id.todayView);
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd (E)");
        v.setText(df.format(Calendar.getInstance().getTime()));
    }

    private void setupSpinner() {
        ArrayAdapter<Task> adapter = new ArrayAdapter<Task>(
            this, android.R.layout.simple_spinner_item, Task.findAll(db));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner = (TaskSelectionSpinner)findViewById(R.id.selectTaskSpinner);
        spinner.setTimeKeeperAndAdapter(timeKeeper, adapter);
    }

    public void onClickStartButton(View view) {
        Task selectedTask = (Task)spinner.getSelectedItem();
        if ( selectedTask != null ) timeKeeper.beginWork(selectedTask);
    }

    public void onClickFinishButton(View view) {
        timeKeeper.endWork();
    }
}
