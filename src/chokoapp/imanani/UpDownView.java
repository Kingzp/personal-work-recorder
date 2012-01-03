package chokoapp.imanani;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

abstract class UpDownView extends TextView {
    protected final static long UP_AND_DOWN_STEP = 15 * 60 * 1000;

    public UpDownView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    public UpDownView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public UpDownView(Context context) {
        super(context);
    }

    abstract public void up();
    abstract public void down();
    abstract public long getTime();

    abstract public boolean isEmpty();
    abstract public void setTime(long date);

    public void autoAdjust() {
        if ( !isEmpty() ) {
            long date = getTime();
            long remainder = date % UP_AND_DOWN_STEP;
            if ( remainder != 0 ) {
                if ( remainder < UP_AND_DOWN_STEP / 2 &&
                     remainder > -UP_AND_DOWN_STEP / 2) {
                    setTime(date - remainder);
                } else {
                    setTime(date + (UP_AND_DOWN_STEP - remainder));
                }
            }
        }
    }
}