package ru.arkuzmin.dais.dto;

public class Status {
	private String taxiMark;
	private String status;
	private Order order;
	public String getTaxiMark() {
		return taxiMark;
	}
	public void setTaxiMark(String taxiMark) {
		this.taxiMark = taxiMark;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	} 
}
