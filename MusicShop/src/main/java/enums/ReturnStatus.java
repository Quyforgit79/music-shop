/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package enums;

/**
 *
 * @author Nguyen Hoang Thai Vinh - CE190384
 */
public enum ReturnStatus {
    PENDING(1, "PENDING", "badge bg-warning text-dark"),
    ACCEPT(2, "ACCEPT", "badge bg-success"),
    REJECT(3, "REJECT", "badge bg-danger");

    private final int code;
    private final String description;
    private final String bootstrapClass;

    ReturnStatus(int code, String description, String bootstrapClass) {
        this.code = code;
        this.description = description;
        this.bootstrapClass = bootstrapClass;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public String getBootstrapClass() {
        return bootstrapClass;
    }

    public static ReturnStatus fromCode(int code) {
        for (ReturnStatus status : ReturnStatus.values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid ReturnStatus code: " + code);
    }
}
