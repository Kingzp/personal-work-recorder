package chokoapp.imanani;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;

public class FooterView extends TextView {
    public FooterView(Context context) {
        this(context, null, 0);
    }
    public FooterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public FooterView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setText(context.getString(R.string.add_a_missing_task));
        setBackgroundResource(R.drawable.footer_background);
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
        setGravity(Gravity.CENTER_VERTICAL);
    }
}