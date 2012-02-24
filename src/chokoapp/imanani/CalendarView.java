package chokoapp.imanani;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;


public class CalendarView extends LinearLayout {
    private LayoutInflater inf;
    private TableRow[] tableRows = new TableRow[6];
    private CalendarDateView[][] calendarDates = new CalendarDateView[6][7];
    private OnMonthSelectListener listener;
    private View topLayout;

    public interface OnMonthSelectListener {
        public void onSelectMonth(int year, int month);
    }

    public CalendarView(Context context) {
        this(context, null);
    }
    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inf = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        topLayout = inf.inflate(R.layout.calendar, this);

        tableRows[0] = (TableRow)topLayout.findViewById(R.id.firstWeekRow);
        tableRows[1] = (TableRow)topLayout.findViewById(R.id.secondWeekRow);
        tableRows[2] = (TableRow)topLayout.findViewById(R.id.thirdWeekRow);
        tableRows[3] = (TableRow)topLayout.findViewById(R.id.fourthWeekRow);
        tableRows[4] = (TableRow)topLayout.findViewById(R.id.fifthWeekRow);
        tableRows[5] = (TableRow)topLayout.findViewById(R.id.sixthWeekRow);

        Field[] idFields = R.id.class.getFields();
        for (Field field : idFields) {
            Pattern calendarDatePattern = Pattern.compile("calendarDate(\\d)(\\d)");
            Matcher match = calendarDatePattern.matcher(field.getName());
            if (match.matches()) {
                int row = Integer.parseInt(match.group(1));
                int col = Integer.parseInt(match.group(2));
                try {
                    calendarDates[row][col] = (CalendarDateView)topLayout.findViewById(field.getInt(null));
                } catch (IllegalArgumentException e) {e.printStackTrace();
                } catch (IllegalAccessException e) {e.printStackTrace();
                }
            }
        }
        Calendar now = Calendar.getInstance();
        setMonth(now.get(Calendar.YEAR), now.get(Calendar.MONTH)+1);
    }

    public void setMonth(int year, int month) {
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
            removeView(tableRows[i]);
        }
    }

    public void setOnMonthSelectListener(OnMonthSelectListener l) { listener = l; }
}