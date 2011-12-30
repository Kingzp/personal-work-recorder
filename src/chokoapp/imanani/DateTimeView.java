package chokoapp.imanani;

import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;
import android.content.Context;
import java.util.Date;
import java.text.SimpleDateFormat;

public class DateTimeView extends TextView {
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
        if ( date != null ) {
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
        return date == null ? -1 : date.getTime();
    }
}