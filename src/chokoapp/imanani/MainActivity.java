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
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends Activity {
    private SQLiteDatabase db;
    private TimeKeeper timeKeeper;
    private Ticker ticker;
    private TaskSelectionSpinner spinner;
    private TaskInputView taskInputView;
    private ToggleButton toggleRecordingButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.main);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
                                  R.layout.title_bar);
        displayToday();

        db = (new DBOpenHelper(this)).getWritableDatabase();
        spinner = (TaskSelectionSpinner)findViewById(R.id.selectTaskSpinner);


        timeKeeper = new TimeKeeper(
                (TextView) findViewById(R.id.totalTimeView),
                (TextView) findViewById(R.id.durationView));
        timeKeeper.init(db);
        ticker = new Ticker(timeKeeper);

        taskInputView = (TaskInputView)findViewById(R.id.taskInputView);
        taskInputView.setOnTaskChangedListener(new OnTaskChangedListener() {
                @Override
                public void onChanged(Task task) {
                    spinner.initialize(timeKeeper);
                    spinner.setSelection(task);
                }
            });
        toggleRecordingButton = (ToggleButton)findViewById(R.id.toggleRecordingButton);
        toggleRecordingButton.setChecked(timeKeeper.nowRecording());
        toggleRecordingButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (toggleRecordingButton.isChecked()) {
                        Task selectedTask = (Task)spinner.getSelectedItem();
                        if ( selectedTask != null ) timeKeeper.beginWork(selectedTask);
                    } else {
                        timeKeeper.endWork();
                    }
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
        case R.id.monthlySummaryMenu:
            startActivity(new Intent(this, MonthlySummaryActivity.class));
            return true;
        case R.id.defineTaskMenu:
            Intent intent = new Intent(this, TaskListActivity.class);
            if (timeKeeper.nowRecording()) {
                intent.putExtra("chokoapp.imanani.cannotDeleteId",
                                timeKeeper.getCurrentTaskId());
            }
            startActivity(intent);
            return true;
        case R.id.appPreferencesMenu:
            startActivity(new Intent(this, AppPreferenceActivity.class));
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        spinner.initialize(timeKeeper);
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
}
