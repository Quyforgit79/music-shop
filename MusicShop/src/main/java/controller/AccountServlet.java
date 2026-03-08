/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.AddressDAO;
import dao.CartDAO;
import dao.OrderDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import model.Address;
import model.Cart;
import model.Order;
import model.User;

/**
 *
 * @author Nguyen Hoang Thai Vinh - CE190384
 */
@WebServlet(name = "AccountServlet", urlPatterns = {"/account"})

public class AccountServlet extends HttpServlet {

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
        HttpSession session = request.getSession(false); // Get the current session if it exists
        User user = (User) session.getAttribute("user"); // Retrieve the logged-in user from the session
        clearSession(request, response, session);

        // Determine which "view" should be displayed on the profile page
        String view = request.getParameter("view");
        if (view == null) {
            view = "info"; // Default view is "info"
        }

        // Handle logic based on the selected view
        switch (view) {
            case "info":
                // Display user information such as birthdate and gender
                int currentYear = LocalDate.now().getYear();
                DateTimeFormatter textFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                String birthdateInputValue = user.getBirthdate() != null
                        ? user.getBirthdate().format(inputFormatter)
                        : "";
                String birthdateTextValue = user.getBirthdate() != null
                        ? user.getBirthdate().format(textFormatter)
                        : "Not set";  // Display "Not set" if no birthdate
                String gender = user.getGender() != null ? user.getGender().getLabel() : "Not set";

                // Gán thông tin dùng chung
                session.setAttribute("user", user);
                session.setAttribute("currentYear", currentYear);
                session.setAttribute("birthdateInputValue", birthdateInputValue);
                session.setAttribute("birthdateTextValue", birthdateTextValue);
                break;

            case "address":
                // Retrieve user addresses
                AddressDAO addressDAO = new AddressDAO();
                List<Address> addresses = addressDAO.getAddressesByUserId(user.getUserId());
                if (addresses == null) {
                    addresses = new ArrayList<>();
                    request.setAttribute("error", "Cannot load addresses");
                }
                request.setAttribute("addresses", addresses);
                break;

            case "cart":
                // Retrieve user's cart and calculate total price
                CartDAO cartDAO = new CartDAO();
                List<Cart> carts = cartDAO.getCartsByUserId(user.getUserId());
                BigDecimal total = BigDecimal.ZERO;
                if (carts != null) {
                    for (Cart c : carts) {
                        total = total.add(c.getProduct().getPrice());
                    }
                } else {
                    carts = new ArrayList<>();
                    request.setAttribute("error", "Cannot load cart");
                }
                request.setAttribute("carts", carts);
                request.setAttribute("total", total);
                break;
            case "order":
                // Retrieve current orders of the user
                OrderDAO orderDAO = new OrderDAO();
                System.out.println(user.getUserId());
                List<Order> orders = orderDAO.getOrdersByUserId(user.getUserId());
                if (orders == null) {
                    orders = new ArrayList<>();
                    request.setAttribute("error", "Cannot load orders");
                }

                request.setAttribute("orders", orders);
                break;
            case "history":
                // Retrieve user's order history
                OrderDAO dao = new OrderDAO();
                System.out.println(user.getUserId());
                List<Order> histories = dao.getOrdersHistoryByUserId(user.getUserId());
                if (histories == null) {
                    orders = new ArrayList<>();
                    request.setAttribute("error", "Cannot load orders");
                }

                request.setAttribute("histories", histories);
                break;
            case "password":
                break;
            default:
                // Handle invalid "view" parameter
                request.setAttribute("error", "Invalid view parameter");
                view = "info";
                break;
        }

        // Forward to profile.jsp with the chosen view
        request.setAttribute("view", view);
        request.getRequestDispatcher("/WEB-INF/user/profile.jsp").forward(request, response);
    }

    /**
     * Moves specific attributes from the session to the request and removes
     * them from the session. This is used for displaying temporary messages or
     * data.
     */
    protected void clearSession(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        // Thông báo
        String[] messages = {"deleteSuccess", "deleteFail", "addSuccess", "addFail", "updateSuccess", "updateFail",
                             "accountError", "fullNameError", "emailError", "phoneError", "genderError",
                             "birthdateError", "birthdateDayError", "birthdateMonthError", "birthdateYearError"};
        for (String message : messages) {
            Object val = session.getAttribute(message);
            if (val != null) {
                request.setAttribute(message, val);
                session.removeAttribute(message);
            }
        }

        // Dữ liệu nhập lại
        String[] fields = {"receiverName", "receiverPhone", "street", "ward", "district", "city",
                           "nameError", "phoneError", "streetError", "cityError",
                           "tempFullName", "tempAccount", "tempEmail", "tempPhone", "tempGender",
                           "birth_day", "birth_month", "birth_year"};
        for (String field : fields) {
            Object val = session.getAttribute(field);
            if (val != null) {
                request.setAttribute(field, val); // Move attribute to request
                session.removeAttribute(field);  // Remove it from session
            }
        }
    }
}
