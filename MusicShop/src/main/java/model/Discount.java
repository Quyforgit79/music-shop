/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import enums.DiscountType;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 *
 * @author Nguyen Hoang Thai Vinh - CE190384
 */
public class Discount {

    private int discountId;
    private String code;
    private String description;
    private DiscountType discountType; // "percentage" hoặc "fixed"
    private BigDecimal discountValue;
    private LocalDate startDate;
    private LocalDate endDate;
    private int usageLimit;
    private int usedCount;
    private boolean isActive;

    // Constructors
    public Discount() {
    }

    public Discount(int discountId, String code, String description, DiscountType discountType, BigDecimal discountValue,
            LocalDate startDate, LocalDate endDate, int usageLimit,
            int usedCount, boolean isActive) {
        this.discountId = discountId;
        this.code = code;
        this.description = description;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.startDate = startDate;
        this.endDate = endDate;
        this.usageLimit = usageLimit;
        this.usedCount = usedCount;
        this.isActive = isActive;
    }

    // Getters and Setters
    public int getDiscountId() {
        return discountId;
    }

    public void setDiscountId(int discountId) {
        this.discountId = discountId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DiscountType getDiscountType() {
        return discountType;
    }

    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }

    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public int getUsageLimit() {
        return usageLimit;
    }

    public void setUsageLimit(int usageLimit) {
        this.usageLimit = usageLimit;
    }

    public int getUsedCount() {
        return usedCount;
    }

    public void setUsedCount(int usedCount) {
        this.usedCount = usedCount;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public String toString() {
        return "Discount{" + "discountId=" + discountId + ", code=" + code + ", description=" + description + ", discountType=" + discountType + ", discountValue=" + discountValue + ", startDate=" + startDate + ", endDate=" + endDate + ", usageLimit=" + usageLimit + ", usedCount=" + usedCount + ", isActive=" + isActive + '}';
    }

}
