/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.OrderDAO;
import dao.ProductDAO;
import dao.ProductImageDAO;
import dao.ReviewDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import model.Product;
import model.ProductImage;
import model.Review;
import model.User;
import util.RelatedProduct;

/**
 *
 * @author Nguyen Hoang Thai Vinh - CE190384
 */
@WebServlet(name = "ProductServlet", urlPatterns = {"/product"})
public class ProductServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ProductServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ProductServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
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
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");
        clearSession(request, response, session);

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        String productIdStr = request.getParameter("id");
        if (productIdStr == null || productIdStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        int productId;
        try {
            productId = Integer.parseInt(productIdStr);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/error-page/404page.jsp");
            return;
        }

        ProductDAO productDAO = new ProductDAO();
        ReviewDAO reviewDAO = new ReviewDAO();
        OrderDAO orderDAO = new OrderDAO();
        ProductImageDAO productImageDAO = new ProductImageDAO();
        
        Product product = productDAO.getProductById(productId);

        boolean hasPurchased = orderDAO.hasPurchased(user.getUserId(), productId);
        if (hasPurchased) {
            request.setAttribute("hasPurchased", "");
        }

        List<Review> userReview = reviewDAO.getUserReview(user.getUserId(), productId);
        if (userReview != null) {
            request.setAttribute("userReview", userReview);
        }
        
        double avgRating = productDAO.getAverageRating(productId);

        // Fetch all reviews for the product
        List<Review> reviews = reviewDAO.getReviewsProductByProductId(productId);
        List<ProductImage> productImages = productImageDAO.getAllProductImages(productId);
        List<Product> relatedList = productDAO.getRelatedProducts(product.getProductId(), RelatedProduct.RELATED_PRODUCT_LIMIT);
        System.out.println(reviews);
        request.setAttribute("product", product);
        request.setAttribute("avgRating", avgRating);
        request.setAttribute("reviews", reviews);
        request.setAttribute("relatedList", relatedList);
        request.setAttribute("productImages", productImages);
        request.getRequestDispatcher("/WEB-INF/product-view.jsp").forward(request, response);

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

    }

    protected void clearSession(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        String[] attributes = {"addFail", "addSuccess", "updateFail", "updateSuccess", "deleteSuccess", "deleteFail"};
        for (String attr : attributes) {
            Object val = session.getAttribute(attr);
            if (val != null) {
                request.setAttribute(attr, val);
                session.removeAttribute(attr);
            }
        }
    }
}
