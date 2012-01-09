package chokoapp.imanani;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

public class TaskInputView extends RelativeLayout {
    private LayoutInflater inf;
    
    public TaskInputView(Context context) {
        this(context, null, 0);
    }

    public TaskInputView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TaskInputView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        inf = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inf.inflate(R.layout.task_input, this);
    }
}
