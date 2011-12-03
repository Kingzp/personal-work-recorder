package chokoapp.imanani;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Handler;

/**
 * １秒ごとに処理を実行するクラス。 指定された処理を、メインスレッドで実行するためにHandlerを経由する。
 */
public class Ticker {

    private Timer timer;
    private TickerTask task;

    public Ticker(Runnable proc) {
        task = new TickerTask(proc);
    }

    /**
     * 処理の定期実行を開始。
     */
    public void start() {
        // 既に動いていたら止める。
        stop();
        // 新しいタイマーを生成してスケジューリング。
        timer = new Timer();
        timer.scheduleAtFixedRate(task, 0, 1000);
    }

    /**
     * 処理の定期実行を終了し、タイマーを解放。
     */
    public void stop() {
        // タイマーが存在したら止めて解放。
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    /**
     * 処理をタイマーに渡すためのTimerTaskのサブクラス。 処理をAndroidのHandler経由で実行するところがミソ。
     */
    private class TickerTask extends TimerTask {

        /**
         * メインスレッドとの橋渡しをするハンドラ。
         */
        Handler handler = new Handler();
        /**
         * 定期的に実行したい処理。
         */
        Runnable proc;

        private TickerTask(Runnable proc) {
            this.proc = proc;
        }

        /**
         * @see TimerTask#run()
         */
        @Override
        public void run() {
            // メインスレッドに処理をあずける。
            handler.post(proc);
        }

    }
}
