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
    private DateButton dateSelectButton;

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
        builder.setPositiveButton(android.R.string.ok, new DisplaySummary());
        builder.setNegativeButton(android.R.string.cancel, null);
        dateSelector = builder.create();

        dateSelectButton = (DateButton)findViewById(R.id.dateSelectButton);
    }

    public void selectDate(View v) {
        datePicker.updateDate(dateSelectButton.getYear(),
                              dateSelectButton.getMonth(),
                              dateSelectButton.getDay());
        dateSelector.show();
    }

    private class DisplaySummary implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, datePicker.getYear());
            cal.set(Calendar.MONTH, datePicker.getMonth());
            cal.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
            dateSelectButton.setDate(cal.getTime());
        }
    }
}
