package chokoapp.imanani;

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

    static public long findByCode(SQLiteDatabase db, String code) {
        Cursor task_cursor = db.query(TABLE_NAME,
                                      new String[] { "_id" },
                                      "code = ?",
                                      new String[] { code },
                                      null, null, null);
        try {
            if ( task_cursor.moveToFirst() ) {
                return task_cursor.getLong(0);
            } else {
                return -1;
            }
        } finally {
            task_cursor.close();
        }
    }

    public String toString() {
        return code + ":" + description;
    }
}
