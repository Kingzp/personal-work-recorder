package chokoapp.imanani;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class TaskSummaryAdapter extends CursorAdapter {
    private LayoutInflater inf;

    public TaskSummaryAdapter(Context context, Cursor c) {
        super(context, c);
        inf = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView codeView = (TextView)view.findViewById(R.id.codeViewOnSummary);
        TextView descriptionView = (TextView)view.findViewById(R.id.descriptionOnSummary);
        TextView taskDurationView = (TextView)view.findViewById(R.id.taskDurationOnSummary);
        codeView.setText(cursor.getString(1));
        descriptionView.setText(cursor.getString(2));
        taskDurationView.setText(cursor.getString(3));
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inf.inflate(R.layout.task_summary_item, null);
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

}
