package ru.arkuzmin.tpw.dto;

public class Order {
	private String orderGuid;
	private String fullName;
	private String address;
	private String deliveryTime;
	private String minPrice;
	private String kmPrice;
	private String comfortClass;
	
	public String getOrderGuid() {
		return orderGuid;
	}
	public void setOrderGuid(String orderGuid) {
		this.orderGuid = orderGuid;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getDeliveryTime() {
		return deliveryTime;
	}
	public void setDeliveryTime(String deliveryTime) {
		this.deliveryTime = deliveryTime;
	}
	public String getMinPrice() {
		return minPrice;
	}
	public void setMinPrice(String minPrice) {
		this.minPrice = minPrice;
	}
	public String getKmPrice() {
		return kmPrice;
	}
	public void setKmPrice(String kmPrice) {
		this.kmPrice = kmPrice;
	}
	public String getComfortClass() {
		return comfortClass;
	}
	public void setComfortClass(String comfortClass) {
		this.comfortClass = comfortClass;
	}
}
