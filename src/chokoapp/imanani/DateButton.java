package chokoapp.imanani;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

public class DateButton extends Button {
    private Date date;
    private SimpleDateFormat df;

    public DateButton(Context context) {
        this(context, null);
    }

    public DateButton(Context context, AttributeSet attr) {
        super(context, attr);
        date = null;
        int dateformatValue = attr.getAttributeResourceValue(null, "app.dateformat", -1);
        if ( dateformatValue <  0 ) {
            df = new SimpleDateFormat();
        } else {
            df = new SimpleDateFormat(getResources().getText(dateformatValue).toString());
        }
    }

    public boolean dateSelected() {
        return date != null;
    }

    public void setDate(Date date) {
        this.date = date;
        if ( date != null ) {
            setText(df.format(date));
        } else {
            setText(getContext().getString(R.string.select_date));
        }
    }

    public int getYear() {
        if ( dateSelected() ) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return cal.get(Calendar.YEAR);
        } else {
            return 9999;
        }
    }

    public int getMonth() {
        if ( dateSelected() ) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return cal.get(Calendar.MONTH);
        } else {
            return 99;
        }
    }

    public int getDay() {
        if ( dateSelected() ) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return cal.get(Calendar.DAY_OF_MONTH);
        } else {
            return 99;
        }
    }

    public long getTime() {
        return dateSelected() ? date.getTime() : -1;
    }
}