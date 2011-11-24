package chokoapp.imanani;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Chronometer;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends Activity {
    private SQLiteDatabase db;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.main);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar);
        displayToday();
        db = (new DBOpenHelper(this)).getReadableDatabase();
        setupSpinner();
    }

    private void displayToday() {
        TextView v = (TextView) findViewById(R.id.todayView);
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd (E)");
        v.setText(df.format(Calendar.getInstance().getTime()));
    }

    private void setupSpinner() {
        Spinner spinner = (Spinner) findViewById(R.id.selectWBSSpinner);

        final Cursor c = db.query(Task.TABLE_NAME, new String[] { "code", "description" },
                                  null, null, null, null, null);
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
    }

    public void onClickStartButton(View view) {
        Chronometer chronometer = (Chronometer)findViewById(R.id.durationView);
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
    }

    public void onClickFinishButton(View view) {
        Chronometer chronometer = (Chronometer)findViewById(R.id.durationView);
        chronometer.stop();
    }
}