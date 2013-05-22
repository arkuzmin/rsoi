package ru.arkuzmin.dais.timer;

import java.util.Timer;

public class StatusCache {
	public static void cacheStatus(int seconds, Object dest, String correlationID) {
		Timer timer = new Timer();
		timer.schedule(new StatusCacheTask(dest, correlationID, seconds), seconds * 1000);
	}
}
