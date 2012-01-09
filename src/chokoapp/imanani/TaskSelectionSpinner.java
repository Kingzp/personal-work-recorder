package chokoapp.imanani;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class TaskSelectionSpinner extends Spinner {
    private TimeKeeper timeKeeper;

    public TaskSelectionSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPromptId(R.string.selectTask);
    }

    public void setTimeKeeperAndAdapter(TimeKeeper timeKeeper,
                                        ArrayAdapter<Task> adapter) {
        this.timeKeeper = timeKeeper;
        setAdapter(adapter);
        setOnItemSelectedListener(new ChangeTask());
        if ( adapter.getCount() == 0 ) return;

        int spinnerPosition = -1;
        int count = adapter.getCount();
        for (int i = 0 ; i < count ; i++ ) {
            if (adapter.getItem(i).getId() == timeKeeper.getCurrentTaskId()) {
                spinnerPosition = i;
            }
        }
        if ( spinnerPosition != -1 ) {
            setSelection(spinnerPosition);
        }
    }
    public boolean valid() { return timeKeeper != null; }

    private class ChangeTask implements OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int pos, long id) {
            Task selectedTask = (Task)parent.getItemAtPosition(pos);
            timeKeeper.changeTask(selectedTask);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    }

    public void setSelection(Task task) {
        ArrayAdapter<Task> adapter = (ArrayAdapter<Task>)getAdapter();
        int count = adapter.getCount();
        for ( int i = 0 ; i < count ; i++ ) {
            if ( adapter.getItem(i).getCode().equals(task.getCode()) ) {
                super.setSelection(i);
                break;
            }
        }
    }
}
