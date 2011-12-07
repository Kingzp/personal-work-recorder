package chokoapp.imanani;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends Activity {
    private SQLiteDatabase db;
    private TimeKeeper timeKeeper;
    private Ticker ticker;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.main);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
                R.layout.title_bar);
        displayToday();
        db = (new DBOpenHelper(this)).getWritableDatabase();
        setupSpinner();

        timeKeeper = new TimeKeeper(
                (TextView) findViewById(R.id.totalTimeView),
                (TextView) findViewById(R.id.durationView));
        timeKeeper.init(db);
        ticker = new Ticker(timeKeeper);
    }

    @Override
    public void onResume() {
        super.onResume();
        ticker.start();
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
        Spinner spinner = (Spinner) findViewById(R.id.selectTaskSpinner);

        final Cursor c = db.query(Task.TABLE_NAME, new String[] { "code",
                "description" }, null, null, null, null, null);
        List<Task> tasks = new ArrayList<Task>() {
            private static final long serialVersionUID = 6925359347298994019L;
            {
                while (c.moveToNext()) {
                    add(new Task(c.getString(0), c.getString(1)));
                }
            }
        };
        c.close();
        ArrayAdapter<Task> adapter = new ArrayAdapter<Task>(this,
                android.R.layout.simple_spinner_item, tasks);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        OnItemSelectedListener listener = new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                    int pos, long id) {
                timeKeeper.changeTask();
            }

            public void onNothingSelected(AdapterView<?> parent) {
                timeKeeper.changeTask();
            }
        };
        spinner.setOnItemSelectedListener(listener);
    }

    public void onClickStartButton(View view) {
        timeKeeper.beginWork();
    }

    public void onClickFinishButton(View view) {
        timeKeeper.endWork();
    }
    
    public void newTask(View v) {
        startActivity(new Intent(this, TaskListActivity.class));
    }
}
