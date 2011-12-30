package chokoapp.imanani;

import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;
import android.content.Context;
import java.util.Date;
import java.text.SimpleDateFormat;

public class DateTimeView extends TextView {
    private final static long UP_AND_DOWN_STEP = 15 * 60 * 1000;
    private Date date;
    private SimpleDateFormat df;

    public DateTimeView(Context context) {
        super(context);
        date = null;
        df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    }
    public DateTimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        date = null;
        df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if ( !isEmpty() ) {
            setText(df.format(this.date));
        } else {
            setText("9999/99/99 00:00:00");
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

    public void up() {
        if ( !isEmpty() ) {
            long current = date.getTime();
            long remainder = current % UP_AND_DOWN_STEP;
            setTime(current - remainder + UP_AND_DOWN_STEP);
        }
    }

    public void down() {
        if ( !isEmpty() ) {
            long current = date.getTime();
            long remainder = current % UP_AND_DOWN_STEP;
            setTime(current - (remainder == 0 ? UP_AND_DOWN_STEP : remainder));
        }
    }

    public boolean isEmpty() {
        return date == null;
    }
}