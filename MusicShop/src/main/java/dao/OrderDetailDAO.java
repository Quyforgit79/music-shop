/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import db.JDBCUtil;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Order;
import model.OrderDetail;
import model.Product;

/**
 *
 * @author Nguyen Hoang Thai Vinh - CE190384
 */
public class OrderDetailDAO extends JDBCUtil {

    // Create new OrderDetail and return generated ID
    public boolean createOrderDetail(OrderDetail orderDetail) {
        String sql = "INSERT INTO order_details (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
        Object[] params = {
            orderDetail.getOrderDetailId(), orderDetail.getOrder().getOrderId(), orderDetail.getProduct().getProductId(),
            orderDetail.getQuantity(), orderDetail.getPrice()
        };

        try {
            return execQuery(sql, params) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get OrderDetail by ID
    public OrderDetail getOrderDetailById(int orderDetailId) {
        String sql = "SELECT \n"
                + "	od.order_detail_id,\n"
                + "	od.order_id,\n"
                + "	od.product_id,\n"
                + "	od.quantity,\n"
                + "	od.price,\n"
                + "	p.product_id,\n"
                + "	p.name,\n"
                + "	p.discount_end,\n"
                + "	p.image_url,\n"
                + "	p.description,\n"
                + "	p.price,\n"
                + "	p.sold_quantity\n"
                + "FROM OrderDetails od\n"
                + "JOIN Orders o ON o.order_id = od.order_id\n"
                + "JOIN Products p ON p.product_id = od.product_id\n"
                + "WHERE od.order_detail_id = ?;";

        try {
            Object[] params = {orderDetailId};

            try ( ResultSet rs = execSelectQuery(sql, params)) {
                if (rs.next()) {
                    Product product = new Product();
                    product.setProductId(rs.getInt("order_id"));

                    Order order = new Order();
                    order.setOrderId(rs.getInt("order_id"));

                    int orderDetailID = rs.getInt("order_detail_id");
                    int quantity = rs.getInt("quantity");
                    BigDecimal pice = rs.getBigDecimal("price");

                    return new OrderDetail(orderDetailID, order, product, quantity, pice);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Get all OrderDetails for an order
    public List<OrderDetail> getOrderDetailsByOrderId(int orderId) {
        List<OrderDetail> list = new ArrayList<>();
        String sql = "SELECT \n"
                + "	od.order_detail_id,\n"
                + "	od.order_id,\n"
                + "	od.product_id,\n"
                + "	od.quantity,\n"
                + "	od.price,\n"
                + "	p.* \n"
                + "FROM OrderDetails od\n"
                + "JOIN Orders o ON o.order_id = od.order_id\n"
                + "JOIN Products p ON p.product_id = od.product_id\n"
                + "WHERE o.order_id = ?;";

        try {
            Object[] params = {orderId};

            try ( ResultSet rs = execSelectQuery(sql, params)) {
                if (rs.next()) {
                    Product product = new Product();
                    product.setProductId(rs.getInt("order_id"));

                    Order order = new Order();
                    order.setOrderId(rs.getInt("order_id"));

                    int orderDetailID = rs.getInt("order_detail_id");
                    int quantity = rs.getInt("quantity");
                    BigDecimal pice = rs.getBigDecimal("price");

                    list.add(new OrderDetail(orderDetailID, order, product, quantity, pice));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Update existing OrderDetail
    public boolean updateOrderDetail(OrderDetail orderDetail) {
        String sql = "UPDATE order_details SET order_id = ?, product_id = ?, quantity = ?, price = ? WHERE order_detail_id = ?";

        try {
            Object[] params = {
                orderDetail.getProduct().getProductId(), orderDetail.getQuantity(), orderDetail.getPrice(), orderDetail.getOrderDetailId()
            };
            return execQuery(sql, params) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete OrderDetail by ID
    public boolean deleteOrderDetail(int orderDetailId) {
        String sql = "DELETE FROM order_details WHERE order_detail_id = ?";

        try {
            return execQuery(sql, null) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Count all OrderDetails
    public int countAllOrderDetails() {
        String sql = "SELECT COUNT(*) FROM order_details";

        try {
            ResultSet rs = execSelectQuery(sql);

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Get all OrderDetails
    public List<OrderDetail> getAllOrderDetails() {
        String sql = "SELECT * FROM order_details";
        List<OrderDetail> list = new ArrayList<>();

        try {
            ResultSet rs = execSelectQuery(sql);
            Product product = new Product();
            product.setProductId(rs.getInt("order_id"));

            Order order = new Order();
            order.setOrderId(rs.getInt("order_id"));

            while (rs.next()) {
                list.add(new OrderDetail(rs.getInt("order_detail_id"), order, product, rs.getInt("quantity"), rs.getBigDecimal("price")));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void main(String[] args) {
        List<OrderDetail> ol = new OrderDetailDAO().getOrderDetailsByOrderId(9);

        for (OrderDetail od : ol) {
            System.out.println(od.toString());
        }
    }
}
