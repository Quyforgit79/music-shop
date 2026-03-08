/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package filter;

import dao.ProductDAO;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import model.Product;
import model.User;

/**
 *
 * @author Nguyen Hoang Thai Vinh - CE190384
 */
@WebFilter(filterName = "OrderConfirmFilter", urlPatterns = {
    "/order-confirm",})
public class OrderConfirmFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("Start order confirm filter");
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        // Kiểm tra login
        if (session == null || session.getAttribute("user") == null) {
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        User u = (User) session.getAttribute("user");

        // Lấy tham số
        String quantityStr = req.getParameter("quantity");
        String productIdStr = req.getParameter("productId");

        if (quantityStr == null || productIdStr == null) {
            res.sendRedirect(req.getContextPath() + "/error-page/404page.jsp");
            return;
        }

        int quantity, productId;
        try {
            quantity = Integer.parseInt(quantityStr);
            productId = Integer.parseInt(productIdStr);
        } catch (NumberFormatException e) {
            res.sendRedirect(req.getContextPath() + "/error-page/404page.jsp");
            return;
        }

        // Kiểm tra tồn kho
        ProductDAO dao = new ProductDAO();
        Product p = dao.getProductById(productId);
        if (p == null) {
            res.sendRedirect(req.getContextPath() + "/error-page/404page.jsp");
            return;
        }

        if (quantity > p.getStockQuantity()) {
            session.setAttribute("updateFail", "Quantity is invalid out of stock!");
            res.sendRedirect(req.getContextPath() + "/product?id=" + productId);
            return;
        }

        System.out.println("End order confirm filter");
        chain.doFilter(request, response);
    }

}
