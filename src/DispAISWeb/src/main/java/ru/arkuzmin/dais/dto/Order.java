package ru.arkuzmin.dais.dto;

import java.io.Serializable;
import java.util.Date;


public class Order implements Serializable{
	
	private static final long serialVersionUID = -3996202705889131436L;
	
	private String requesterGUID;
	private String orderGUID;
	private String orderDetailsGUID;
	private Date orderDt;
	private String address;
	private Date deliveryTime;
	private Double kmPrice;
	private Double minPrice;
	private String comfortClass;
	private String orderStatus;
	private String coordinates;
	
	public String getRequesterGUID() {
		return requesterGUID;
	}
	public void setRequesterGUID(String requesterGUID) {
		this.requesterGUID = requesterGUID;
	}
	public String getOrderGUID() {
		return orderGUID;
	}
	public void setOrderGUID(String orderGUID) {
		this.orderGUID = orderGUID;
	}
	public Date getOrderDt() {
		return orderDt;
	}
	public void setOrderDt(Date orderDt) {
		this.orderDt = orderDt;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Date getDeliveryTime() {
		return deliveryTime;
	}
	public void setDeliveryTime(Date deliveryTime) {
		this.deliveryTime = deliveryTime;
	}
	public Double getKmPrice() {
		return kmPrice;
	}
	public void setKmPrice(Double kmPrice) {
		this.kmPrice = kmPrice;
	}
	public Double getMinPrice() {
		return minPrice;
	}
	public void setMinPrice(Double minPrice) {
		this.minPrice = minPrice;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getCoordinates() {
		return coordinates;
	}
	public void setCoordinates(String coordinates) {
		this.coordinates = coordinates;
	}
	public String getOrderDetailsGUID() {
		return orderDetailsGUID;
	}
	public void setOrderDetailsGUID(String orderDetailsGUID) {
		this.orderDetailsGUID = orderDetailsGUID;
	}
	public String getComfortClass() {
		return comfortClass;
	}
	public void setComfortClass(String comfortClass) {
		this.comfortClass = comfortClass;
	}
}
