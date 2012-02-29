package chokoapp.imanani;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class ManipulateButton extends ImageView {
    private Manipulator manipulator;
    private ContinuousTouch continuousToch;

    public ManipulateButton(Context context) {
        this(context, null, 0);
    }
    public ManipulateButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public ManipulateButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setClickable(true);
    }

    public void setManipulator(Manipulator manip) {
        this.manipulator = manip;

        setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    manipulator.execute();
                }
            });

        setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    continuousToch = new ContinuousTouch();
                    continuousToch.execute();
                    return true;
                }
            });
        setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        if ( continuousToch != null) {
                            continuousToch.cancel(true);
                        }
                        break;
                    case MotionEvent.ACTION_DOWN:
                        break;
                    default:
                        break;
                    }
                    return false;
                }
            });
    }

    private class ContinuousTouch extends AsyncTask<Void, Void, Void> {
        private static final int UPDATE_INTERVAL = 500;

        @Override
        protected Void doInBackground(Void... params) {
            while ( true ) {
                if ( isCancelled() ) {
                    return null;
                }
                try {
                    Thread.sleep(UPDATE_INTERVAL);
                } catch (InterruptedException e) {
                    return null;
                }
                publishProgress();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            manipulator.execute();
        }
    }
}