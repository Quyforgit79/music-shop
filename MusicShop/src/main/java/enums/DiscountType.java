/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package enums;

/**
 *
 * @author Nguyen Hoang Thai Vinh - CE190384
 */
public enum DiscountType {
    PERCENTAGE(1, "Percentage"),
    FIXED(2, "Fixed"),
    NONE(3, "None");

    private final int code;
    private final String label;

    DiscountType(int code, String label) {
        this.code = code;
        this.label = label;
    }

    public static DiscountType fromType(int code) {
        for (DiscountType t : values()) {
            if (t.code == code) {
                return t;
            }
        }
        return null;
    }

    public String getLabel() {
        return label;
    }

    public int getCode() {
        return code;
    }
}
