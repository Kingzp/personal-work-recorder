package chokoapp.imanani;

import java.util.List;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TaskSummaryAdapter extends ArrayAdapter<DailyTaskSummary> {
    private LayoutInflater inf;
    private static int resourceId = R.layout.task_summary_item;

    public TaskSummaryAdapter(Context context, List<DailyTaskSummary> dailyTaskSummaries) {
        super(context, resourceId);
        for (DailyTaskSummary dailyTaskSummary : dailyTaskSummaries) {
            super.add(dailyTaskSummary);
        }
        this.inf = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DailyTaskSummary dailyTaskSummary = (DailyTaskSummary)getItem(position);
        if ( convertView == null ) {
            convertView = inf.inflate(resourceId, null);
        }
        TextView codeView = (TextView)convertView.findViewById(R.id.codeViewOnSummary);
        TextView descriptionView = (TextView)convertView.findViewById(R.id.descriptionOnSummary);
        TimeView taskDurationView = (TimeView)convertView.findViewById(R.id.taskDurationOnSummary);
        codeView.setText(dailyTaskSummary.getCode());
        descriptionView.setText(dailyTaskSummary.getDescription());
        taskDurationView.setTime(dailyTaskSummary.getDuration());

        UpButton upButton = (UpButton)convertView.findViewById(R.id.timePlusView);
        upButton.setupListeners(taskDurationView);
        DownButton downButton = (DownButton)convertView.findViewById(R.id.timeMinusView);
        downButton.setupListeners(taskDurationView);

        taskDurationView.addTextChangedListener(new UpdateDailyTaskSummary(position, taskDurationView));

        return convertView;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    private class UpdateDailyTaskSummary implements TextWatcher {
        private int position;
        private TimeView timeView;

        public UpdateDailyTaskSummary(int position, TimeView timeView) {
            this.position = position;
            this.timeView = timeView;
        }
        @Override
        public void afterTextChanged(Editable e) {
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            DailyTaskSummary dailyTaskSummary = getItem(position);
            dailyTaskSummary.setDuration(timeView.getTime());
        }
    }
}
