package chokoapp.imanani;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Handler;

/**
 * �P�b���Ƃɏ��������s����N���X�B
 * �w�肳�ꂽ�������A���C���X���b�h�Ŏ��s���邽�߂�Handler���o�R����B
 */
public class Ticker {

	private Timer timer;
	private TickerTask task;

	public Ticker(Runnable proc) {
		task = new TickerTask(proc);
	}

	/**
	 * �����̒�����s���J�n�B
	 */
	public void start() {
		// ���ɓ����Ă�����~�߂�B
		stop();
		// �V�����^�C�}�[�𐶐����ăX�P�W���[�����O�B
		timer = new Timer();
		timer.scheduleAtFixedRate(task,	0, 1000);
	}

	/**
	 * �����̒�����s���I�����A�^�C�}�[������B
	 */
	public void stop() {
		// �^�C�}�[�����݂�����~�߂ĉ���B
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}

	/**
	 * �������^�C�}�[�ɓn�����߂�TimerTask�̃T�u�N���X�B
	 * ������Android��Handler�o�R�Ŏ��s����Ƃ��낪�~�\�B
	 */
	private class TickerTask extends TimerTask {

		/**
		 * ���C���X���b�h�Ƃ̋��n��������n���h���B
		 */
		Handler handler = new Handler();
		/**
		 * ����I�Ɏ��s�����������B
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
			// ���C���X���b�h�ɏ�������������B
			handler.post(proc);
		}
		
	}
}
