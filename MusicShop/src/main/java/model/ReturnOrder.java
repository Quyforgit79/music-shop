/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import enums.ReturnStatus;
import java.time.LocalDateTime;

/**
 *
 * @author Nguyen Hoang Thai Vinh - CE190384
 */
public class ReturnOrder {

    private User user;
    private Order order;
    private String reason;
    private int returnId;
    private LocalDateTime returnDate;
    private ReturnStatus status;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getReturnId() {
        return returnId;
    }

    public void setReturnId(int returnId) {
        this.returnId = returnId;
    }

    public LocalDateTime getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDateTime returnDate) {
        this.returnDate = returnDate;
    }

    public ReturnStatus getStatus() {
        return status;
    }

    public void setStatus(ReturnStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ReturnOrder{" + "user=" + user + ", order=" + order + ", reason=" + reason + ", returnId=" + returnId + ", returnDate=" + returnDate + ", status=" + status + '}';
    }

}
