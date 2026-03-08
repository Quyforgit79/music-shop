/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import enums.DiscountType;
import enums.OrderStatus;
import enums.PaymentMethod;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 *
 * @author Nguyen Hoang Thai Vinh - CE190384
 */
public class OrderViewModel {

    private int orderId;
    private Timestamp orderDate;
    private OrderStatus status;

    private BigDecimal totalAmount;
    private BigDecimal discountAmount;
    private DiscountType discountType;

    private Address address;

    private PaymentMethod paymentMethod;

    private String trackingNumber;
    private Timestamp estimatedDelivery;

    private List<OrderDetail> orderDetails;

    public OrderViewModel() {
    }

    public OrderViewModel(int orderId, Timestamp orderDate, OrderStatus status, BigDecimal totalAmount, BigDecimal discountAmount, Address address, PaymentMethod paymentMethod, String trackingNumber, Timestamp estimatedDelivery, List<OrderDetail> orderDetails, DiscountType discountType) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.status = status;
        this.totalAmount = totalAmount;
        this.discountAmount = discountAmount;
        this.address = address;
        this.paymentMethod = paymentMethod;
        this.trackingNumber = trackingNumber;
        this.estimatedDelivery = estimatedDelivery;
        this.orderDetails = orderDetails;
        this.discountType = discountType;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
    
    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public Timestamp getEstimatedDelivery() {
        return estimatedDelivery;
    }

    public void setEstimatedDelivery(Timestamp estimatedDelivery) {
        this.estimatedDelivery = estimatedDelivery;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public DiscountType getDiscountType() {
        return discountType;
    }

    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }

    @Override
    public String toString() {
        return "OrderViewModel{" + "orderId=" + orderId + ", orderDate=" + orderDate + ", status=" + status + ", totalAmount=" + totalAmount + ", discountAmount=" + discountAmount + ", discountType=" + discountType + ", address=" + address + ", paymentMethod=" + paymentMethod + ", trackingNumber=" + trackingNumber + ", estimatedDelivery=" + estimatedDelivery + ", orderDetails=" + orderDetails + '}';
    }
}
