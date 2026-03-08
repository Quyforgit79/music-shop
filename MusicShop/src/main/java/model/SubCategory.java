/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Nguyen Hoang Thai Vinh - CE190384
 */
public class SubCategory {

    private int subCategoryId;
    private String name;
    private Category category;

    public SubCategory() {
    }

    public SubCategory(int subCategoryId, String name, Category category) {
        this.subCategoryId = subCategoryId;
        this.name = name;
        this.category = category;
    }

    public int getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(int subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "SubCategory{" + "subcategoryId=" + subCategoryId + ", name=" + name + ", category=" + category + '}';
    }

}
