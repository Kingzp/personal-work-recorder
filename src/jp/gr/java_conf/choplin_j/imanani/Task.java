package jp.gr.java_conf.choplin_j.imanani;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Task {
    public static final String TABLE_NAME = "tasks";
    public static final String[] COLUMNS = {
        "code TEXT",
        "description TEXT"
    };

    private long _id;
    private String code;
    private String description;

    public Task(String code, String description) {
        this.code = code;
        this.description = description;
    }
    public Task(long id, String code, String description) {
        this._id = id;
        this.code = code;
        this.description = description;
    }
    public long getId() { return _id; }
    public String getCode() { return code; }
    public String getDescription() { return description; }

    static public Task findByCode(SQLiteDatabase db, String code) {
        Cursor task_cursor = db.query(TABLE_NAME,
                                      new String[] { "_id", "code", "description" },
                                      "code = ?",
                                      new String[] { code },
                                      null, null, null);
        try {
            if ( task_cursor.moveToFirst() ) {
                return new Task(task_cursor.getLong(0),
                                task_cursor.getString(1),
                                task_cursor.getString(2));
            } else {
                return null;
            }
        } finally {
            task_cursor.close();
        }
    }

    static public List<Task> findAll(SQLiteDatabase db) {
        List<Task> all = new ArrayList<Task>();
        Cursor taskCursor = db.query(TABLE_NAME,
                                      new String[] {"_id", "code", "description" },
                                      null, null, null, null, "code");
        while ( taskCursor.moveToNext() ) {
            all.add(new Task(taskCursor.getLong(0),
                             taskCursor.getString(1),
                             taskCursor.getString(2)));
        }
        try {
            return all;
        } finally {
            taskCursor.close();
        }
    }

    public boolean save(SQLiteDatabase db) {
        ContentValues val = new ContentValues();
        val.put("code", getCode());
        val.put("description", getDescription());
        long id = db.insert(TABLE_NAME, null, val);
        if ( id >= 0 ) {
            this._id = id;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return code + ":" + description;
    }
}
