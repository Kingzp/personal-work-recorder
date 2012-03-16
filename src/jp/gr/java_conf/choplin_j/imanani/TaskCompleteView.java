package jp.gr.java_conf.choplin_j.imanani;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

public class TaskCompleteView extends AutoCompleteTextView {

    public TaskCompleteView(Context context) {
        super(context);
    }

    public TaskCompleteView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TaskCompleteView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected CharSequence convertSelectionToString(Object selectedItem) {
        if ( selectedItem instanceof Task ) {
            Task task = (Task)selectedItem;
            return task.getCode();
        }
        return super.convertSelectionToString(selectedItem);
    }
}
