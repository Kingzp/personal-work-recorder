package chokoapp.imanani;

import java.util.Comparator;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ArrayAdapter;

public class TaskCompleteAdapter extends ArrayAdapter<Task> {
    private SQLiteDatabase db;

    public TaskCompleteAdapter(Context context, SQLiteDatabase db) {
        super(context, android.R.layout.simple_dropdown_item_1line,
              Task.findAll(db));
        this.db = db;
    }

    public Task find(String taskCode) {
        int count = getCount();
        for ( int i = 0 ; i < count ; i++ ) {
            if ( getItem(i).getCode().equals(taskCode) ) {
                return getItem(i);
            }
        }
        return null;
    }

    @Override
    public void add(Task task) {
        if ( find(task.getCode()) != null ) return;

        if ( task.save(db) ) {
            super.add(task);
            sort(new Comparator<Task>() {
                    @Override
                    public int compare(Task lhs, Task rhs) {
                        return lhs.getCode().compareTo(rhs.getCode());
                    }
                    @Override
                    public boolean equals(Object obj) {
                        return this.equals(obj);
                    }
                });
        }
    }
}