/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package enums;

/**
 *
 * @author Nguyen Hoang Thai Vinh - CE190384
 */
public enum Gender {
    MALE(1, "Male"),
    FEMALE(2, "Female"),
    OTHER(3, "Other");

    private final int gender;
    private final String label;

    Gender(int gender, String label) {
        this.gender = gender;
        this.label = label;
    }

    public static Gender fromGender(int gender) {
        for (Gender g : values()) {
            if (g.gender == gender) {
                return g;
            }
        }
        return null;
    }

    public int getGender() {
        return gender;
    }

    public String getLabel() {
        return label;
    }
}
