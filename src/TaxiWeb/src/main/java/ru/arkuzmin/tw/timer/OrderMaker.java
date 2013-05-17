package ru.arkuzmin.tw.timer;

import java.util.Timer;

import javax.jms.Destination;

public class OrderMaker {

	public static void completeOrder(int seconds, Destination dest, String correlationID) {
		Timer timer = new Timer();
		timer.schedule(new OrderTask(dest, correlationID), seconds * 1000);
	}
}
