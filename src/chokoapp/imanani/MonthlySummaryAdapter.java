package chokoapp.imanani;

import java.util.ArrayList;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.app.Activity;
import android.content.SharedPreferences;

public class MonthlySummaryAdapter extends BaseAdapter {

    private Activity context;
    private ArrayList<MonthlyWork> list;

    public MonthlySummaryAdapter(Activity context) {
        super();
        this.context = context;
    }

    public MonthlySummaryAdapter(Activity context, ArrayList<MonthlyWork> list) {
        super();
        this.context = context;
        this.list = list;
    }

    public int getCount() {
        if (list == null) {
            return 0;
        } else {
            return list.size();
        }
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void setList(ArrayList<MonthlyWork> list) {
        this.list = list;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewgroup) {

        MonthlyWork work = (MonthlyWork)getItem(i);
        view = context.getLayoutInflater().inflate(R.layout.monthly_work_summary_item, null);

        TextView tvCode = (TextView)view.findViewById(R.id.monthlyWorkSummaryListCode);
        tvCode.setText(work.getCode());

        TextView tvDescription = (TextView)view.findViewById(R.id.monthlyWorkSummaryListDescription);
        tvDescription.setText(work.getDescription());

        TextView tvDuration = (TextView)view.findViewById(R.id.monthlyWorkSummaryListDuration);
        tvDuration.setText(TimeUtils.getMonthlyTimeString(context, work.getDuration()));

        TextView tvPercent = (TextView)view.findViewById(R.id.monthlyWorkSummaryListPercent);
        tvPercent.setText(Integer.toString(work.getPercent()) + "%");

        return view;
    }

}