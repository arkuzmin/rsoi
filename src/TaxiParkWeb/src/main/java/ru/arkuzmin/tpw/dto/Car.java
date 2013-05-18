package ru.arkuzmin.tpw.dto;

public class Car {

	private String carGuid;
	private String carMark;
	private Double minPrice;
	private Double kmPrice;
	private String comfortClass;
	private String queueID;
	public String getCarGuid() {
		return carGuid;
	}
	public void setCarGuid(String carGuid) {
		this.carGuid = carGuid;
	}
	public String getCarMark() {
		return carMark;
	}
	public void setCarMark(String carMark) {
		this.carMark = carMark;
	}
	public Double getMinPrice() {
		return minPrice;
	}
	public void setMinPrice(Double minPrice) {
		this.minPrice = minPrice;
	}
	public Double getKmPrice() {
		return kmPrice;
	}
	public void setKmPrice(Double kmPrice) {
		this.kmPrice = kmPrice;
	}
	public String getComfortClass() {
		return comfortClass;
	}
	public void setComfortClass(String comfortClass) {
		this.comfortClass = comfortClass;
	}
	public String getQueueID() {
		return queueID;
	}
	public void setQueueID(String queueID) {
		this.queueID = queueID;
	}
}
