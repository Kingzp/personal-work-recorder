package chokoapp.imanani;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.view.MotionEvent;

public class DownButton extends ImageView {
    private ContinuouslyDown downer;
    private UpDownView targetView;

    public DownButton(Context context) {
        this(context, null);
    }

    public DownButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setClickable(true);
        setImageResource(R.drawable.minus_button);
    }

    public void setupListeners(UpDownView view) {
        this.targetView = view;
        setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    targetView.down();
                }
            });

        setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    downer = new ContinuouslyDown();
                    downer.execute();
                    return true;
                }
            });
        setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        if ( downer != null ) downer.cancel(true);
                    case MotionEvent.ACTION_DOWN:
                        break;
                    default:
                        break;
                    }
                    return false;
                }
            });
    }

    private class ContinuouslyDown extends AsyncTask<Void, Void, Void> {
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

        protected void onProgressUpdate(Void... values) {
            targetView.down();
        }
    }
}