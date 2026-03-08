/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import enums.PaymentMethod;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 *
 * @author Nguyen Hoang Thai Vinh - CE190384
 */
public class Payment {

    private int payment_id;
    private Order order;
    private LocalDate payDate;
    private BigDecimal amount;
    private PaymentMethod paymentMethod;

    public Payment(int payment_id, Order order, LocalDate payDate, BigDecimal amount, PaymentMethod paymentMethod) {
        this.payment_id = payment_id;
        this.order = order;
        this.payDate = payDate;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
    }

    public Payment() {
    }

    public int getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(int payment_id) {
        this.payment_id = payment_id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public LocalDate getPayDate() {
        return payDate;
    }

    public void setPayDate(LocalDate payDate) {
        this.payDate = payDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    @Override
    public String toString() {
        return "Payment{" + "payment_id=" + payment_id + ", order=" + order + ", payDate=" + payDate + ", amount=" + amount + ", paymentMethod=" + paymentMethod + '}';
    }

}
