package chokoapp.imanani;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


public class CalendarView extends LinearLayout {
    private LayoutInflater inf;
    private OnMonthSelectListener monthSelectListener = new EmptyMonthSelectListener();
    private OnDateClickListener dateClickListener = new EmptyDateClickListener();
    private LinearLayout monthSelectLayout;
    private int selectedYear;
    private int selectedMonth;
    private TextView monthSelectView;
    private TableLayout calendarContents;
    private TableRow dayOfWeekRow;
    private TimeView totalDurationView;
    private SQLiteDatabase db;

    public interface OnMonthSelectListener {
        public void onSelectMonth(int year, int month);
    }
    private class EmptyMonthSelectListener implements OnMonthSelectListener {
        @Override
        public void onSelectMonth(int year, int month) {}
    }

    public interface OnDateClickListener {
        public void onDateClick(int year, int month, int date);
    }
    private class EmptyDateClickListener implements OnDateClickListener {
        @Override
        public void onDateClick(int year, int month, int date) {}
    }

    public CalendarView(Context context) {
        this(context, null);
    }
    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);

        db = (new DBOpenHelper(context)).getReadableDatabase();
        inf = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout topLayout = (LinearLayout)inf.inflate(R.layout.calendar, this);

        monthSelectView = (TextView)topLayout.findViewById(R.id.monthSelectView);
        calendarContents = (TableLayout)topLayout.findViewById(R.id.calendarContents);

        dayOfWeekRow = (TableRow)inf.inflate(R.layout.day_of_week_row, null, false);

        Calendar now = Calendar.getInstance();
        display(now.get(Calendar.YEAR), now.get(Calendar.MONTH));

        monthSelectLayout = (LinearLayout)topLayout.findViewById(R.id.monthSelectLayout);
        monthSelectLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showMonthPicker(selectedYear, selectedMonth);
                }
            });

        totalDurationView = (TimeView)topLayout.findViewById(R.id.totalDurationView);
    }

    public void display(int year, int month) {
        if ( selectedYear == year && selectedMonth == month) return;

        selectedYear = year;
        selectedMonth = month;

        displayTitle(year, month);

        calendarContents.removeAllViews();
        calendarContents.addView(dayOfWeekRow);
        TableRow.LayoutParams param = new TableRow.LayoutParams();
        param.weight = 1;

        TableRow tableRow = null;

        DateInfo dateInfo = new DateInfo(year, month);
        dateInfo.moveTopCorner();
        do {
            if (dateInfo.isSunday()) {
                tableRow = new TableRow(getContext());
            }
            CalendarDateView dateView = new CalendarDateView(getContext());
            if (dateInfo.getMonth() != month) {
                dateView.setInvalid();
            } else {
                dateView.setBackground(dateInfo.isSunday());
                if ( db != null ) {
                    DailyWorkSummary dailyWorkSummary =
                        DailyWorkSummary.findByDate(db, dateInfo.getTime());
                    List<DailyTaskSummary> dailyTaskSummaries = null;
                    int color = Color.BLACK;
                    if (dailyWorkSummary.isEmpty()) {
                        dailyTaskSummaries = TaskRecord.findByDate(db, dateInfo.getTime());
                        if ( !dailyTaskSummaries.isEmpty() ) {
                            color = Color.parseColor("#8ff0e68c"); /* khaki color */
                        }
                    } else {
                        dailyTaskSummaries = DailyTaskSummary.findById(db, dailyWorkSummary.getId());
                        color = Color.parseColor("#8f90ee90"); /* light green */
                    }
                    long time = 0;
                    for (DailyTaskSummary dailyTaskSummary : dailyTaskSummaries) {
                        time += dailyTaskSummary.getDuration();
                    }
                    dateView.setTime(time);
                    dateView.setTextColor(color);
                    final int date = dateInfo.getDate();
                    dateView.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dateClickListener.onDateClick(selectedYear,
                                                              selectedMonth,
                                                              date);
                            }
                        });
                }
            }
            dateView.setDate(dateInfo.getDate());
            tableRow.addView(dateView, param);
            if (dateInfo.isSaturday()) {
                calendarContents.addView(tableRow);
            }
        } while ( dateInfo.moveToNext() );
    }

    public void setOnMonthSelectListener(OnMonthSelectListener l) { monthSelectListener = l; }
    public void setOnDateClickListener(OnDateClickListener l) { dateClickListener = l; }

    public int getYear() { return selectedYear; }
    public int getMonth() { return selectedMonth; }

    public void setTotalDuration(long time) {
        totalDurationView.setTime(time);
    }

    private void showMonthPicker(int year, int month) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final DatePicker datePicker = new DatePicker(getContext());
        datePicker.updateDate(year, month, 1);
        int day_id = Resources.getSystem().getIdentifier("day", "id", "android");
        datePicker.findViewById(day_id).setVisibility(View.GONE);
        builder.setView(datePicker)
            .setTitle(getContext().getString(R.string.select_month))
            .setPositiveButton(android.R.string.ok,
                               new DialogInterface.OnClickListener() {
                                   @Override
                                   public void onClick(DialogInterface d, int w) {
                                       display(datePicker.getYear(), datePicker.getMonth());
                                       monthSelectListener.onSelectMonth(selectedYear, selectedMonth);
                                   }
                               })
            .setNegativeButton(android.R.string.cancel, null)
            .create()
            .show();
    }

    private void displayTitle(int year, int month) {
        Calendar now = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月");
        now.set(Calendar.YEAR, year);
        now.set(Calendar.MONTH, month);
        monthSelectView.setText(df.format(now.getTime()));
    }
}