package chokoapp.imanani;

import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

public class DailySummaryActivity extends ListActivity {
    SQLiteDatabase db;
    Cursor summaryCursor;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daily_summay);
        db = (new DBOpenHelper(this)).getWritableDatabase();
        summaryCursor = db.rawQuery("SELECT _id, code, description, '99:99:99' FROM task_records", null);

        setListAdapter(new TaskSummaryAdapter(this, summaryCursor));
    }

}
