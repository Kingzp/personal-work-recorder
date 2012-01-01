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
        long duration = time / 1000;
        long sec = duration % 60;
        long min = (duration / 60) % 60;
        long hor = duration / (60 * 60);
        setText(String.format("%02d:%02d:%02d", hor, min, sec));
    }

    public void setTime(long time) {
        this.time = time;
        invalidate();
    }

    public long getTime() { return time; }

    @Override
    public void up() {
        long remainder = time % UP_AND_DOWN_STEP;
        time += remainder + UP_AND_DOWN_STEP;
    }

    @Override
    public void down() {
        if ( time > 0 ) {
            long remainder = time % UP_AND_DOWN_STEP;
            time -= remainder > 0 ? remainder : UP_AND_DOWN_STEP;
        }
    }
}