/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import db.JDBCUtil;
import java.sql.SQLException;

/**
 *
 * @author Nguyen Hoang Thai Vinh - CE190384
 */
public class ReturnOrderDAO extends JDBCUtil {

    public boolean createReturnOrder(int userId, int orderId, String reason) {
        String sql = "INSERT INTO ReturnOrder (user_id, order_id, reason) VALUES (?, ?, ?)";
        Object[] params = {
            userId, orderId, reason
        };

        try {
            return execQuery(sql, params) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
