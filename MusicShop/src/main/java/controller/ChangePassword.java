/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.UserDAO;
import hashpw.MD5PasswordHasher;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;

/**
 *
 * @author Nguyen Hoang Thai Vinh - CE190384
 */
@WebServlet(name = "ChangePassword", urlPatterns = {"/change-password"})
public class ChangePassword extends HttpServlet {

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
            out.println("<title>Servlet ChangePassword</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ChangePassword at " + request.getContextPath() + "</h1>");
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
        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String oldPassword = request.getParameter("oldPw");
        String newPassword = request.getParameter("newPw");
        String confirmPassword = request.getParameter("confirmPw");

        System.out.println(oldPassword);
        System.out.println(newPassword);
        System.out.println(confirmPassword);
        System.out.println(user.getUserId() + " " + user.getPassword());

        // Verify old password
        if (!MD5PasswordHasher.checkPassword(oldPassword, user.getPassword())) {
            System.out.println(MD5PasswordHasher.hashPassword(oldPassword));
            System.out.println(user.toString());
            System.out.println(user.getPassword());
            request.removeAttribute("oldPwError");
            request.setAttribute("oldPwError", "Old password is incorrect!");
            request.setAttribute("updateFail", "Password change failed!");
            request.setAttribute("view", "password");
            request.getRequestDispatcher("/WEB-INF/user/profile.jsp").forward(request, response);
            return;
        }

        // Verify old password
        if (!newPassword.equals(confirmPassword)) {
            request.removeAttribute("confirmPwError");
            request.setAttribute("confirmPwError", "Password re-entered is incorrect!");
            request.setAttribute("updateFail", "Password change failed!");
            request.setAttribute("view", "password");
            request.getRequestDispatcher("/WEB-INF/user/profile.jsp").forward(request, response);
            return;
        }

        // Validate new password strength
        if (!newPassword.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$")) {
            request.removeAttribute("newPwError");
            request.setAttribute("newPwError", "Password must be at least 8 characters, including uppercase, lowercase, numbers and special characters!");
            request.setAttribute("updateFail", "Password change failed!");
            request.setAttribute("view", "password");
            request.getRequestDispatcher("/WEB-INF/user/profile.jsp").forward(request, response);
            return;
        }

        // Hash the new password
        String newHashPw = MD5PasswordHasher.hashPassword(newPassword);
        boolean isUpdatePw = new UserDAO().updatePasswordByUserId(newHashPw, user.getUserId());

        if (isUpdatePw) {
            user.setPassword(newHashPw);
            session.setAttribute("user", user);
            session.setAttribute("updateSuccess", "Password change success!");
        } else {
            request.setAttribute("updateFail", "Password change failed!");
        }

        response.sendRedirect(request.getContextPath() + "/account?view=password");
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
