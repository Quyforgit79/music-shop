/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Nguyen Hoang Thai Vinh - CE190384
 */
public class DeliveryDate {

    private static final int DELIVERY_DAY = 4;

    /**
     * Get the current date (order date).
     */
    public static LocalDate getOrderDate() {
        return LocalDate.now();
    }

    /**
     * Calculate delivery date after DELIVERY_DAY (default 4 days).
     * 
     * @return LocalDate Delivery date
     */
    public static LocalDate getEstimatedDeliveryDate() {
        return getOrderDate().plusDays(DELIVERY_DAY);
    }

    public static void main(String[] args) {
        LocalDate orderDate = getOrderDate();
        LocalDate deliveryDate = getEstimatedDeliveryDate();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        System.out.println("Ngày đặt hàng       : " + orderDate.format(formatter));
        System.out.println("Ngày giao hàng (+4) : " + deliveryDate.format(formatter));
    }
}
