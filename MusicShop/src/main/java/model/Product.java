/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 *
 * @author Nguyen Hoang Thai Vinh - CE190384
 */
public class Product {

    private int productId;
    private String name;
    private String description;
    private BigDecimal price;
    private int stockQuantity;
    private Category category;
    private String imageUrl;
    private SubCategory subCategory;
    private LocalDateTime createDateTime;
    private Brand brand;
    private int soldQuantity;
    private Discount discount;
    private String madeIn;
    private int yearOfManufacture;
    private String material;

    public Product() {
    }

    public Product(int productId, String name, String description, BigDecimal price,
            int stockQuantity, Category category, String imageUrl, LocalDateTime createDateTime,
            Brand brand, Discount discount, int soldQuantity, String material, int yearOfManufacture,
            String madeIn, SubCategory subCategory) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.category = category;
        this.imageUrl = imageUrl;
        this.createDateTime = createDateTime;
        this.brand = brand;
        this.discount = discount;
        this.soldQuantity = soldQuantity;
        this.madeIn = madeIn;
        this.material = material;
        this.yearOfManufacture = yearOfManufacture;
        this.subCategory = subCategory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public LocalDateTime getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(LocalDateTime createDateTime) {
        this.createDateTime = createDateTime;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

    public int getSoldQuantity() {
        return soldQuantity;
    }

    public void setSoldQuantity(int soldQuantity) {
        this.soldQuantity = soldQuantity;
    }

    public String getMadeIn() {
        return madeIn;
    }

    public void setMadeIn(String madeIn) {
        this.madeIn = madeIn;
    }

    public int getYearOfManufacture() {
        return yearOfManufacture;
    }

    public void setYearOfManufacture(int yearOfManufacture) {
        this.yearOfManufacture = yearOfManufacture;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public SubCategory getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(SubCategory subCategory) {
        this.subCategory = subCategory;
    }

    @Override
    public String toString() {
        return "Product{" + "productId=" + productId + ", name=" + name + ", description=" + description + ", price=" + price + ", stockQuantity=" + stockQuantity + ", category=" + category + ", imageUrl=" + imageUrl + ", subCategory=" + subCategory + ", createDateTime=" + createDateTime + ", brand=" + brand + ", soldQuantity=" + soldQuantity + ", discount=" + discount + ", madeIn=" + madeIn + ", yearOfManufacture=" + yearOfManufacture + ", material=" + material + '}';
    }
}
