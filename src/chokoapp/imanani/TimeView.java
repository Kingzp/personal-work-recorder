package chokoapp.imanani;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

public class TimeView extends UpDownView {
    private long time;

    public TimeView(Context context) {
        this(context, null);
    }
    public TimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
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

    @Override
    public void setTime(long time) {
        this.time = time;
        invalidate();
    }

    @Override
    public long getTime() { return time; }

    @Override
    public void up() {
        setTime(TimeUtils.up(getTime()));
    }

    @Override
    public void down() {
        setTime(TimeUtils.down(getTime()));
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}