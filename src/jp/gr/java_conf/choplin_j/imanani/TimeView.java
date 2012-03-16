package jp.gr.java_conf.choplin_j.imanani;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

public class TimeView extends TextView {
    private long time;

    public TimeView(Context context) {
        this(context, null);
    }
    public TimeView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }
    public TimeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        time = 0;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        String sign = "";
        long duration;
        if ( time < 0 ) {
            sign = "-";
            duration = (-1 * time) / 1000;
        } else {
            duration = time / 1000;
        }

        long sec = duration % 60;
        long min = (duration / 60) % 60;
        long hor = duration / (60 * 60);
        setText(String.format("%s%02d:%02d:%02d", sign, hor, min, sec));
    }

    public void setTime(long time) {
        this.time = time;
        invalidate();
    }

    public long getTime() { return time; }

    public void up() {
        setTime(TimeUtils.up(getTime()));
    }

    public void down() {
        setTime(TimeUtils.down(getTime()));
    }
}