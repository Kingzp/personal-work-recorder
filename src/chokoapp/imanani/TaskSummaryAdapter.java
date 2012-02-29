package chokoapp.imanani;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
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
        final DailyTaskSummary dailyTaskSummary = getItem(position);
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

    public boolean contains(Task task) {
        int count = getCount();
        for ( int i = 0 ; i < count ; i++ ) {
            if ( getItem(i).getCode().equals(task.getCode()) ) return true;
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

    public long getTotal() {
        int count = getCount();
        long sum = 0;
        for ( int i = 0 ; i < count ; i++ ) {
            sum += getItem(i).getDuration();
        }
        return sum;
    }

    public boolean isEmpty() { return getCount() == 0; }

    @SuppressWarnings("serial")
    public List<Task> getRemainedTasks(final SQLiteDatabase db) {
        return new ArrayList<Task>() {{
                for ( Task task : Task.findAll(db) ) {
                    if ( !TaskSummaryAdapter.this.contains(task) ) add(task);
                }
            }};
    }

    public void saveToSharedPreference(final Context context) {
        SharedPreferences pref =
            context.getSharedPreferences("taskSummaryAdapter",
                                         Context.MODE_PRIVATE|Context.MODE_WORLD_WRITEABLE);
        Editor e = pref.edit();
        if ( isEmpty() ) {
            e.clear();
        } else {
            int count = getCount();
            StringBuilder dailyTaskSummaries = new StringBuilder();
            for ( int i = 0 ; i < count ; i++ ) {
                getItem(i).saveToSharedPreference(context);
                dailyTaskSummaries.append(getItem(i));
                if ( i < count - 1) dailyTaskSummaries.append(":");
            }
            e.putString("dailyTaskSummaries", dailyTaskSummaries.toString());
        }
        e.commit();
    }

    public void restoreFromSharedPreference(Context context) {
        SharedPreferences pref =
            context.getSharedPreferences("taskSummaryAdapter",
                                         Context.MODE_PRIVATE|Context.MODE_WORLD_READABLE);
        String dailyTaskSummaries = pref.getString("dailyTaskSummaries", null);
        if ( dailyTaskSummaries != null ) {
            setDailyTaskSummaries(
                DailyTaskSummary.restoreFromSharedPreference(context, dailyTaskSummaries));
        }
    }
}
