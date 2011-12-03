package chokoapp.imanani;

import android.widget.TextView;

/**
 * 勤務時間と作業時間を計って、表示に反映させるクラス。
 */
class TimeKeeper implements Runnable {

    /**
     * 勤務時間。
     */
    private long workStartTime;
    /**
     * 作業時間。
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

    public TimeKeeper(TextView workTimeView, TextView taskTimeView) {
        this.workTimeView = workTimeView;
        this.taskTimeView = taskTimeView;
    }

    /**
     * 勤務開始。
     * 
     * @return 通常は０を返す。勤務を終了せずに再び開始したら、前回開始してから現在までの時間を返す。
     */
    public long beginWork() {
        long now = System.currentTimeMillis();
        try {
            return workStartTime == 0 ? 0 : now - workStartTime;
        } finally {
            workStartTime = taskStartTime = now;
        }
    }

    /**
     * 勤務終了。
     * 
     * @return 勤務開始から現在までの時間を返す。
     */
    public long endWork() {
        long now = System.currentTimeMillis();
        try {
            return workStartTime == 0 ? 0 : now - workStartTime;
        } finally {
            workStartTime = taskStartTime = 0;
        }
    }

    /**
     * 作業変更。
     * 
     * @return 勤務開始または前回の作業変更から現在までの時間を返す。
     */
    public long changeTask() {
        if (workStartTime == 0) {
            // 勤務が開始していなければ、作業も開始しないで０を返す。
            return 0;
        }
        long now = System.currentTimeMillis();
        try {
            return taskStartTime == 0 ? 0 : now - taskStartTime;
        } finally {
            taskStartTime = now;
        }
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

}