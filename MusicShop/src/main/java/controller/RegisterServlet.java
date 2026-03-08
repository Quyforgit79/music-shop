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
@WebServlet(name = "RegisterServlet", urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {

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
            out.println("<title>Servlet RegisterServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet RegisterServlet at " + request.getContextPath() + "</h1>");
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
        request.getRequestDispatcher("/WEB-INF/register.jsp").forward(request, response);
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

        // Get parameters with null checks
        String name = request.getParameter("full_name");
        String account = request.getParameter("account");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");

        // Validate required fields
        if (name == null || account == null || email == null || phone == null || password == null) {
            request.setAttribute("error", "Please fill in all information!");
            request.getRequestDispatcher("/WEB-INF/register.jsp").forward(request, response);
            return;
        }

        // Gán lại để giữ giá trị nếu có lỗi
        request.setAttribute("full_name", name);
        request.setAttribute("account", account);
        request.setAttribute("email", email);
        request.setAttribute("phone", phone);

        boolean hasError = false;

        // Regex kiểm tra
        if (name == null || !name.matches("^[\\p{L}\\s]{3,}$")) {
            request.setAttribute("fullnameError", "Full name must be 3 characters or more, no special characters or numbers.");
            System.out.println("fullnameError");
            hasError = true;
        }

        if (account == null || !account.matches("^[a-zA-Z0-9_]{3,20}$")) {
            request.setAttribute("accountError", "Username must be 3-20 characters, containing only letters, numbers and underscores.");
            System.out.println("accountError");
            hasError = true;
        }

        if (email == null || !email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            request.setAttribute("emailError", "Email is not in correct format..");
            System.out.println("emailError");
            hasError = true;
        }

        if (phone == null || !phone.matches("^0[0-9]{9,10}$")) {
            request.setAttribute("phoneError", "Phone number must start with 0 and have 10-11 digits.");
            System.out.println("phoneError");
            hasError = true;
        }

        if (password == null || !password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$")) {
            request.setAttribute("passwordError", "Password must be at least 8 characters, including uppercase, lowercase, numbers and special characters.");
            System.out.println("passwordError");
            hasError = true;
        }

        if (hasError) {
            request.getRequestDispatcher("WEB-INF/register.jsp").forward(request, response);
            return;
        }

        // Kiểm tra trùng dữ liệu
        UserDAO dao = new UserDAO();
        User existUser = dao.isUserTaken(account, phone, email);

        if (existUser != null) {
            if (existUser.getAccount().equals(account)) {
                request.setAttribute("accountError", "Account already exists.");
            }
            if (existUser.getEmail().equals(email)) {
                request.setAttribute("emailError", "Email already in use.");
            }
            if (existUser.getPhone().equals(phone)) {
                request.setAttribute("phoneError", "Phone number has been registered.");
            }
            request.getRequestDispatcher("WEB-INF/register.jsp").forward(request, response);
            return;
        }

        // Nếu hợp lệ thì tạo user
        User user = new User();
        user.setFullName(name);
        user.setAccount(account);
        user.setEmail(email);
        user.setPhone(phone);
        user.setPassword(MD5PasswordHasher.hashPassword(password));

        if (dao.insertUser(user)) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            response.sendRedirect(request.getContextPath() + "/home");
        } else {
            request.setAttribute("registerError", "Registration failed. Please try again.!");
            request.getRequestDispatcher("WEB-INF/register.jsp").forward(request, response);
        }
    }
}
