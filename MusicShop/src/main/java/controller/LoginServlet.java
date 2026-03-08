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
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;

/**
 *
 * @author Nguyen Hoang Thai Vinh - CE190384
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

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
            out.println("<title>Servlet LoginServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet LoginServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
        String savedUserAccount = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userAccount".equals(cookie.getName())) {
                    savedUserAccount = cookie.getValue();
                    break;
                }
            }
        }

        request.setAttribute("savedUserAccount", savedUserAccount);
        request.getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
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
        String userInfo = request.getParameter("info");
        String password = request.getParameter("password");
        String rememberMe = request.getParameter("rememberMe");

        if (userInfo == null || userInfo.isEmpty() || password == null || password.isEmpty()) {
            request.setAttribute("error", "Please enter your account and password.");
            request.getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
            return;
        }

        if (userInfo != null) {
            userInfo = userInfo.trim();
        }
        if (password != null) {
            password = password.trim();
        }

        String hashPassword = MD5PasswordHasher.hashPassword(password);
        UserDAO userDAO = new UserDAO();
        User loginUser = userDAO.getUserLogin(userInfo, hashPassword);

        if (loginUser != null) {
            if (!loginUser.isIsActive()) {
                request.setAttribute("info", userInfo);
                request.setAttribute("error", "Your account has been locked.");
                request.getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
                return;
            }
            HttpSession session = request.getSession();

            // Remember me cookie
            if ("on".equals(rememberMe)) {
                Cookie userCookie = new Cookie("userAccount", loginUser.getAccount());
                userCookie.setMaxAge(7 * 24 * 60 * 60); // 7 ngày
                userCookie.setPath(request.getContextPath());
                response.addCookie(userCookie);
            } else {
                Cookie userCookie = new Cookie("userAccount", "");
                userCookie.setMaxAge(0);
                userCookie.setPath(request.getContextPath());
                response.addCookie(userCookie);
            }

            // Đặt session user
            session.setAttribute("user", loginUser);
            session.setAttribute("message", "Welcome " + loginUser.getAccount());

            // Kiểm tra redirect URL trước khi phân quyền
            String redirectUrl = (String) session.getAttribute("redirectAfterLogin");
            session.removeAttribute("redirectAfterLogin");

            if (redirectUrl != null && !redirectUrl.isEmpty()) {
                response.sendRedirect(redirectUrl);
                return;
            }
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        } else {
            request.setAttribute("info", userInfo);
            request.setAttribute("error", "User name or password is incorrect.");
            request.getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
        }
    }
}
