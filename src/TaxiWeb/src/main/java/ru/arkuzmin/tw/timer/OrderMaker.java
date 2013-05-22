package ru.arkuzmin.tw.timer;

import java.util.Timer;

import javax.jms.Destination;

public class OrderMaker {

	public static void completeOrder(int seconds, int n, Destination dest, String correlationID) {
		Timer timer = new Timer();
		timer.schedule(new OrderTask(dest, correlationID, n, seconds), seconds * 1000);
	}
}
