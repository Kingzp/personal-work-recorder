package chokoapp.imanani;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class TaskSelectionSpinner extends Spinner {
    private long currentTaskId;
    private TimeKeeper timeKeeper;

    public TaskSelectionSpinner(Context context, ArrayAdapter<Task> adapter, 
                                long currentTaskId, TimeKeeper timeKeeper) {
        super(context);
        this.timeKeeper = timeKeeper;

        setPromptId(R.string.selectTask);
        setAdapter(adapter);
        int spinnerPosition = 0;
        int count = adapter.getCount();
        for (int i = 0 ; i < count ; i++ ) {
            if (adapter.getItem(i).getId() == currentTaskId) {
                spinnerPosition = i;
            }
        }
        this.currentTaskId = adapter.getItem(spinnerPosition).getId();
        setSelection(spinnerPosition);
        setOnItemSelectedListener(new ChangeTask(spinnerPosition));
    }

    public long getCurrentTaskId() { return currentTaskId; }

    private class ChangeTask implements OnItemSelectedListener {
        private int currentSpinnerPosition;
        public ChangeTask(int currentSpinnerPosition) {
            this.currentSpinnerPosition = currentSpinnerPosition;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int pos, long id) {
            if ( currentSpinnerPosition != pos ) {
                Task selectedTask = (Task)parent.getItemAtPosition(pos);
                currentTaskId = selectedTask.getId();
                timeKeeper.changeTask(selectedTask);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    }
}
