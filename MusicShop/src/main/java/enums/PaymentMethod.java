/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package enums;

/**
 *
 * @author Nguyen Hoang Thai Vinh - CE190384
 */
public enum PaymentMethod {
    CARD(1, "Cash on Delivery (COD)"),
    BANK_TRANSFER(2, "Bank Transfer");

    private final int code;
    private final String label;

    PaymentMethod(int code, String label) {
        this.code = code;
        this.label = label;
    }

    public static PaymentMethod fromCode(int code) {
        for (PaymentMethod p : values()) {
            if (p.code == code) {
                return p;
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
