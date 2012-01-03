package chokoapp.imanani;

import android.graphics.Canvas;
import android.util.AttributeSet;
import android.content.Context;
import java.util.Date;
import java.text.SimpleDateFormat;

public class DateTimeView extends UpDownView {
    private Date date;
    private SimpleDateFormat df;

    public DateTimeView(Context context) {
        this(context, null);
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

    @Override
    public void setTime(long date) {
        this.date = new Date(date);
        invalidate();
    }

    public void clearTime() {
        this.date = null;
        invalidate();
    }

    @Override
    public long getTime() {
        return isEmpty() ? -1 : date.getTime();
    }

    @Override
    public void up() {
        if ( !isEmpty() ) {
            long current = date.getTime();
            long remainder = current % UP_AND_DOWN_STEP;
            setTime(current - remainder + UP_AND_DOWN_STEP);
        }
    }

    @Override
    public void down() {
        if ( !isEmpty() ) {
            long current = date.getTime();
            long remainder = current % UP_AND_DOWN_STEP;
            setTime(current - (remainder == 0 ? UP_AND_DOWN_STEP : remainder));
        }
    }

    @Override
    public boolean isEmpty() {
        return date == null;
    }

}