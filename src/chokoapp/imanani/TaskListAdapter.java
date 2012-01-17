package chokoapp.imanani;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class TaskListAdapter extends CursorAdapter {
    private LayoutInflater inf;
    private ArrayList<Long> checkedIds;
    private Button deleteButton;
    private long cannotDeleteId;

    public TaskListAdapter(Context context, Cursor c, Button button, long cannotDeleteId) {
        super(context, c, true);
        inf = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        checkedIds = new ArrayList<Long>();
        deleteButton = button;
        this.cannotDeleteId = cannotDeleteId;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        long taskIdofThisRow = cursor.getLong(0);

        CheckBox check = (CheckBox)view.findViewById(R.id.deleteCheck);
        check.setChecked(checkedIds.contains(taskIdofThisRow));
        check.setEnabled(taskIdofThisRow != cannotDeleteId);
        check.setOnClickListener(new ChangeCheckedIds(taskIdofThisRow));

        TextView taskCode = (TextView)view.findViewById(R.id.taskCode);
        taskCode.setText(cursor.getString(1));
        TextView taskDescription = (TextView)view.findViewById(R.id.taskDescription);
        taskDescription.setText(cursor.getString(2));

        view.setOnLongClickListener(new ShowEditDialog(context, cursor));
        view.setLongClickable(taskIdofThisRow != cannotDeleteId);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inf.inflate(R.layout.task_item, null);
    }

    private class ChangeCheckedIds implements View.OnClickListener {
        private long checkedId;

        public ChangeCheckedIds(long id) { this.checkedId = id; }

        @Override
        public void onClick(View v) {
            if ( checkedIds.contains(checkedId) ) {
                checkedIds.remove(checkedId);
            } else {
                checkedIds.add(checkedId);
            }
            deleteButton.setEnabled(!checkedIds.isEmpty());
        }
    }

    public void deleteTask(SQLiteDatabase db) {
        db.beginTransaction();
        try {
            for(Long id : checkedIds) {
                db.delete(Task.TABLE_NAME, "_id = ?",
                        new String[] { String.format("%d", id) });
            }
            db.setTransactionSuccessful();
            checkedIds.clear();
            deleteButton.setEnabled(false);
        } finally {
            db.endTransaction();
        }
    }
}
