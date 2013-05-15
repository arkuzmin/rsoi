
package ru.arkuzmin.dais.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for order complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="order">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="address" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="comfortClass" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="coordinates" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="deliveryTime" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="kmPrice" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="minPrice" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="orderDetailsGUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="orderDt" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="orderGUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="orderStatus" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="requesterGUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "order", propOrder = {
    "address",
    "comfortClass",
    "coordinates",
    "deliveryTime",
    "kmPrice",
    "minPrice",
    "orderDetailsGUID",
    "orderDt",
    "orderGUID",
    "orderStatus",
    "requesterGUID"
})
public class Order {

    protected String address;
    protected String comfortClass;
    protected String coordinates;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar deliveryTime;
    protected Double kmPrice;
    protected Double minPrice;
    protected String orderDetailsGUID;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar orderDt;
    protected String orderGUID;
    protected String orderStatus;
    protected String requesterGUID;

    /**
     * Gets the value of the address property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the value of the address property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddress(String value) {
        this.address = value;
    }

    /**
     * Gets the value of the comfortClass property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComfortClass() {
        return comfortClass;
    }

    /**
     * Sets the value of the comfortClass property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComfortClass(String value) {
        this.comfortClass = value;
    }

    /**
     * Gets the value of the coordinates property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCoordinates() {
        return coordinates;
    }

    /**
     * Sets the value of the coordinates property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCoordinates(String value) {
        this.coordinates = value;
    }

    /**
     * Gets the value of the deliveryTime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDeliveryTime() {
        return deliveryTime;
    }

    /**
     * Sets the value of the deliveryTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDeliveryTime(XMLGregorianCalendar value) {
        this.deliveryTime = value;
    }

    /**
     * Gets the value of the kmPrice property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getKmPrice() {
        return kmPrice;
    }

    /**
     * Sets the value of the kmPrice property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setKmPrice(Double value) {
        this.kmPrice = value;
    }

    /**
     * Gets the value of the minPrice property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getMinPrice() {
        return minPrice;
    }

    /**
     * Sets the value of the minPrice property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setMinPrice(Double value) {
        this.minPrice = value;
    }

    /**
     * Gets the value of the orderDetailsGUID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrderDetailsGUID() {
        return orderDetailsGUID;
    }

    /**
     * Sets the value of the orderDetailsGUID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrderDetailsGUID(String value) {
        this.orderDetailsGUID = value;
    }

    /**
     * Gets the value of the orderDt property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getOrderDt() {
        return orderDt;
    }

    /**
     * Sets the value of the orderDt property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setOrderDt(XMLGregorianCalendar value) {
        this.orderDt = value;
    }

    /**
     * Gets the value of the orderGUID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrderGUID() {
        return orderGUID;
    }

    /**
     * Sets the value of the orderGUID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrderGUID(String value) {
        this.orderGUID = value;
    }

    /**
     * Gets the value of the orderStatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrderStatus() {
        return orderStatus;
    }

    /**
     * Sets the value of the orderStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrderStatus(String value) {
        this.orderStatus = value;
    }

    /**
     * Gets the value of the requesterGUID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequesterGUID() {
        return requesterGUID;
    }

    /**
     * Sets the value of the requesterGUID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequesterGUID(String value) {
        this.requesterGUID = value;
    }

}
