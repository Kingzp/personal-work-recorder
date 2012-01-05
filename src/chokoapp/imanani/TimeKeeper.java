package chokoapp.imanani;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.TextView;

/**
 * 勤務時間と作業時間を計って、表示に反映させるクラス。
 */
class TimeKeeper implements Runnable {

    /**
     * 勤務実績のID。
     */
    private long workId;
    /**
     * 勤務開始時刻。
     */
    private long workStartTime;
    /**
     * 作業開始時刻。
     */
    private long taskStartTime;

    /**
     * 勤務時間表示用View。
     */
    private TextView workTimeView;
    /**
     * 作業時間表示用View。
     */
    private TextView taskTimeView;

    /**
     * DB.
     */
    private SQLiteDatabase db;

    /**
     * 記録している作業の tasks._id
     */
    private long currentTaskId;

    public TimeKeeper(TextView workTimeView, TextView taskTimeView) {
        this.workTimeView = workTimeView;
        this.taskTimeView = taskTimeView;
        this.currentTaskId = -1;
    }

    /**
     * 勤務開始。
     */
    public void beginWork(Task task) {
        workStartTime = System.currentTimeMillis();
        db.execSQL("insert into work_records (start_time) values(?)",
                new Object[] { workStartTime });
        Cursor cursor = db.rawQuery("select last_insert_rowid()", null);
        try {
            cursor.moveToFirst();
            workId = cursor.getLong(0);
            changeTask(task);
        } finally {
            cursor.close();
        }
    }

    /**
     * 勤務終了。
     */
    public void endWork() {
        if (workStartTime != 0) {
            db.execSQL("update work_records set end_time = ? where _id = ?",
                    new Object[] { System.currentTimeMillis(), workId });
            workStartTime = taskStartTime = 0;
            workId = 0;
        }
    }

    /**
     * 作業変更。
     */
    public void changeTask(Task task) {
        if (workStartTime == 0) {
            // 勤務が開始していなければ、作業も開始しない。
            return;
        }
        if ( currentTaskId == task.getId() ) {
            return;
        }
        taskStartTime = System.currentTimeMillis();
        db.execSQL("insert into task_records"
                + "(work_id, start_time, code, description)"
                + "values(?,?,?,?)", new Object[] { workId, taskStartTime,
                                                    task.getCode(), task.getDescription() });
        currentTaskId = task.getId();
    }

    /**
     * 勤務時間と作業時間を表示に反映させる。
     */
    public void run() {
        long now = System.currentTimeMillis();
        if (workStartTime != 0) {
            workTimeView.setText(format(now - workStartTime));
        }
        if (taskStartTime != 0) {
            taskTimeView.setText(format(now - taskStartTime));
        }
    }

    /**
     * 時間のフォーマッティング。
     *
     * @param time
     *            ミリ秒単位の時間。
     * @return H:MM:SS形式の文字列。
     */
    public String format(long time) {
        long sec = time / 1000;
        long min = sec / 60;
        long hour = min / 60;
        return String.format("%02d:%02d:%02d", hour, min % 60, sec % 60);
    }

    public void init(SQLiteDatabase db) {
        this.db = db;
        Cursor workCursor = db.rawQuery("select _id, start_time"
                + " from work_records" + " where end_time is null", null);
        try {
            if (!workCursor.moveToFirst()) {
                return;
            }
            workId = workCursor.getLong(0);
            taskStartTime = workStartTime = workCursor.getLong(1);
            Cursor taskCursor = db.rawQuery("select start_time, code"
                    + " from task_records" + " where work_id = ?"
                    + " order by start_time desc",
                    new String[] { String.valueOf(workId) });
            try {
                if (!taskCursor.moveToFirst()) {
                    return;
                }
                taskStartTime = taskCursor.getLong(0);
                // task_records には tasks の主キーを持ってないので苦肉の策
                Task currentTask = Task.findByCode(db, taskCursor.getString(1));
                currentTaskId = currentTask == null ? -1 : currentTask.getId();
            } finally {
                taskCursor.close();
            }
        } finally {
            workCursor.close();
        }
    }

    public boolean nowRecording() {
        return workStartTime != 0;
    }

    public long getCurrentTaskId() {
        return currentTaskId;
    }
}