package chokoapp.imanani;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
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
    private int currentYear;
    private int currentMonth;
    private TextView monthSelectView;
    private TableLayout calendarContents;
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

        Calendar now = Calendar.getInstance();
        setMonth(now.get(Calendar.YEAR), now.get(Calendar.MONTH));

        monthSelectLayout = (LinearLayout)topLayout.findViewById(R.id.monthSelectLayout);
        monthSelectLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showMonthPicker(currentYear, currentMonth);
                }
            });

        totalDurationView = (TimeView)topLayout.findViewById(R.id.totalDurationView);
    }

    public void setMonth(int year, int month) {
        if (currentYear == year && currentMonth == month) return;

        currentYear = year;
        currentMonth = month;

        displayCalendar();
    }

    public void setOnMonthSelectListener(OnMonthSelectListener l) { monthSelectListener = l; }
    public void setOnDateClickListener(OnDateClickListener l) { dateClickListener = l; }

    public int getYear() { return currentYear; }
    public int getMonth() { return currentMonth; }

    public void setTotalDuration(long time) {
        totalDurationView.setTime(time);
    }

    public void updateCell(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        if (currentYear != cal.get(Calendar.YEAR) ||
            currentMonth != cal.get(Calendar.MONTH)) return;

        DateInfo dateInfo = new DateInfo(cal.get(Calendar.YEAR),
                                         cal.get(Calendar.MONTH),
                                         cal.get(Calendar.DATE));
        View row = calendarContents.getChildAt(dateInfo.getRow() + 1);
        if (row != null && row instanceof TableRow) {
            View cell = ((TableRow)row).getChildAt(dateInfo.getColumn());
            if (cell != null && cell instanceof CalendarDateView) {
                ((CalendarDateView)cell).setDate(date, db);
            }
        }
    }

    private void displayCalendar() {

        displayTitle();

        calendarContents.removeAllViews();
        TableRow dayOfWeekRow = (TableRow)inf.inflate(R.layout.day_of_week_row, null, false);
        calendarContents.addView(dayOfWeekRow);
        TableRow.LayoutParams param = new TableRow.LayoutParams();
        param.weight = 1;

        TableRow tableRow = null;

        DateInfo dateInfo = new DateInfo(currentYear, currentMonth);
        dateInfo.moveTopCorner();
        do {
            if (dateInfo.isSunday()) {
                tableRow = new TableRow(getContext());
            }
            CalendarDateView dateView = new CalendarDateView(getContext());
            if (dateInfo.getMonth() != currentMonth) {
                dateView.setInvalid();
                dateView.setDate(dateInfo.getDate());
            } else {
                dateView.setBackground(dateInfo.isSunday());
                dateView.setDate(dateInfo.getTime(), db);
                final int date = dateInfo.getDate();
                dateView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dateClickListener.onDateClick(currentYear,
                                                          currentMonth,
                                                          date);
                        }
                    });
            }
            tableRow.addView(dateView, param);
            if (dateInfo.isSaturday()) {
                calendarContents.addView(tableRow);
            }
        } while ( dateInfo.moveToNext() );
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
                                       setMonth(datePicker.getYear(), datePicker.getMonth());
                                       monthSelectListener.onSelectMonth(currentYear, currentMonth);
                                   }
                               })
            .setNegativeButton(android.R.string.cancel, null)
            .create()
            .show();
    }

    private void displayTitle() {
        Calendar now = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月");
        now.set(Calendar.YEAR, currentYear);
        now.set(Calendar.MONTH, currentMonth);
        monthSelectView.setText(df.format(now.getTime()));
    }
}