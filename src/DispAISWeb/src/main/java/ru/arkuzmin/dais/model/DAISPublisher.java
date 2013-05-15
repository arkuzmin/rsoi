package ru.arkuzmin.dais.model;

import javax.xml.ws.Endpoint;

public class DAISPublisher {
	private Endpoint endpoint;

	public static void main(String[] args) {
		DAISPublisher self = new DAISPublisher();
		self.create_endpoint();
		self.configure_endpoint();
		self.publish();
	}

	private void create_endpoint() {
		endpoint = Endpoint.create(new DAISWebServiceImpl());
	}

	private void configure_endpoint() {
		endpoint.setExecutor(new DAISThreadPool());
	}

	private void publish() {
		int port = 8888;
		String url = "http://localhost:" + port + "/ts";
		endpoint.publish(url);
		System.out.println("Publishing DAIS on port " + port);
	}
}
