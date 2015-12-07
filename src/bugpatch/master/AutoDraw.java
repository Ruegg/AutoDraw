package bugpatch.master;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;

public class AutoDraw {

	public static JFrame drawGUI;

	public static int runningTime = 0;

	public static HashMap<Integer, String> history = new HashMap<Integer, String>();

	public static boolean isDrawing = false;

	public static void main(String[] args) {
		drawGUI = new DrawGUI();

		ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
		ses.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				addRunningTime();
			}
		}, 0, 1, TimeUnit.SECONDS);
	}

	public static void addRunningTime() {
		runningTime++;
	}
}
