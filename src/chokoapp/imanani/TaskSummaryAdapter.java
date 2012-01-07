package chokoapp.imanani;

import java.util.Comparator;
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

    public TaskSummaryAdapter(Context context) {
        super(context, resourceId);
        this.inf = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setDailyTaskSummaries(List<DailyTaskSummary> dailyTaskSummaries) {
        clear();
        for (DailyTaskSummary dailyTaskSummary : dailyTaskSummaries) {
            add(dailyTaskSummary);
        }
    }

    @Override
    public void add(DailyTaskSummary dailyTaskSummary) {
        super.add(dailyTaskSummary);
        sort(new Comparator<DailyTaskSummary>() {
                @Override
                public int compare(DailyTaskSummary lhs, DailyTaskSummary rhs) {
                    return lhs.getCode().compareTo(rhs.getCode());
                }
                @Override
                public boolean equals(Object obj) {
                    return this.equals(obj);
                }
            });
        dailyTaskSummary.addObserver((DailySummaryActivity)getContext());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final DailyTaskSummary dailyTaskSummary = (DailyTaskSummary)getItem(position);
        if ( convertView == null ) {
            convertView = inf.inflate(resourceId, null);
        }
        TextView codeView = (TextView)convertView.findViewById(R.id.codeViewOnSummary);
        TextView descriptionView = (TextView)convertView.findViewById(R.id.descriptionOnSummary);
        final TimeView taskDurationView = (TimeView)convertView.findViewById(R.id.taskDurationOnSummary);
        codeView.setText(dailyTaskSummary.getCode());
        descriptionView.setText(dailyTaskSummary.getDescription());
        taskDurationView.setTime(dailyTaskSummary.getDuration());

        ((ManipulateButton)convertView.findViewById(R.id.timePlusView))
            .setManipulator(new Manipulator() {
                    @Override
                    public void execute() {
                        taskDurationView.up();
                        dailyTaskSummary.up();
                    }
                });
        ((ManipulateButton)convertView.findViewById(R.id.timeMinusView))
            .setManipulator(new Manipulator() {
                    @Override
                    public void execute() {
                        taskDurationView.down();
                        dailyTaskSummary.down();
                    }
                });

        return convertView;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    public boolean contains(String code) {
        int count = getCount();
        for ( int i = 0 ; i < count ; i++ ) {
            if ( getItem(i).getCode().equals(code) ) return true;
        }
        return false;
    }

    public void autoAdjust() {
        int count = getCount();
        for ( int i = 0 ; i < count ; i++ ) {
            getItem(i).autoAdjust();
        }
        notifyDataSetChanged();
    }
}
