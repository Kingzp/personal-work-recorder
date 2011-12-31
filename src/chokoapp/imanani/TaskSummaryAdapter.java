package chokoapp.imanani;

import java.util.List;

import android.content.Context;
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
        TextView taskDurationView = (TextView)convertView.findViewById(R.id.taskDurationOnSummary);
        codeView.setText(dailyTaskSummary.getCode());
        descriptionView.setText(dailyTaskSummary.getDescription());
        long duration = dailyTaskSummary.getDuration() / 1000;
        long sec = duration % 60;
        long min = (duration / 60) % 60;
        long hor = duration / (60 * 60);
        taskDurationView.setText(String.format("%02d:%02d:%02d", hor, min, sec));
        return convertView;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

}
