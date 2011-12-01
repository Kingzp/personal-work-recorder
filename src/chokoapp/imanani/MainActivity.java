package chokoapp.imanani;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
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
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar);
        displayToday();
        db = (new DBOpenHelper(this)).getReadableDatabase();
        setupSpinner();

        timeKeeper = new TimeKeeper();
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
		OnItemSelectedListener listener = new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				timeKeeper.changeTask();
			}

			@Override
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

    /**
     * 勤務時間と作業時間を計って、表示に反映させるクラス。
     */
    private class TimeKeeper implements Runnable {

        private long workStartTime;
        private long taskStartTime;

        /**
         * 勤務開始。
         * @return 通常は０を返す。勤務を終了せずに再び開始したら、前回開始してから現在までの時間を返す。
         */
        public long beginWork() {
			long now = System.currentTimeMillis();
			try {
				return workStartTime == 0 ? 0 : now - workStartTime;
			} finally {
				workStartTime = taskStartTime = now;
        	}
		}

        /**
         * 勤務終了。
         * @return 勤務開始から現在までの時間を返す。
         */
        public long endWork() {
        	long now = System.currentTimeMillis();
        	try {
				return workStartTime == 0 ? 0 : now - workStartTime;
        	} finally {
        		workStartTime = taskStartTime = 0;
        	}
        }

        /**
         * 作業変更。
         * @return 勤務開始または前回の作業変更から現在までの時間を返す。
         */
        public long changeTask() {
			if (workStartTime == 0) {
				// 勤務が開始していなければ、作業も開始しないで０を返す。
				return 0;
			}
        	long now = System.currentTimeMillis();
        	try {
				return taskStartTime == 0 ? 0 : now - taskStartTime;
        	} finally {
        		taskStartTime = now;
        	}
        }

        /**
         * 勤務時間と作業時間を表示に反映させる。
         */
        @Override
		public void run() {
			long now = System.currentTimeMillis();
			if (workStartTime != 0) {
				TextView total = (TextView) findViewById(R.id.totalTimeView);
				total.setText(format(now - workStartTime));
			}
			if (taskStartTime != 0) {
				TextView duration = (TextView) findViewById(R.id.durationView);
				duration.setText(format(now - taskStartTime));
			}
		}

        /**
         * 時間のフォーマッティング。
         * @param time ミリ秒単位の時間。
         * @return H:MM:SS形式の文字列。
         */
		public String format(long time) {
			long sec = time / 1000;
			long min = sec / 60;
			long hour = min / 60;
			return String.format("%02d:%02d:%02d", hour, min % 60, sec % 60);
		}

    }
}