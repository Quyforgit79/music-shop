/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.OrderDAO;
import dao.OrderDetailDAO;
import dao.ReviewDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import model.OrderDetail;
import model.User;

/**
 *
 * @author Nguyen Hoang Thai Vinh - CE190384
 */
@WebServlet(name = "ReviewServlet", urlPatterns = {"/review"})
public class ReviewServlet extends HttpServlet {

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

        String orderIdStr = request.getParameter("orderId");
        System.out.println(orderIdStr);
        int orderId;
        try {
            orderId = Integer.parseInt(orderIdStr);
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/error-page/404page.jsp");
            return;
        }

        OrderDetailDAO odDAO = new OrderDetailDAO();
        List<OrderDetail> odList = odDAO.getOrderDetailsByOrderId(orderId);

        if (odList == null) {
            session.setAttribute("updateFail", "Opp! There are some thing wrong.");
            response.sendRedirect(request.getContextPath() + "/account?view=info");
            return;
        }

        request.setAttribute("orderId", orderId);
        request.setAttribute("productId", odList.get(0).getProduct().getProductId());
        request.setAttribute("productName", odList.get(0).getProduct().getName());
        request.getRequestDispatcher("/WEB-INF/product-review.jsp").forward(request, response);
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
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        User user = (User) session.getAttribute("user");

        String action = request.getParameter("action");
        ReviewDAO reviewDAO = new ReviewDAO();

        switch (action) {
            case "delete":
                delete(request, response, reviewDAO, session);
                break;
            case "edit":
                edit(request, response, reviewDAO, session);
                break;
            case "createReview":
                createReview(request, response, reviewDAO, session);
                break;
            default:
                throw new AssertionError();
        }
    }

    private void delete(HttpServletRequest request, HttpServletResponse response, ReviewDAO reviewDAO, HttpSession session)
            throws IOException, ServletException {
        String reviewIdStr = request.getParameter("reviewId");
        String productIdStr = request.getParameter("productId");

        int reviewId, productId;
        try {
            reviewId = Integer.parseInt(reviewIdStr);
            productId = Integer.parseInt(productIdStr);
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/error-page/404page.jsp");
            return;
        }

        boolean isDelete = reviewDAO.deleteReview(reviewId);
        if (!isDelete) {
            session.setAttribute("deleteFail", "Delete comment Fail!");
            response.sendRedirect(request.getContextPath() + "/product?id=" + productId);
        } else {
            session.setAttribute("deleteSuccess", "Delete review successfully!");
            response.sendRedirect(request.getContextPath() + "/product?id=" + productId);
        }
    }

    private void edit(HttpServletRequest request, HttpServletResponse response, ReviewDAO reviewDAO, HttpSession session)
            throws IOException, ServletException {
        String reviewIdStr = request.getParameter("reviewId");
        String productIdStr = request.getParameter("productId");
        String comment = request.getParameter("reviewText");
        String ratingStr = request.getParameter("rating");

        System.out.println("Product ID: " + productIdStr);
        System.out.println("Rating: " + ratingStr);
        System.out.println("Comment: " + comment);
        System.out.println("reviewID: " + reviewIdStr);

        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        int reviewId, productId, rating;
        try {
            reviewId = Integer.parseInt(reviewIdStr);
            productId = Integer.parseInt(productIdStr);
            rating = Integer.parseInt(ratingStr);
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/error-page/404page.jsp");
            return;
        }

        if (comment == null || comment.isEmpty()) {
            session.setAttribute("updateFail", "Comments cannot be left blank!");
            response.sendRedirect(request.getContextPath() + "/product?id=" + productId);
        }

        boolean isEdit = reviewDAO.updateReview(rating, comment, reviewId, user.getUserId());
        if (!isEdit) {
            session.setAttribute("updateFail", "Edit comment failed!");
            response.sendRedirect(request.getContextPath() + "/product?id=" + productId);
        } else {
            session.setAttribute("updateSuccess", "Comment edited successfully!");
            response.sendRedirect(request.getContextPath() + "/product?id=" + productId);
        }
    }

    private void createReview(HttpServletRequest request, HttpServletResponse response, ReviewDAO reviewDAO, HttpSession session) throws ServletException, IOException {
        User user = (User) session.getAttribute("user");
        String productIdStr = request.getParameter("productId");
        String orderIdStr = request.getParameter("orderId");
        String ratingStr = request.getParameter("rating");
        String comment = request.getParameter("reviewText");

        int productId, orderId, rating;
        try {
            productId = Integer.parseInt(productIdStr);
            orderId = Integer.parseInt(orderIdStr);
            rating = Integer.parseInt(ratingStr);
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/error-page/404page.jsp");
            return;
        }

        boolean added = reviewDAO.createReview(productId, user.getUserId(), rating, comment);
        if (added) {
            session.setAttribute("addSuccess", "Review posted successfully!");
            OrderDAO orderDAO = new OrderDAO();
            boolean isUpdate = orderDAO.setOrderReviewed(orderId, user.getUserId());

            if (!isUpdate) {
                session.setAttribute("addFail", "An error occurred while submitting the product review.!");
            }
        } else {
            session.setAttribute("addFail", "Failed to post review.");
        }

        response.sendRedirect(request.getContextPath() + "/account?view=order");
    }
}
