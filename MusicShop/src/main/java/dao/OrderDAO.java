/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import db.JDBCUtil;
import enums.AddressStyle;
import enums.DiscountType;
import enums.OrderStatus;
import enums.PaymentMethod;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import model.Address;
import model.Discount;
import model.Order;
import model.User;
import model.Product;
import model.Brand;
import model.Category;
import model.OrderDetail;
import model.OrderViewModel;
import model.Payment;

public class OrderDAO extends JDBCUtil {

    public boolean addOrder(Order order) {
        String sql = "INSERT INTO Orders (user_id, status, total_amount, discount_id, address_id) "
                + "VALUES (?, ?, ?, ?, ?)";

        Object[] params = {
            order.getUser().getUserId(),
            order.getStatus() != null ? order.getStatus().getValue() : null, // enum -> int
            order.getTotalAmount(),
            order.getDiscount() != null ? order.getDiscount().getDiscountId() : null,
            order.getAddress() != null ? order.getAddress().getAddressId() : null
        };

        try {
            return execQuery(sql, params) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean setOrderReviewed(int orderId, int userId) {
        String sql = "UPDATE Orders set is_review = 1 WHERE order_id = ? AND user_id = ?";
        Object[] params = {orderId, userId};

        try {
            return execQuery(sql, params) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteOrderById(int orderId, int userId) {
        String sql = "DELETE Orders WHERE order_id = ? AND user_id = ?;";
        Object[] params = {orderId, userId};

        try {
            return execQuery(sql, params) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public int createOrder(int userId, int addressId, int productId, int quantity,
            Integer discountId, int paymentId) throws SQLException {
        String procedureCall = "{CALL sp_CreateOrder(?, ?, ?, ?, ?, ?, ?)}";
        Object[] params = {
            userId, // 1 - @user_id
            addressId, // 2 - @address_id
            productId, // 3 - @product_id
            quantity, // 4 - @quantity
            discountId, // 5 - @discount_id
            paymentId, // 6 - @payment_id
            null // 7 - @order_id OUTPUT
        };
        return execStoredProcedure(procedureCall, params, 7);
    }

    public Order getOrderById(int orderId) {
        String sql = "SELECT o.*, p.*, s.*, a.street, a.city, a.district,"
                + "     a.ward, a.type, a.is_default  \n"
                + "     FROM Orders o  \n"
                + "     LEFT JOIN Address a ON o.address_id = a.address_id  \n"
                + "	LEFT JOIN Payments p ON p.payment_id = o.payment_id\n"
                + "     WHERE o.order_id = ?";

        Object[] params = {orderId};

        try ( ResultSet rs = execSelectQuery(sql, params)) {
            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setFullName(rs.getString("name"));

                Discount discount = null;
                int discountId = rs.getInt("discount_id");
                if (!rs.wasNull() && discountId != 0) {
                    discount = new Discount();
                    discount.setDiscountId(discountId);
                    discount.setDiscountValue(rs.getBigDecimal("discount_amount"));
                }

                Address address = new Address();
                address.setAddressId(rs.getInt("address_id"));
                address.setStreet(rs.getString("street"));
                address.setCity(rs.getString("city"));
                address.setWard(rs.getString("ward"));
                address.setDistrict(rs.getString("district"));

                // Chuyển đổi int status sang enum
                int statusInt = rs.getInt("status");
                OrderStatus status = OrderStatus.fromInt(statusInt);

                // Payment
                Payment payment = new Payment();
                if (rs.getTimestamp("payment_date") != null) {
                    payment.setPayDate(rs.getDate("payment_date").toLocalDate());
                }
                payment.setAmount(rs.getBigDecimal("amount"));
                payment.setPaymentMethod(PaymentMethod.fromCode(rs.getInt("payment_method")));

                // Xử lý ngày giao hàng thực tế
                LocalDate shippedDate = null;
                if (rs.getDate("shipped_date") != null) {
                    shippedDate = rs.getDate("shipped_date").toLocalDate();
                }

                // Xử lý ngày giao dự kiến
                LocalDate estimatedDelivery = null;
                if (rs.getTimestamp("estimated_delivery") != null) {
                    estimatedDelivery = rs.getTimestamp("estimated_delivery").toLocalDateTime().toLocalDate();
                }

                Order order = new Order(
                        rs.getInt("order_id"),
                        user,
                        rs.getTimestamp("order_date").toLocalDateTime(),
                        status,
                        rs.getBigDecimal("total_amount"),
                        discount,
                        address,
                        payment,
                        shippedDate,
                        estimatedDelivery,
                        rs.getInt("is_review")
                );

                return order;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Order> getOrdersByUserId(int userId) {
        List<Order> orders = new ArrayList<>();
        Map<Integer, Order> orderMap = new LinkedHashMap<>(); // Đảm bảo không trùng order_id

        String sql = "SELECT \n"
                + "    o.*, \n"
                + "    a.city, a.district, a.ward, a.street, \n"
                + "    a.receiver_name, a.receiver_phone, \n"
                + "    od.order_detail_id, od.quantity, od.price as [order_detail_price],\n"
                + "    p.product_id, p.name AS product_name, p.image_url \n"
                + "FROM Orders o \n"
                + "JOIN OrderDetails od ON o.order_id = od.order_id \n"
                + "LEFT JOIN Address a ON o.address_id = a.address_id \n"
                + "LEFT JOIN Products p ON od.product_id = p.product_id \n"
                + "WHERE o.user_id = ? AND o.is_return = 0 AND o.is_review = 0 \n"
                + "ORDER BY o.order_id DESC;";

        try ( ResultSet rs = execSelectQuery(sql, new Object[]{userId})) {
            while (rs.next()) {
                int orderId = rs.getInt("order_id");

                // Nếu order chưa được tạo, thì tạo mới
                Order order = orderMap.get(orderId);
                if (order == null) {
                    // Tạo User
                    User user = new User();
                    user.setUserId(rs.getInt("user_id"));

                    // Tạo Address
                    Address address = new Address();
                    address.setAddressId(rs.getInt("address_id"));
                    address.setStreet(rs.getString("street"));
                    address.setCity(rs.getString("city"));
                    address.setWard(rs.getString("ward"));
                    address.setDistrict(rs.getString("district"));
                    address.setReceiverName(rs.getString("receiver_name"));
                    address.setReceiverPhone(rs.getString("receiver_phone"));

                    // Trạng thái đơn hàng
                    OrderStatus status = OrderStatus.fromInt(rs.getInt("status"));

                    // Ngày đặt hàng
                    LocalDateTime orderDate = null;
                    Timestamp orderTimestamp = rs.getTimestamp("order_date");
                    if (orderTimestamp != null) {
                        orderDate = orderTimestamp.toLocalDateTime();
                    }

                    // Ngày giao hàng thực tế
                    LocalDate shippedDate = null;
                    Date shippedSqlDate = rs.getDate("shipped_date");
                    if (shippedSqlDate != null) {
                        shippedDate = shippedSqlDate.toLocalDate();
                    }

                    // Ngày giao dự kiến
                    LocalDate estimatedDelivery = null;
                    Timestamp estimatedTimestamp = rs.getTimestamp("estimated_delivery");
                    if (estimatedTimestamp != null) {
                        estimatedDelivery = estimatedTimestamp.toLocalDateTime().toLocalDate();
                    }

                    // Tạo đối tượng Order
                    order = new Order();
                    order.setOrderId(orderId);
                    order.setUser(user);
                    order.setAddress(address);
                    order.setStatus(status);
                    order.setOrderDate(orderDate);
                    order.setTotalAmount(rs.getBigDecimal("total_amount"));
                    order.setShippedDate(shippedDate);
                    order.setEstimatedDelivery(estimatedDelivery);
                    order.setIsReview(rs.getInt("is_review"));

                    orderMap.put(orderId, order);
                }

                // Tạo Product
                Product product = new Product();
                product.setProductId(rs.getInt("product_id"));
                product.setName(rs.getString("product_name"));
                product.setImageUrl(rs.getString("image_url"));

                // Tạo OrderDetail
                OrderDetail detail = new OrderDetail();
                detail.setOrderDetailId(rs.getInt("order_detail_id"));
                detail.setQuantity(rs.getInt("quantity"));
                detail.setPrice(rs.getBigDecimal("order_detail_price"));
                detail.setProduct(product);

                // Gán sản phẩm đại diện (nếu bạn muốn hiển thị 1 sp trong đơn)
                order.setOrderDetail(detail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<>(orderMap.values());
    }

    public Product getProductByOrderId(int orderId) {
        String sql = "SELECT  \n"
                + "    p.name AS product_name, \n"
                + "    p.description AS product_description, \n"
                + "    p.image_url, \n"
                + "    p.category_id, \n"
                + "    p.brand_id, \n"
                + "    p.price,\n"
                + "    p.stock_quantity,\n"
                + "    p.discount_id, \n"
                + "    p.product_id, \n"
                + "    b.brand_id AS brand_id, \n"
                + "    b.brand_name AS brand_name,\n"
                + "    c.category_id AS category_id,\n"
                + "    c.name AS category_name,\n"
                + "    c.description AS category_description\n"
                + "FROM OrderDetails od \n"
                + "LEFT JOIN Products p ON p.product_id = od.product_id \n"
                + "LEFT JOIN Orders o ON o.order_id = od.order_id \n"
                + "LEFT JOIN Brands b ON b.brand_id = p.brand_id \n"
                + "LEFT JOIN Categories c ON c.category_id = p.category_id\n"
                + "WHERE o.order_id = ?";

        Object[] params = {orderId};

        try ( ResultSet rs = execSelectQuery(sql, params)) {
            if (rs.next()) {
                Product product = new Product();

                Brand brand = new Brand(
                        rs.getInt("brand_id"),
                        rs.getString("brand_name")
                );

                Category category = new Category(
                        rs.getInt("category_id"),
                        rs.getString("category_name"),
                        rs.getString("category_description")
                );

                product.setProductId(rs.getInt("product_id"));
                product.setName(rs.getString("product_name"));
                product.setDescription(rs.getString("product_description"));
                product.setImageUrl(rs.getString("image_url"));
                product.setStockQuantity(rs.getInt("stock_quantity"));
                product.setPrice(rs.getBigDecimal("price"));
                product.setBrand(brand);
                product.setCategory(category);

                return product;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public OrderViewModel getOrderView(int orderId) {
        String sql = "SELECT \n"
                + "    o.order_id, \n"
                + "    o.order_date, \n"
                + "    o.status, \n"
                + "    o.total_amount, \n"
                + "    o.shipped_date, \n"
                + "    ISNULL(d.discount_value, 0) AS discount_amount, d.discount_type,\n"
                + "    a.*, \n"
                + "    p.payment_method, \n"
                + "    o.estimated_delivery, \n"
                + "    pr.product_id, \n"
                + "    pr.name AS product_name, \n"
                + "    pr.image_url, \n"
                + "    od.quantity, \n"
                + "    od.price \n"
                + "FROM Orders o \n"
                + "JOIN OrderDetails od ON o.order_id = od.order_id \n"
                + "JOIN Products pr ON od.product_id = pr.product_id \n"
                + "LEFT JOIN Address a ON o.address_id = a.address_id \n"
                + "LEFT JOIN Payments p ON o.payment_id = p.payment_id \n"
                + "LEFT JOIN Discounts d ON o.discount_id = d.discount_id \n"
                + "WHERE o.order_id = ?;";

        OrderViewModel orderView = null;
        List<OrderDetail> productList = new ArrayList<>();
        Object[] params = {orderId};

        try ( ResultSet rs = execSelectQuery(sql, params)) {
            while (rs.next()) {
                if (orderView == null) {
                    orderView = new OrderViewModel();
                    orderView.setOrderId(rs.getInt("order_id"));
                    orderView.setOrderDate(rs.getTimestamp("order_date"));
                    int discountTypeCode = rs.getInt("discount_type");
                    orderView.setDiscountType(!rs.wasNull() ? DiscountType.fromType(discountTypeCode) : DiscountType.NONE);
                    try {
                        orderView.setStatus(OrderStatus.fromInt(rs.getInt("status")));
                    } catch (IllegalArgumentException e) {
                        System.err.println("Invalid status value: " + rs.getInt("status") + " for orderId: " + orderId);
                        orderView.setStatus(OrderStatus.PENDING); // Fallback
                    }
                    orderView.setTotalAmount(rs.getBigDecimal("total_amount"));
                    orderView.setDiscountAmount(rs.getBigDecimal("discount_amount"));
                    
                    try {
                        int paymentMethodCode = rs.getInt("payment_method");
                        if (!rs.wasNull()) {
                            orderView.setPaymentMethod(PaymentMethod.fromCode(paymentMethodCode));
                        } else {
                            orderView.setPaymentMethod(null);
                            System.out.println("Payment method is NULL for orderId: " + orderId);
                        }
                    } catch (IllegalArgumentException e) {
                        System.err.println("Invalid payment_method value: " + rs.getInt("payment_method") + " for orderId: " + orderId);
                        orderView.setPaymentMethod(PaymentMethod.CARD); // Fallback
                    }

                    orderView.setEstimatedDelivery(rs.getTimestamp("estimated_delivery"));
                }
                
                Address address = new Address(
                        rs.getInt("address_id"),
                        null,
                        rs.getString("street"),
                        rs.getString("ward"),
                        rs.getString("district"),
                        rs.getString("city"),
                        AddressStyle.fromStyle(rs.getInt("type")),
                        rs.getBoolean("is_default"),
                        rs.getString("receiver_phone"),
                        rs.getString("receiver_name"),
                        rs.getBoolean("is_deleted")
                );
                orderView.setAddress(address);

                Product product = new Product();
                product.setProductId(rs.getInt("product_id"));
                product.setName(rs.getString("product_name") != null ? rs.getString("product_name") : "N/A");
                product.setImageUrl(rs.getString("image_url") != null ? rs.getString("image_url") : "default-image.jpg");

                OrderDetail detail = new OrderDetail();
                detail.setProduct(product);
                detail.setQuantity(rs.getInt("quantity"));
                detail.setPrice(rs.getBigDecimal("price"));

                productList.add(detail);
            }
            if (orderView != null) {
                orderView.setOrderDetails(productList);
                System.out.println("Order loaded with " + productList.size() + " products for orderId: " + orderId);
            }
        } catch (SQLException e) {
            System.err.println("SQL Error for orderId: " + orderId + " - " + e.getMessage());
            e.printStackTrace();
        }
        return orderView;
    }

    public List<Order> getOrdersHistoryByUserId(int userId) {
        List<Order> orders = new ArrayList<>();
        Map<Integer, Order> orderMap = new LinkedHashMap<>();

        String sql = "SELECT \n"
                + "    o.*, \n"
                + "    a.city, a.district, a.ward, a.street, \n"
                + "    a.receiver_name, a.receiver_phone, \n"
                + "    od.order_detail_id, od.quantity, od.price as [order_detail_price],\n"
                + "    p.product_id, p.name AS product_name, p.image_url \n"
                + "FROM Orders o \n"
                + "JOIN OrderDetails od ON o.order_id = od.order_id \n"
                + "LEFT JOIN Address a ON o.address_id = a.address_id \n"
                + "LEFT JOIN Products p ON od.product_id = p.product_id \n"
                + "WHERE o.user_id = ? AND o.is_return = 0 AND o.status = 3 \n"
                + "ORDER BY o.order_id DESC;";

        try ( ResultSet rs = execSelectQuery(sql, new Object[]{userId})) {
            while (rs.next()) {
                int orderId = rs.getInt("order_id");

                // Nếu order chưa được tạo, thì tạo mới
                Order order = orderMap.get(orderId);
                if (order == null) {
                    // Tạo User
                    User user = new User();
                    user.setUserId(rs.getInt("user_id"));

                    // Tạo Address
                    Address address = new Address();
                    address.setAddressId(rs.getInt("address_id"));
                    address.setStreet(rs.getString("street"));
                    address.setCity(rs.getString("city"));
                    address.setWard(rs.getString("ward"));
                    address.setDistrict(rs.getString("district"));
                    address.setReceiverName(rs.getString("receiver_name"));
                    address.setReceiverPhone(rs.getString("receiver_phone"));

                    // Trạng thái đơn hàng
                    OrderStatus status = OrderStatus.fromInt(rs.getInt("status"));

                    // Ngày đặt hàng
                    LocalDateTime orderDate = null;
                    Timestamp orderTimestamp = rs.getTimestamp("order_date");
                    if (orderTimestamp != null) {
                        orderDate = orderTimestamp.toLocalDateTime();
                    }

                    // Ngày giao hàng thực tế
                    LocalDate shippedDate = null;
                    Date shippedSqlDate = rs.getDate("shipped_date");
                    if (shippedSqlDate != null) {
                        shippedDate = shippedSqlDate.toLocalDate();
                    }

                    // Ngày giao dự kiến
                    LocalDate estimatedDelivery = null;
                    Timestamp estimatedTimestamp = rs.getTimestamp("estimated_delivery");
                    if (estimatedTimestamp != null) {
                        estimatedDelivery = estimatedTimestamp.toLocalDateTime().toLocalDate();
                    }

                    // Tạo đối tượng Order
                    order = new Order();
                    order.setOrderId(orderId);
                    order.setUser(user);
                    order.setAddress(address);
                    order.setStatus(status);
                    order.setOrderDate(orderDate);
                    order.setTotalAmount(rs.getBigDecimal("total_amount"));
                    order.setShippedDate(shippedDate);
                    order.setEstimatedDelivery(estimatedDelivery);

                    orderMap.put(orderId, order);
                }

                // Tạo Product
                Product product = new Product();
                product.setProductId(rs.getInt("product_id"));
                product.setName(rs.getString("product_name"));
                product.setImageUrl(rs.getString("image_url"));

                // Tạo OrderDetail
                OrderDetail detail = new OrderDetail();
                detail.setOrderDetailId(rs.getInt("order_detail_id"));
                detail.setQuantity(rs.getInt("quantity"));
                detail.setPrice(rs.getBigDecimal("order_detail_price"));
                detail.setProduct(product);

                // Gán sản phẩm đại diện (nếu bạn muốn hiển thị 1 sp trong đơn)
                order.setOrderDetail(detail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<>(orderMap.values());
    }

    public boolean deleteAllOrder(int orderId) {
        String sql = "DELETE FROM Orders WHERE user_id = ? AND status = 3";
        Object[] params = {orderId};

        try {
            return execQuery(sql, params) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean hasPurchased(int userId, int productId) {
        String sql = "SELECT COUNT(*) FROM Orders o "
                + "JOIN OrderDetails od ON od.order_id = o.order_id "
                + "WHERE o.user_id = ? AND od.product_id = ? AND o.status = 3";
        Object[] params = {userId, productId};

        try ( ResultSet rs = execSelectQuery(sql, params)) {
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        Product p = new OrderDAO().getProductByOrderId(9);
        System.out.println(p.toString());
    }
}
