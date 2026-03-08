/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.AddressDAO;
import dao.DiscountDAO;
import dao.ProductDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import model.Address;
import model.Discount;
import model.Product;
import model.User;

/**
 *
 * @author Nguyen Hoang Thai Vinh - CE190384
 */
@WebServlet(name = "OrderConfirmServlet", urlPatterns = {"/order-confirm"})
public class OrderConfirmServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleRequest(request, response, true);
    }

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
        handleRequest(request, response, false);
    }

    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, boolean isPost) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            request.setAttribute("error", "Vui lòng đăng nhập để tiếp tục.");
            response.sendRedirect("login");
            return;
        }
        User user = (User) session.getAttribute("user");

        String productIdParam = request.getParameter("productId");
        String quantityStr = request.getParameter("quantity");
        String voucherIdStr = request.getParameter("voucherId");
        String paymentMethod = request.getParameter("paymentMethod");
        String shippingMethod = request.getParameter("shippingMethod");

        if (paymentMethod == null || paymentMethod.isEmpty()) {
            paymentMethod = "1";
        }
        if (shippingMethod == null || shippingMethod.isEmpty()) {
            shippingMethod = "1";
        }

        int productId = 0, quantity = 1;
        if (productIdParam == null || quantityStr == null) {
            request.setAttribute("error", "Sản phẩm hoặc số lượng không hợp lệ.");
            response.sendRedirect("shop");
            return;
        }

        try {
            productId = Integer.parseInt(productIdParam);
            quantity = Integer.parseInt(quantityStr);
        } catch (Exception e) {
            request.setAttribute("error", "Sản phẩm hoặc số lượng không hợp lệ.");
            response.sendRedirect("shop");
            return;
        }

        request.setAttribute("productId", String.valueOf(productId));
        request.setAttribute("quantity", String.valueOf(quantity));
        request.setAttribute("paymentMethod", paymentMethod);
        request.setAttribute("shippingMethod", shippingMethod);

        Product product = new ProductDAO().getProductById(productId);
        if (product == null) {
            request.setAttribute("error", "Không tìm thấy sản phẩm.");
            response.sendRedirect("shop");
            return;
        }

        AddressDAO addressDAO = new AddressDAO();
        List<Address> addressList = addressDAO.getAddressesByUserId(user.getUserId());
        Address defaultAddress = addressDAO.getDefaultAddress(user.getUserId());
        if (defaultAddress == null && !addressList.isEmpty()) {
            defaultAddress = addressList.get(0);
            addressDAO.setDefaultAddress(defaultAddress.getAddressId(), user.getUserId());
        }
        if (defaultAddress == null) {
            request.setAttribute("addressFail", "no_address");
        }

        List<Discount> discountList = new ArrayList<>();
        Discount productDiscount = new DiscountDAO().getDiscountByProductId(productId);
        Discount voucherDiscount = new DiscountDAO().getCasualDiscount();
        if (productDiscount != null) {
            discountList.add(productDiscount);
        }
        if (voucherDiscount != null) {
            discountList.add(voucherDiscount);
        }

        List<Discount> validDiscounts = new ArrayList<>();
        for (Discount d : discountList) {
            if (isDiscountValid(d)) {
                validDiscounts.add(d);
            }
        }

        BigDecimal quantityBD = new BigDecimal(quantity);
        BigDecimal productPrice = product.getPrice();
        BigDecimal productTotalPrice = productPrice.multiply(quantityBD);
        BigDecimal discountAmount = BigDecimal.ZERO;
        BigDecimal totalAmount = productTotalPrice; // Mặc định là giá sản phẩm
        Discount selectedDiscount = null;

        if (voucherIdStr != null && !voucherIdStr.equals("not")) {
            try {
                int voucherId = Integer.parseInt(voucherIdStr);
                selectedDiscount = new DiscountDAO().getDiscountById(voucherId);
                if (selectedDiscount != null && isDiscountValid(selectedDiscount)) {
                    switch (selectedDiscount.getDiscountType().getCode()) {
                        case 1:
                            discountAmount = productTotalPrice.multiply(
                                    selectedDiscount.getDiscountValue().divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)
                            );
                            break;
                        case 2:
                            discountAmount = selectedDiscount.getDiscountValue();
                            if (discountAmount.compareTo(productTotalPrice) > 0) {
                                discountAmount = productTotalPrice;
                            }
                            break;
                        default:
                            discountAmount = BigDecimal.ZERO;
                    }
                    totalAmount = productTotalPrice.subtract(discountAmount);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid voucher ID: " + voucherIdStr);
            }
        }

        request.setAttribute("product", product);
        request.setAttribute("quantity", quantity);
        request.setAttribute("productPrice", productPrice);
        request.setAttribute("totalAmount", totalAmount);
        request.setAttribute("discountAmount", discountAmount);
        request.setAttribute("selectedDiscount", selectedDiscount);
        request.setAttribute("discountList", validDiscounts);
        request.setAttribute("defaultAddress", defaultAddress);
        request.setAttribute("addressList", addressList);
        request.setAttribute("shippingMethod", shippingMethod);

        request.getRequestDispatcher("/WEB-INF/order-confirmation.jsp").forward(request, response);
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
