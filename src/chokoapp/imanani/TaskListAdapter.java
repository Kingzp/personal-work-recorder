package chokoapp.imanani;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class TaskListAdapter extends CursorAdapter {
    private LayoutInflater inf;
    private ArrayList<Long> checkedIds;
    private Button deleteButton;

    public TaskListAdapter(Context context, Cursor c, Button button) {
        super(context, c, true);
        inf = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        checkedIds = new ArrayList<Long>();
        deleteButton = button;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        CheckBox check = (CheckBox)view.findViewById(R.id.deleteCheck);
        check.setChecked(false);
        check.setOnCheckedChangeListener(new EnableDeleteButton(cursor.getLong(0)));
        TextView taskId = (TextView)view.findViewById(R.id.taskId);
        taskId.setText(String.format("%d",cursor.getLong(0)));
        TextView taskCode = (TextView)view.findViewById(R.id.taskCode);
        taskCode.setText(cursor.getString(1));
        TextView taskDescription = (TextView)view.findViewById(R.id.taskDescription);
        taskDescription.setText(cursor.getString(2));
        view.setOnLongClickListener(new ShowEditDialog(context, cursor));
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inf.inflate(R.layout.task_item, null);
    }

    private class EnableDeleteButton implements OnCheckedChangeListener {
        private long id;

        public EnableDeleteButton(long id) {
            this.id = id;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            if (isChecked) {
                checkedIds.add(id);
            } else {
                checkedIds.remove(id);
            }
            if (checkedIds.isEmpty() ) {
                deleteButton.setEnabled(false);
            } else {
                deleteButton.setEnabled(true);
            }
        }
    }
}
