package chokoapp.imanani;

import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;
import android.content.Context;

import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;

public class DateTimeView extends TextView {
    private Date date;
    private SimpleDateFormat df;

    public DateTimeView(Context context) {
        this(context, null);
    }
    public DateTimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        date = null;
        df = new SimpleDateFormat("M/d HH:mm:ss");
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if ( !isEmpty() ) {
            setText(datePadding() + df.format(this.date));
        } else {
            setText("99/99 00:00:00");
        }
    }

    public void setTime(long date) {
        this.date = new Date(date);
        invalidate();
    }

    public void clearTime() {
        this.date = null;
        invalidate();
    }

    public long getTime() {
        return isEmpty() ? -1 : date.getTime();
    }

    public boolean isEmpty() {
        return date == null;
    }

    private String datePadding() {
        if (isEmpty()) return "";
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return (cal.get(Calendar.MONTH) < 9 ? " " : "") +
               (cal.get(Calendar.DATE) < 10 ? " " : "");
    }
}