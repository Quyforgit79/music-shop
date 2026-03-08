/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import db.JDBCUtil;
import enums.DiscountType;
import java.sql.ResultSet;
import model.Discount;

/**
 *
 * @author Nguyen Hoang Thai Vinh - CE190384
 */
public class DiscountDAO extends JDBCUtil {

    public Discount getDiscountByProductId(int productId) {
        String sql = "SELECT d.* "
                + "FROM Discounts d "
                + "JOIN Products p ON p.discount_id = d.discount_id "
                + "WHERE p.product_id = ?";

        Object[] params = {productId};

        try ( ResultSet rs = execSelectQuery(sql, params)) {
            if (rs.next()) {
                Discount discount = new Discount();
                discount.setDiscountId(rs.getInt("discount_id"));
                discount.setCode(rs.getString("code"));
                discount.setDescription(rs.getString("description"));
                discount.setDiscountType(DiscountType.fromType(rs.getInt("discount_type")));
                discount.setDiscountValue(rs.getBigDecimal("discount_value"));
                discount.setStartDate(rs.getDate("start_date").toLocalDate());
                discount.setEndDate(rs.getDate("end_date").toLocalDate());
                discount.setUsageLimit(rs.getInt("usage_limit"));
                discount.setUsedCount(rs.getInt("used_count"));
                discount.setIsActive(rs.getBoolean("is_active"));
                return discount;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public Discount getCasualDiscount() {
        String sql = "SELECT d.* "
                + "FROM Discounts d "
                + "WHERE d.code = 'CSAL'";

        try ( ResultSet rs = execSelectQuery(sql)) {
            if (rs.next()) {
                Discount discount = new Discount();
                discount.setDiscountId(rs.getInt("discount_id"));
                discount.setCode(rs.getString("code"));
                discount.setDescription(rs.getString("description"));
                discount.setDiscountType(DiscountType.fromType(rs.getInt("discount_type")));
                discount.setDiscountValue(rs.getBigDecimal("discount_value"));
                discount.setStartDate(rs.getDate("start_date").toLocalDate());
                discount.setEndDate(rs.getDate("end_date").toLocalDate());
                discount.setUsageLimit(rs.getInt("usage_limit"));
                discount.setUsedCount(rs.getInt("used_count"));
                discount.setIsActive(rs.getBoolean("is_active"));
                return discount;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public Discount getDiscountById(int discountId) {
        String sql = "SELECT * \n"
                + "FROM Discounts d\n"
                + "WHERE d.discount_id = ?";

        Object[] params = {discountId};

        try ( ResultSet rs = execSelectQuery(sql, params)) {
            if (rs.next()) {
                Discount discount = new Discount();
                discount.setDiscountId(rs.getInt("discount_id"));
                discount.setCode(rs.getString("code"));
                discount.setDescription(rs.getString("description"));
                discount.setDiscountType(DiscountType.fromType(rs.getInt("discount_type")));
                discount.setDiscountValue(rs.getBigDecimal("discount_value"));
                discount.setStartDate(rs.getDate("start_date").toLocalDate());
                discount.setEndDate(rs.getDate("end_date").toLocalDate());
                discount.setUsageLimit(rs.getInt("usage_limit"));
                discount.setUsedCount(rs.getInt("used_count"));
                discount.setIsActive(rs.getBoolean("is_active"));
                return discount;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void main(String[] args) {
        Discount d = new DiscountDAO().getCasualDiscount();

        System.out.println(d.toString());
    }
}
