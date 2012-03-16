package jp.gr.java_conf.choplin_j.imanani;

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
    private boolean needToSwitchFocus;
    private int errorMessageResourceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_list);

        deleteTask = (Button)findViewById(R.id.deleteTaskButton);
        deleteTask.setEnabled(false);

        defineTask = (Button)findViewById(R.id.defineTaskButton);
        defineTask.setEnabled(false);

        needToSwitchFocus = false;
        errorMessageResourceId = 0;

        taskCode = (EditText)findViewById(R.id.definedTaskCode);
        taskCode.setFilters(new InputFilter[] { new AlphaNumericFilter(this) });
        taskCode.addTextChangedListener(new EnableDefineButton(defineTask));
        taskCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus && validateInput() == false) {
                        needToSwitchFocus = true;
                    }
                }
            });

        taskDescription = (EditText)findViewById(R.id.definedDescription);
        taskDescription.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus && needToSwitchFocus) {
                        Toast.makeText(v.getContext(),
                                       errorMessageResourceId,
                                       Toast.LENGTH_SHORT).show();
                        v.clearFocus();
                        taskCode.requestFocus();
                        needToSwitchFocus = false;
                    }
                }
            });

        db = (new DBOpenHelper(this)).getWritableDatabase();
        allTaskCursor = db.query(Task.TABLE_NAME,
                                 new String[] {"_id", "code", "description"},
                                 null, null, null, null, "code");
        startManagingCursor(allTaskCursor);
        long cannotDeleteId = getIntent().getLongExtra("jp.gr.java_conf.choplin_j.imanani.cannotDeleteId", -1);
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

    private boolean validateInput() {
        String code = taskCode.getText().toString();
        if (code.length() == 0) {
            errorMessageResourceId = R.string.should_not_be_empty;
            return false;
        } else if (Task.findByCode(db, code) != null) {
            errorMessageResourceId = R.string.has_the_same_code;
            return false;
        } else {
            errorMessageResourceId = 0;
            return true;
        }
    }
}
