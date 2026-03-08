/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package enums;

/**
 *
 * @author Nguyen Hoang Thai Vinh - CE190384
 */
public enum OrderStatus {
    PENDING(1, "Pending", "bg-info"), // Màu xanh dương nhạt, thường dùng cho thông tin
    SHIPPED(2, "Shipped", "bg-success"), // Màu xanh lá cây, thường dùng cho thành công
    DELIVERED(3, "Delivered", "bg-primary"), // Màu xanh dương đậm, có thể dùng cho trạng thái chính
    CANCELLED(4, "Cancelled", "bg-danger"); // Màu đỏ, thường dùng cho lỗi hoặc hủy

    private final int value;
    private final String label;
    private final String bootstrapClass;

    OrderStatus(int value, String label, String bootstrapClass) {
        this.value = value;
        this.label = label;
        this.bootstrapClass = bootstrapClass;
    }

    public int getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

    public String getBootstrapClass() {
        return bootstrapClass;
    }

    public static OrderStatus fromInt(int value) {
        for (OrderStatus s : values()) {
            if (s.value == value) {
                return s;
            }
        }
        return null;
    }
}
