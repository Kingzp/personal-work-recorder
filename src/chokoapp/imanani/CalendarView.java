package chokoapp.imanani;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private TableRow[] weekRows = new TableRow[6];
    private CalendarDateView[][] calendarDates = new CalendarDateView[6][7];
    private OnMonthSelectListener listener;
    private LinearLayout monthSelectLayout;
    private int selectedYear;
    private int selectedMonth; /* 1: January, 2: February, 3: March ... */
    private TextView monthSelectView;

    public interface OnMonthSelectListener {
        public void onSelectMonth(int year, int month);
    }

    public CalendarView(Context context) {
        this(context, null);
    }
    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inf = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View topLayout = inf.inflate(R.layout.calendar, this);

        monthSelectView = (TextView)topLayout.findViewById(R.id.monthSelectView);

        Field[] idFields = R.id.class.getFields();
        for (Field field : idFields) {
            Pattern weekRowPattern = Pattern.compile("weekRow(\\d)");
            Matcher matchRow = weekRowPattern.matcher(field.getName());
            Pattern calendarDatePattern = Pattern.compile("calendarDate(\\d)(\\d)");
            Matcher matchDate = calendarDatePattern.matcher(field.getName());
            if (matchRow.matches()) {
                int row = Integer.parseInt(matchRow.group(1));
                try {
                    weekRows[row] = (TableRow)topLayout.findViewById(field.getInt(null));
                } catch (IllegalArgumentException e) {e.printStackTrace();
                } catch (IllegalAccessException e) {e.printStackTrace();
                }
            }
            if (matchDate.matches()) {
                int row = Integer.parseInt(matchDate.group(1));
                int col = Integer.parseInt(matchDate.group(2));
                try {
                    calendarDates[row][col] = (CalendarDateView)topLayout.findViewById(field.getInt(null));
                } catch (IllegalArgumentException e) {e.printStackTrace();
                } catch (IllegalAccessException e) {e.printStackTrace();
                }
            }
        }
        Calendar now = Calendar.getInstance();
        display(now.get(Calendar.YEAR), now.get(Calendar.MONTH)+1);

        monthSelectLayout = (LinearLayout)topLayout.findViewById(R.id.monthSelectLayout);
        monthSelectLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showMonthPicker(selectedYear, selectedMonth);
                }
            });
    }

    public void display(int year, int month) {
        selectedYear = year;
        selectedMonth = month;

        displayTitle(year, month);

        TableLayout.LayoutParams param = new TableLayout.LayoutParams();
        param.weight = 1;

        DateInfo dateInfo = new DateInfo(year, month);
        dateInfo.moveTopCorner();
        do {
            int row = dateInfo.getRow();
            int col = dateInfo.getColumn();
            calendarDates[row][col].setDate(dateInfo.getDate());
            if (dateInfo.getMonth() != month) {
                calendarDates[row][col].setInvalid();
            } else {
                calendarDates[row][col].setBackground(dateInfo.isSunday());
            }
        } while (dateInfo.moveToNext());
        for ( int i = dateInfo.getRow() + 1 ; i < 6 ; i++ ) {
            removeView(weekRows[i]);
        }
    }

    public void setOnMonthSelectListener(OnMonthSelectListener l) { listener = l; }

    public int getYear() { return selectedYear; }
    public int getMonth() { return selectedMonth; }

    private void showMonthPicker(int year, int month) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final DatePicker datePicker = new DatePicker(getContext());
        datePicker.updateDate(year, month-1, 1);
        int day_id = Resources.getSystem().getIdentifier("day", "id", "android");
        datePicker.findViewById(day_id).setVisibility(View.GONE);
        builder.setView(datePicker)
            .setTitle(getContext().getString(R.string.select_month))
            .setPositiveButton(android.R.string.ok,
                               new DialogInterface.OnClickListener() {
                                   @Override
                                   public void onClick(DialogInterface d, int w) {
                                       display(datePicker.getYear(), datePicker.getMonth()+1);
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
        now.set(Calendar.MONTH, month-1);
        monthSelectView.setText(df.format(now.getTime()));
   }
}