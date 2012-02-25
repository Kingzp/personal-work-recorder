package chokoapp.imanani;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CalendarDateView extends RelativeLayout {
    private LayoutInflater inf;
    private TextView dateView;
    private TextView timeView;

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

    public void setDate(int date) {
        dateView.setText(Integer.toString(date));
    }

    public void setInvalid() {
        setBackgroundResource(R.drawable.date_cell_invalid);
        timeView.setText("");
    }

    public void setTime(long time) {
        long sec = time / 1000;
        timeView.setText(String.format("%02d:%02d", sec / (60 * 60), (sec / 60) % 60));
    }
    public void setTextColor(int color) {
        timeView.setTextColor(color);
    }
}