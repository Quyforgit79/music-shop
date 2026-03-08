/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import db.JDBCUtil;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.Product;
import model.ProductImage;

/**
 *
 * @author Nguyen Hoang Thai Vinh - CE190384
 */
public class ProductImageDAO extends JDBCUtil{
    public List<ProductImage> getAllProductImages(int productId) {
        List<ProductImage> list = new ArrayList<>();
        String sql = "SELECT * FROM ProductImages WHERE product_id = ?";
        Object[] params = {productId};
        
        try (ResultSet rs = execSelectQuery(sql, params)) {
            while (rs.next()) {
                Product p = new Product();
                p.setProductId(rs.getInt("product_id"));
                
                ProductImage pi = new ProductImage();
                pi.setImageUrl(rs.getString("image_url"));
                pi.setImageId(rs.getInt("image_id"));
                pi.setIsPrimary(rs.getBoolean("is_primary"));
                
                list.add(pi);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return list;
    }
    
    public static void main(String[] args) {
        List<ProductImage> list = new ProductImageDAO().getAllProductImages(31);
        
        for(ProductImage p : list) {
            System.out.println(p.toString());
        }
    }
}
