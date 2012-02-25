package chokoapp.imanani;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
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
    private OnMonthSelectListener listener;
    private LinearLayout monthSelectLayout;
    private int selectedYear;
    private int selectedMonth;
    private TextView monthSelectView;
    private TableLayout calendarContents;
    private TableRow dayOfWeekRow;
    private TimeView totalDurationView;

    public interface OnMonthSelectListener {
        public void onSelectMonth(int year, int month);
    }

    public CalendarView(Context context) {
        this(context, null);
    }
    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
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
            }
            dateView.setDate(dateInfo.getDate());
            tableRow.addView(dateView, param);
            if (dateInfo.isSaturday()) {
                calendarContents.addView(tableRow);
            }
        } while ( dateInfo.moveToNext() );
    }

    public void setOnMonthSelectListener(OnMonthSelectListener l) { listener = l; }

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
                                       if (listener != null ) {
                                           listener.onSelectMonth(selectedYear, selectedMonth);
                                       }
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