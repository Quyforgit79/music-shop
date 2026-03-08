/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.OrderDAO;
import dao.ReturnOrderDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import model.OrderViewModel;
import model.User;

/**
 *
 * @author Nguyen Hoang Thai Vinh - CE190384
 */
@WebServlet(name = "OrderServlet", urlPatterns = {"/order"})
public class OrderServlet extends HttpServlet {

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

        int orderId;
        try {
            orderId = Integer.parseInt(request.getParameter("id"));
        } catch (Exception e) {
            response.sendRedirect(request.getDispatcherType() + "account?view=order");
            return;
        }

        OrderDAO dao = new OrderDAO();
        OrderViewModel order = dao.getOrderView(orderId);

        BigDecimal discountValue = order.getDiscountAmount();

        if (order.getDiscountType() == null) {
            request.setAttribute("discountNull", "No discount applied");
            request.setAttribute("order", order);
        } else {
            if (order.getDiscountType().getCode() == 1) {
                request.setAttribute("discountPercent", discountValue);
                request.setAttribute("discountValue", null);
            } else {
                request.setAttribute("discountValue", discountValue);
                request.setAttribute("discountPercent", null);
            }
            request.setAttribute("order", order);
        }

        request.getRequestDispatcher("/WEB-INF/order-view.jsp").forward(request, response);

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
        String action = request.getParameter("action");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        switch (action) {
            case "delete":
                deleteOrder(request, response, session, user);
                break;
            case "deleteAll":
                deleteAllOrder(request, response, session, user);
                break;
            case "createReturn":
                createReturnOrder(request, response, session, user);
                break;
            default:
                throw new AssertionError();
        }

    }

    private void deleteOrder(HttpServletRequest request, HttpServletResponse response, HttpSession session, User user)
            throws ServletException, IOException {
        OrderDAO dao = new OrderDAO();
        String orderIdStr = request.getParameter("orderId");

        int orderId;
        try {
            orderId = Integer.parseInt(orderIdStr);
        } catch (Exception e) {
            session.setAttribute("deleteFail", "Delete failed order");
            response.sendRedirect(request.getContextPath() + "/account?view=history");
            return;
        }

        boolean isDelete = dao.deleteOrderById(orderId, user.getUserId());

        if (isDelete) {
            session.setAttribute("deleteSuccess", "Order deleted successfully");
            response.sendRedirect(request.getContextPath() + "/account?view=history");
        } else {
            session.setAttribute("deleteFail", "Delete failed order");
            response.sendRedirect(request.getContextPath() + "/account?view=history");
        }
    }

    private void deleteAllOrder(HttpServletRequest request, HttpServletResponse response, HttpSession session, User user)
            throws ServletException, IOException {
        OrderDAO dao = new OrderDAO();

        boolean isDelete = dao.deleteAllOrder(user.getUserId());

        if (isDelete) {
            session.setAttribute("deleteSuccess", "Order deleted successfully");
            response.sendRedirect(request.getContextPath() + "/account?view=history");
        } else {
            session.setAttribute("deleteFail", "Delete failed order");
            response.sendRedirect(request.getContextPath() + "/account?view=history");
        }
    }

    private void createReturnOrder(HttpServletRequest request, HttpServletResponse response, HttpSession session, User user)
            throws ServletException, IOException {
        String orderIdStr = request.getParameter("id");
        String reason = request.getParameter("reason");

        int orderId;
        try {
            orderId = Integer.parseInt(orderIdStr);
        } catch (Exception e) {
            session.setAttribute("updateFail", "Return order Fail!");
            System.out.println("err 1");
            response.sendRedirect(request.getContextPath() + "/account?view=order");
            return;
        }

        if (reason == null || reason.trim().isEmpty()) {
            System.out.println("err 2");
            session.setAttribute("updateFail", "Return order Fail!");
            response.sendRedirect(request.getContextPath() + "/account?view=order");
            return;
        }

        ReturnOrderDAO dao = new ReturnOrderDAO();
        boolean isReturn = dao.createReturnOrder(user.getUserId(), orderId, reason);

        if (isReturn) {
            session.setAttribute("updateSuccess", "Cancellation request sent");
            response.sendRedirect(request.getContextPath() + "/account?view=order");
        } else {
            System.out.println("err 3");
            session.setAttribute("updateFail", "Return order Fail!");
            response.sendRedirect(request.getContextPath() + "/account?view=order");
        }
    }
}
