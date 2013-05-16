package ru.arkuzmin.tw.common;

public class BadMessageException extends Exception {

	private static final long serialVersionUID = -3535938789281778981L;

	public BadMessageException(String msg) {
		super(msg);
	}
}
