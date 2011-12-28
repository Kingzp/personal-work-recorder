package chokoapp.imanani;

import java.util.Calendar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

public class DailySummaryActivity extends ListActivity {
    private SQLiteDatabase db;
    private Cursor summaryCursor;
    private DatePicker datePicker;
    private AlertDialog dateSelector;
    private Calendar selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daily_summay);
        db = (new DBOpenHelper(this)).getWritableDatabase();
        summaryCursor = db.rawQuery("SELECT _id, code, description, '99:99:99' FROM task_records", null);

        setListAdapter(new TaskSummaryAdapter(this, summaryCursor));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        datePicker = new DatePicker(this);
        builder.setView(datePicker);
        builder.setTitle(getResources().getString(R.string.select_date));
        builder.setPositiveButton(android.R.string.ok, new DisplaySummary(this));
        builder.setNegativeButton(android.R.string.cancel, null);
        dateSelector = builder.create();

        selectedDate = Calendar.getInstance();
    }

    public void selectDate(View v) {
        datePicker.updateDate(selectedDate.get(Calendar.YEAR),
                              selectedDate.get(Calendar.MONTH),
                              selectedDate.get(Calendar.DATE));
        dateSelector.show();
    }

    private String getSelectedDate() {
        if (datePicker != null) {
            return String.format("%04d年%02d月%02d日",
                                 datePicker.getYear(),
                                 datePicker.getMonth() + 1,
                                 datePicker.getDayOfMonth());
        } else {
            return "9999年99月99日";
        }
    }

    private void updateSelectedDate() {
        selectedDate.set(Calendar.YEAR, datePicker.getYear());
        selectedDate.set(Calendar.MONTH, datePicker.getMonth());
        selectedDate.set(Calendar.DATE, datePicker.getDayOfMonth());
    }

    private class DisplaySummary implements DialogInterface.OnClickListener {
        private Activity act;

        public DisplaySummary(Activity act) {
            this.act = act;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            Button dateSelectButton = (Button)act.findViewById(R.id.dateSelectButton);
            dateSelectButton.setText(getSelectedDate());
            updateSelectedDate();
            // TODO
            Toast.makeText(act, "NOT YET IMPLEMENTED", Toast.LENGTH_SHORT).show();
        }
    }
}
