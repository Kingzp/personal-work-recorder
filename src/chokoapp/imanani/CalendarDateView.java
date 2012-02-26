package chokoapp.imanani;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CalendarDateView extends RelativeLayout {
    private LayoutInflater inf;
    private TextView dateView;
    private TextView timeView;
    private Date date;

    public CalendarDateView(Context context) {
        this(context, null, 0);
    }
    public CalendarDateView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public CalendarDateView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        inf = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View topLayout = inf.inflate(R.layout.date_cell, this);

        dateView = (TextView)topLayout.findViewById(R.id.calendarDateView);
        timeView = (TextView)topLayout.findViewById(R.id.calendarDayTotalView);
    }

    public void setBackground(boolean isSunday) {
        setBackgroundResource(isSunday ? R.drawable.date_cell_sunday : R.drawable.date_cell);
    }

    public void setDate(Date date, SQLiteDatabase db) {
        if (this.date == date) return;

        this.date = date;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        setDate(cal.get(Calendar.DATE));

        if ( db != null ) {
            DailyWorkSummary dailyWorkSummary =
                DailyWorkSummary.findByDate(db, date.getTime());
            List<DailyTaskSummary> dailyTaskSummaries = null;
            int color = Color.BLACK;
            if (dailyWorkSummary.isEmpty()) {
                dailyTaskSummaries = TaskRecord.findByDate(db, date.getTime());
                if ( !dailyTaskSummaries.isEmpty() ) {
                    color = Color.parseColor("#8ff0e68c"); /* khaki color */
                }
            } else {
                dailyTaskSummaries = DailyTaskSummary.findById(db, dailyWorkSummary.getId());
                color = Color.parseColor("#8f90ee90"); /* light green */
            }
            long time = 0;
            for (DailyTaskSummary dailyTaskSummary : dailyTaskSummaries) {
                time += dailyTaskSummary.getDuration();
            }
            setTime(time);
            setTextColor(color);
        } else {
            setTime(0);
            setTextColor(Color.BLACK);
        }
    }
    public void setDate(int date) {
        dateView.setText(Integer.toString(date));
    }

    public void setInvalid() {
        setBackgroundResource(R.drawable.date_cell_invalid);
        timeView.setText("");
    }

    private void setTime(long time) {
        long sec = time / 1000;
        timeView.setText(String.format("%02d:%02d", sec / (60 * 60), (sec / 60) % 60));
    }
    private void setTextColor(int color) {
        timeView.setTextColor(color);
    }
}