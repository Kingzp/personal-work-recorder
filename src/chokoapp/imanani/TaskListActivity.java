package chokoapp.imanani;

import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
        taskCode.setFilters(new InputFilter[] { new AlphaNumericFilter(this) });
        taskCode.addTextChangedListener(new EnableDefineButton(defineTask));

        taskDescription = (EditText)findViewById(R.id.definedDescription);

        db = (new DBOpenHelper(this)).getWritableDatabase();
        allTaskCursor = db.query(Task.TABLE_NAME, 
                                 new String[] {"_id", "code", "description"},
                                 null, null, null, null, "code");
        startManagingCursor(allTaskCursor);
        long cannotDeleteId = getIntent().getLongExtra("chokoapp.imanani.cannotDeleteId", -1);
        setListAdapter(new TaskListAdapter(this, allTaskCursor, deleteTask, cannotDeleteId));

    }

    public void defineTask(View v) {
        String code = taskCode.getText().toString();
        String description = taskDescription.getText().toString();

        if ( code.length() > 0 ) {
            Task task = Task.findByCode(db, code);
            if ( task != null ) {
                Toast.makeText(this, getString(R.string.has_the_same_code), 
                               Toast.LENGTH_SHORT).show();
            } else {
                task = new Task(code, description);
                task.save(db);
                allTaskCursor.requery();
                taskCode.setText("");
                taskDescription.setText("");
            }
        } else {
            Toast.makeText(this, getString(R.string.should_not_be_empty),
                           Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteTask(View v) {
        TaskListAdapter taskListAdapter = (TaskListAdapter)getListAdapter();
        taskListAdapter.deleteTask(db);
        allTaskCursor.requery();
    }

}
