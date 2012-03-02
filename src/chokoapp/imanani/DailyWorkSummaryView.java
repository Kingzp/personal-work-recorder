package chokoapp.imanani;

import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

public class DailyWorkSummaryView extends RelativeLayout implements Observer {
    private DailyWorkSummary dailyWorkSummary;
    private DateTimeView startTimeView;
    private DateTimeView endTimeView;
    private TimeView totalTimeView;

    public DailyWorkSummaryView(Context context) {
        this(context, null, 0);
    }

    public DailyWorkSummaryView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DailyWorkSummaryView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater inf = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inf.inflate(R.layout.daily_work_summary, this);
        startTimeView = (DateTimeView)view.findViewById(R.id.startTimeView);
        endTimeView = (DateTimeView)view.findViewById(R.id.endTimeView);
        totalTimeView = (TimeView)view.findViewById(R.id.totalTimeView);
    }

    public void setDailyWorkSummary(final DailyWorkSummary dailyWorkSummary) {
        this.dailyWorkSummary = dailyWorkSummary;
        dailyWorkSummary.addObserver(this);

        ((ManipulateButton)findViewById(R.id.startTimeUp))
            .setManipulator(new Manipulator() {
                    @Override
                    public void execute() {
                        dailyWorkSummary.startTimeUp();
                    }
                });
        ((ManipulateButton)findViewById(R.id.startTimeDown))
            .setManipulator(new Manipulator() {
                    @Override
                    public void execute() {
                        dailyWorkSummary.startTimeDown();
                    }
                });

        ((ManipulateButton)findViewById(R.id.endTimeUp))
            .setManipulator(new Manipulator() {
                    @Override
                    public void execute() {
                        dailyWorkSummary.endTimeUp();
                    }
                });
        ((ManipulateButton)findViewById(R.id.endTimeDown))
            .setManipulator(new Manipulator() {
                    @Override
                    public void execute() {
                        dailyWorkSummary.endTimeDown();
                    }
                });
    }

    @Override
    public void update(Observable o, Object arg) {
        if ( dailyWorkSummary.isEmpty() ) {
            startTimeView.clearTime();
            endTimeView.clearTime();
        } else {
            if ( dailyWorkSummary.nowRecording() ) {
                startTimeView.setTime(dailyWorkSummary.getStartAt());
                endTimeView.clearTime();
            } else {
                startTimeView.setTime(dailyWorkSummary.getStartAt());
                endTimeView.setTime(dailyWorkSummary.getEndAt());
            }
        }

        totalTimeView.setTime(dailyWorkSummary.getTotal());
    }
}
