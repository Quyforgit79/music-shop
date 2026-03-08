/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import db.JDBCUtil;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.SubCategory;

/**
 *
 * @author Nguyen Hoang Thai Vinh - CE190384
 */
public class SubcategoryDAO extends JDBCUtil{
    public List<SubCategory> getAllSubcategoryByCategorys(int categoryId) {
        String sql = "SELECT * FROM Subcategories WHERE category_id = ?";
        List<SubCategory> list = new ArrayList<>();
        
        Object[] params = {
            categoryId
        };
        
        try (ResultSet rs = execSelectQuery(sql, params)) {
            while (rs.next()) {
                SubCategory subCategory = new SubCategory();
                subCategory.setName(rs.getString("name"));
                subCategory.setSubCategoryId(rs.getInt("subcategory_id"));
                
                list.add(subCategory);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return list;
    }
    
    public static void main(String[] args) {
        List<SubCategory> sub = new SubcategoryDAO().getAllSubcategoryByCategorys(1);
        
        for(SubCategory s : sub) {
            System.out.println(sub.toString());
        }
    }
}
