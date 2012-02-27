package chokoapp.imanani;

import java.util.ArrayList;
import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class MonthlySummaryAdapter extends ArrayAdapter<MonthlyWork> {
    private static final int resourceId = R.layout.monthly_work_summary_item;
    private LayoutInflater inf;
    private SQLiteDatabase db;

    public MonthlySummaryAdapter(Context context) {
        this(context, new ArrayList<MonthlyWork>());
    }
    public MonthlySummaryAdapter(Context context, List<MonthlyWork> monthlyWorks) {
        super(context, resourceId, monthlyWorks);
        inf = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        db = (new DBOpenHelper(context)).getReadableDatabase();
    }

    public void setList(List<MonthlyWork> monthlyWorks) {
        for (MonthlyWork monthlyWork : monthlyWorks) {
            add(monthlyWork);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MonthlyWork work = getItem(position);
        if ( convertView == null ) {
            convertView = inf.inflate(resourceId, null);
        }

        TextView tvCode = (TextView)convertView.findViewById(R.id.monthlyWorkSummaryListCode);
        tvCode.setText(work.getCode());

        TextView tvDescription = (TextView)convertView.findViewById(R.id.monthlyWorkSummaryListDescription);
        tvDescription.setText(work.getDescription());

        TextView tvDuration = (TextView)convertView.findViewById(R.id.monthlyWorkSummaryListDuration);
        tvDuration.setText(TimeUtils.getMonthlyTimeString(getContext(), work.getDuration()));

        TextView tvPercent = (TextView)convertView.findViewById(R.id.monthlyWorkSummaryListPercent);
        tvPercent.setText(Integer.toString(getPercent(position)) + "%");

        return convertView;
    }

    public void queryWorks(int year, int month) {
        List<MonthlyWork> monthlyWorks = MonthlyWork.queryWorks(db, year, month);
        clear();
        setList(monthlyWorks);
    }

    public long getTotalDuration() {
        long totalDuration = 0;
        int count = getCount();
        for (int i = 0 ; i < count ; i++) {
            totalDuration += getItem(i).getDuration();
        }
        return totalDuration;
    }

    private int getPercent(int position) {
        if (getTotalDuration() <= 0) return -1;

        if (position < getCount() - 1) {
            return (int)Math.round( (getItem(position).getDuration() * 100.0) / getTotalDuration() );
        } else {
            return 100 - lastPercent();
        }
    }

    private int lastPercent() {
        if (getTotalDuration() <= 0) return 0;

        int percent = 0;
        int count = getCount();
        for (int i = 0 ; i < count - 1 ; i++) {
            percent += getPercent(i);
        }
        return percent;
    }
}