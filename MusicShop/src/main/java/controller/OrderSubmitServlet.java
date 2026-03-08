/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.AddressDAO;
import dao.DiscountDAO;
import dao.OrderDAO;
import dao.ProductDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import java.time.LocalDate;
import model.Address;
import model.Discount;
import model.User;

/**
 *
 * @author Nguyen Hoang Thai Vinh - CE190384
 */
@WebServlet(name = "OrderSubmitServlet", urlPatterns = {"/order-submit"})
public class OrderSubmitServlet extends HttpServlet {

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login");
            return;
        }

        // Lấy tham số từ form
        String productIdStr = request.getParameter("productId");
        String quantityStr = request.getParameter("quantity");
        String addressIdStr = request.getParameter("addressId");
        String voucherIdStr = request.getParameter("voucherId");
        String paymentMethodStr = request.getParameter("paymentMethod");

        // Log để debug
        System.out.println("productIdStr = " + productIdStr);
        System.out.println("quantityStr = " + quantityStr);
        System.out.println("addressIdStr = " + addressIdStr);
        System.out.println("voucherIdStr = " + voucherIdStr);
        System.out.println("paymentMethodStr = " + paymentMethodStr);

        // Thiết lập các thuộc tính request
        setRequestAttributes(request, productIdStr, quantityStr, addressIdStr, voucherIdStr, paymentMethodStr);

        int addressId, productId, quantity, paymentMethod;
        Integer voucherId = null;

        try {
            // Kiểm tra null hoặc chuỗi rỗng
            if (addressIdStr == null || addressIdStr.trim().isEmpty()) {
                throw new NumberFormatException("Address ID is missing");
            }
            if (productIdStr == null || productIdStr.trim().isEmpty()) {
                throw new NumberFormatException("Product ID is missing");
            }
            if (quantityStr == null || quantityStr.trim().isEmpty()) {
                throw new NumberFormatException("Quantity is missing");
            }
            if (paymentMethodStr == null || paymentMethodStr.trim().isEmpty()) {
                throw new NumberFormatException("Payment method is missing");
            }

            addressId = Integer.parseInt(addressIdStr);
            productId = Integer.parseInt(productIdStr);
            quantity = Integer.parseInt(quantityStr);
            paymentMethod = Integer.parseInt(paymentMethodStr);

            if (quantity <= 0) {
                throw new NumberFormatException("Quantity must be positive");
            }
            if (paymentMethod != 1 && paymentMethod != 2) {
                throw new NumberFormatException("Invalid payment method");
            }
            if (voucherIdStr != null && !voucherIdStr.equals("not")) {
                if (voucherIdStr.trim().isEmpty()) {
                    throw new NumberFormatException("Voucher ID is missing");
                }
                voucherId = Integer.parseInt(voucherIdStr);
                DiscountDAO discountDAO = new DiscountDAO();
                Discount discount = discountDAO.getDiscountById(voucherId);
                if (discount == null || !isDiscountValid(discount)) {
                    throw new IllegalArgumentException("Invalid or expired discount");
                }
            }
        } catch (NumberFormatException e) {
            String error;
            System.out.println("NumberFormatException: " + e.getMessage());
            System.out.println("productIdStr: " + productIdStr);
            System.out.println("quantityStr: " + quantityStr);
            System.out.println("addressIdStr: " + addressIdStr);
            System.out.println("voucherIdStr: " + voucherIdStr);
            System.out.println("paymentMethodStr: " + paymentMethodStr);
            switch (e.getMessage()) {
                case "Address ID is missing":
                    error = "Địa chỉ không được để trống.";
                    break;
                case "Product ID is missing":
                    error = "Sản phẩm không được để trống.";
                    break;
                case "Quantity is missing":
                    error = "Số lượng không được để trống.";
                    break;
                case "Payment method is missing":
                    error = "Phương thức thanh toán không được để trống.";
                    break;
                case "Quantity must be positive":
                    error = "Số lượng phải lớn hơn 0.";
                    break;
                case "Invalid payment method":
                    error = "Phương thức thanh toán không hợp lệ.";
                    break;
                case "Voucher ID is missing":
                    error = "Mã voucher không hợp lệ.";
                    break;
                default:
                    error = "Dữ liệu đầu vào không hợp lệ: " + e.getMessage();
            }
            request.setAttribute("error", error);
            request.getRequestDispatcher("/WEB-INF/order-confirmation.jsp").forward(request, response);
            return;
        } catch (IllegalArgumentException e) {
            String error;
            System.out.println("IllegalArgumentException: " + e.getMessage());
            System.out.println("productIdStr: " + productIdStr);
            System.out.println("quantityStr: " + quantityStr);
            System.out.println("addressIdStr: " + addressIdStr);
            System.out.println("voucherIdStr: " + voucherIdStr);
            System.out.println("paymentMethodStr: " + paymentMethodStr);
            if (e.getMessage().equals("Invalid or expired discount")) {
                error = "Voucher không hợp lệ hoặc đã hết hạn.";
            } else {
                error = "Lỗi dữ liệu: " + e.getMessage();
            }
            request.setAttribute("error", error);
            request.getRequestDispatcher("/WEB-INF/order-confirmation.jsp").forward(request, response);
            return;
        }

        // Kiểm tra địa chỉ
        AddressDAO addressDAO = new AddressDAO();
        Address address = addressDAO.getAddressById(addressId, user.getUserId());
        if (address == null) {
            request.setAttribute("error", "Địa chỉ không hợp lệ.");
            request.getRequestDispatcher("/WEB-INF/order-confirmation.jsp").forward(request, response);
            return;
        }

        // Kiểm tra sản phẩm
        ProductDAO productDAO = new ProductDAO();
        if (productDAO.getProductById(productId) == null) {
            request.setAttribute("error", "Sản phẩm không tồn tại.");
            request.getRequestDispatcher("/WEB-INF/order-confirmation.jsp").forward(request, response);
            return;
        }

        // Gọi OrderDAO
        OrderDAO orderDAO = new OrderDAO();
        try {
            int orderId = orderDAO.createOrder(
                    user.getUserId(),
                    addressId,
                    productId,
                    quantity,
                    voucherId,
                    paymentMethod
            );
            request.setAttribute("orderId", orderId);
            request.getRequestDispatcher("/WEB-INF/order-success.jsp").forward(request, response);
        } catch (SQLException e) {
            String errorMessage = e.getMessage();
            System.out.println("SQLException: " + errorMessage);
            if (errorMessage.contains("Invalid address")) {
                request.setAttribute("error", "Địa chỉ không hợp lệ.");
            } else if (errorMessage.contains("Product not found")) {
                request.setAttribute("error", "Sản phẩm không tồn tại hoặc không hoạt động.");
            } else if (errorMessage.contains("Insufficient stock")) {
                request.setAttribute("error", "Số lượng tồn kho không đủ.");
            } else if (errorMessage.contains("Invalid or expired discount")) {
                request.setAttribute("error", "Voucher không hợp lệ hoặc đã hết hạn.");
            } else if (errorMessage.contains("Order value does not meet")) {
                request.setAttribute("error", "Giá trị đơn hàng không đáp ứng yêu cầu giảm giá.");
            } else if (errorMessage.contains("Invalid payment method")) {
                request.setAttribute("error", "Phương thức thanh toán không hợp lệ.");
            } else {
                request.setAttribute("error", "Không thể tạo đơn hàng: " + errorMessage);
            }
            request.getRequestDispatcher("/WEB-INF/order-confirmation.jsp").forward(request, response);
        }
    }

    // Hàm thiết lập các thuộc tính request
    private void setRequestAttributes(HttpServletRequest request, String productIdStr, String quantityStr,
            String addressIdStr, String voucherIdStr, String paymentMethodStr) {
        request.setAttribute("productId", productIdStr);
        request.setAttribute("quantity", quantityStr);
        request.setAttribute("addressId", addressIdStr);
        request.setAttribute("voucherId", voucherIdStr);
        request.setAttribute("paymentMethod", paymentMethodStr);
    }

    private boolean isDiscountValid(Discount discount) {
        if (discount == null) {
            return false;
        }
        LocalDate today = LocalDate.now();
        boolean inDateRange = !today.isBefore(discount.getStartDate()) && !today.isAfter(discount.getEndDate());
        boolean underUsageLimit = discount.getUsageLimit() <= 0 || discount.getUsedCount() < discount.getUsageLimit();
        return discount.isIsActive() && inDateRange && underUsageLimit;
    }

}
