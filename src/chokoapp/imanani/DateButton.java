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
        super(context);
        date = new Date();
        df = new SimpleDateFormat();
    }

    public DateButton(Context context, AttributeSet attr) {
        super(context, attr);
        date = new Date();
        int dateformatValue = attr.getAttributeResourceValue(null, "app.dateformat", -1);
        if ( dateformatValue <  0 ) {
            df = new SimpleDateFormat();
        } else {
            df = new SimpleDateFormat(getResources().getText(dateformatValue).toString());
        }
    }

    public void setDate(Date date) {
        this.date = date;
        setText(df.format(date));
    }

    public String getText() {
        return df.format(date);
    }

    public int getYear() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }

    public int getMonth() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH);
    }

    public int getDay() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    public long getTime() {
        return date.getTime();
    }
}