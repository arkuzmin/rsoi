package ru.arkuzmin.dais.timer;

import java.util.List;
import java.util.Timer;

public class ResendOrder {
	public static void resendOrder(List<String> taxiparks, String correlationID, int seconds) {
		Timer timer = new Timer();
		timer.schedule(new ResendOrderTask(taxiparks, correlationID, seconds), seconds * 1000);
	}
}
