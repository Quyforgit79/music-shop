/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package enums;

/**
 *
 * @author Nguyen Hoang Thai Vinh - CE190384
 */
public enum ShippingStatus {
    NOT_SHIPPED(1, "Not Shipped", "secondary"),
    SHIPPING(2, "Shipping", "info"),
    DELIVERED(3, "Delivered", "success"),
    FAILED(4, "Failed", "danger");

    private final int value;
    private final String label;
    private final String badgeClass;

    ShippingStatus(int value, String label, String badgeClass) {
        this.value = value;
        this.label = label;
        this.badgeClass = badgeClass;
    }

    public int getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

    public String getBadgeClass() {
        return badgeClass;
    }

    public static ShippingStatus fromInt(int value) {
        for (ShippingStatus status : values()) {
            if (status.getValue() == value) {
                return status;
            }
        }
        return null; // hoặc NOT_SHIPPED
    }
}
