package chokoapp.imanani;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

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

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final DatePicker datePicker = new DatePicker(context);
        final AlertDialog dialog =
            builder.setView(datePicker)
            .setTitle(context.getString(R.string.select_date))
            .setPositiveButton(android.R.string.ok,
                               new DialogInterface.OnClickListener() {
                                   @Override
                                   public void onClick(DialogInterface d, int w) {
                                       setDate(datePicker);
                                   }
                               })
            .setNegativeButton(android.R.string.cancel, null)
            .create();

        super.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ( dateSelected() ) {
                        datePicker.updateDate(getYear(), getMonth(), getDay());
                    }
                    dialog.show();
                }
            });
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

    public void setDate(DatePicker datePicker) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, datePicker.getYear());
        cal.set(Calendar.MONTH, datePicker.getMonth());
        cal.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
        setDate(cal.getTime());
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