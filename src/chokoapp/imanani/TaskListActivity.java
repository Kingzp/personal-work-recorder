package chokoapp.imanani;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TaskListActivity extends ListActivity {
    SQLiteDatabase db;
    Cursor allTaskCursor;
    EditText taskCode;
    EditText taskDescription;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_list);
        
        taskCode = (EditText)findViewById(R.id.definedTaskCode);
        taskDescription = (EditText)findViewById(R.id.definedDescription);
        
        db = (new DBOpenHelper(this)).getWritableDatabase();
        allTaskCursor = db.query(Task.TABLE_NAME, 
                            new String[] {"_id", "code", "description"},
                            null, null, null, null, null);
        startManagingCursor(allTaskCursor);
        setListAdapter(new TaskListAdapter(this, allTaskCursor));
    }
    
    public void defineTask(View v) {
        String code = taskCode.getText().toString();
        if ( code.length() > 0 ) {
            Cursor specifiedCodeCursor = db.query(Task.TABLE_NAME, null, "code = ?", new String[] {code},
                                null, null, null);
            if ( specifiedCodeCursor.moveToFirst() ) {
                Toast.makeText(this, getString(R.string.has_the_same_code), 
                               Toast.LENGTH_SHORT).show();
            } else {
                ContentValues newTask = new ContentValues();
                newTask.put("code", code);
                newTask.put("description", taskDescription.getText().toString());
                db.insert(Task.TABLE_NAME, null, newTask);
                allTaskCursor.requery();
            }
            specifiedCodeCursor.close();
        } else {
            Toast.makeText(this, getString(R.string.should_not_be_empty),
                           Toast.LENGTH_SHORT).show();
        }
    }
    
    public void deleteTask(View v) {
        ListView tasks = getListView();
        int tasks_count = tasks.getChildCount();
        ArrayList<Long> deleteIds = new ArrayList<Long>();
        for(int i = 0 ; i < tasks_count ; i++ ) {
            View taskView = tasks.getChildAt(i);
            CheckBox b = (CheckBox)taskView.findViewById(R.id.deleteCheck);
            if ( b.isChecked() ) {
                String checkedId = ((TextView)taskView.findViewById(R.id.taskId)).getText().toString();
                deleteIds.add(Long.parseLong(checkedId));
            }
        }
        db.beginTransaction();
        try {
            for(Long id : deleteIds) {
                db.delete(Task.TABLE_NAME, "_id = ?", 
                        new String[] { String.format("%d", id) });
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        allTaskCursor.requery();
    }

    private class TaskListAdapter extends CursorAdapter {
        private LayoutInflater inf;

        public TaskListAdapter(Context context, Cursor c) {
            super(context, c, true);
            inf = (LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            CheckBox check = (CheckBox)view.findViewById(R.id.deleteCheck);
            check.setChecked(false);
            TextView taskId = (TextView)view.findViewById(R.id.taskId);
            taskId.setText(String.format("%d",cursor.getLong(0)));
            TextView taskCode = (TextView)view.findViewById(R.id.taskCode);
            taskCode.setText(cursor.getString(1));
            TextView taskDescription = (TextView)view.findViewById(R.id.taskDescription);
            taskDescription.setText(cursor.getString(2));
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return inf.inflate(R.layout.task_item, null);
        }
        
    }
}
