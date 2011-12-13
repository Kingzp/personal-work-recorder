package chokoapp.imanani;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TaskListActivity extends ListActivity {
    private SQLiteDatabase db;
    private Cursor allTaskCursor;
    private Button deleteTask;
    private Button defineTask;
    private EditText taskCode;
    private EditText taskDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_list);

        deleteTask = (Button)findViewById(R.id.deleteTaskButton);
        deleteTask.setEnabled(false);

        defineTask = (Button)findViewById(R.id.defineTaskButton);
        defineTask.setEnabled(false);

        taskCode = (EditText)findViewById(R.id.definedTaskCode);
        taskCode.setFilters(new InputFilter[] { new AlphaNumericFilter() });
        taskCode.addTextChangedListener(new EnableDefineButton(defineTask));

        taskDescription = (EditText)findViewById(R.id.definedDescription);

        db = (new DBOpenHelper(this)).getWritableDatabase();
        allTaskCursor = db.query(Task.TABLE_NAME, 
                            new String[] {"_id", "code", "description"},
                            null, null, null, null, null);
        startManagingCursor(allTaskCursor);
        long cannotDeleteId = getIntent().getLongExtra("chokoapp.imanani.cannotDeleteId", -1);
        setListAdapter(new TaskListAdapter(this, allTaskCursor, deleteTask, cannotDeleteId));

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
                taskCode.setText("");
                taskDescription.setText("");
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

}
