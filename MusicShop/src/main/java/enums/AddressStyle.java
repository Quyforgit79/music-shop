/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package enums;

/**
 *
 * @author Nguyen Hoang Thai Vinh - CE190384
 */
public enum AddressStyle {
    HOME(1, "Home"),
    OFFICE(2, "Office"),
    OTHER(3, "Other");

    private int style;
    private String label;

    private AddressStyle(int style, String label) {
        this.style = style;
        this.label = label;
    }

    public static AddressStyle fromStyle(int style) {
        for (AddressStyle s : values()) {
            if (s.style == style) {
                return s;
            }
        }
        return null;
    }

    public int getStyle() {
        return style;
    }

    public void setStyle(int style) {
        this.style = style;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
