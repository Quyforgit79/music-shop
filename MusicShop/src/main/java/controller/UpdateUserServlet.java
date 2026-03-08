/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.UserDAO;
import enums.Gender;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import model.User;

/**
 *
 * @author Nguyen Hoang Thai Vinh - CE190384
 */
@WebServlet(name = "UpdateUserServlet", urlPatterns = {"/updateUser"})
public class UpdateUserServlet extends HttpServlet {

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
        System.out.println("bat dau update user servlet");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        UserDAO userDAO = new UserDAO();
        
        if (user == null) {
            System.out.println("User is null, redirecting to login");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Lấy tham số
        String fullName = request.getParameter("fullName") != null ? request.getParameter("fullName").trim() : "";
        String account = request.getParameter("account") != null ? request.getParameter("account").trim() : "";
        String email = request.getParameter("email") != null ? request.getParameter("email").trim() : "";
        String phone = request.getParameter("phone") != null ? request.getParameter("phone").trim() : "";
        String gender = request.getParameter("gender") != null ? request.getParameter("gender").trim() : "";
        String day = request.getParameter("birth_day") != null ? request.getParameter("birth_day") : "";
        String month = request.getParameter("birth_month") != null ? request.getParameter("birth_month") : "";
        String year = request.getParameter("birth_year") != null ? request.getParameter("birth_year") : "";

        // Log để debug
        System.out.println("Received: fullName=" + fullName + ", account=" + account + 
                           ", email=" + email + ", phone=" + phone + ", gender=" + gender + 
                           ", birth_day=" + day + ", birth_month=" + month + ", birth_year=" + year);

        // Xóa các lỗi cũ
        session.removeAttribute("accountError");
        session.removeAttribute("fullNameError");
        session.removeAttribute("emailError");
        session.removeAttribute("phoneError");
        session.removeAttribute("genderError");
        session.removeAttribute("birthdateError");
        session.removeAttribute("birthdateDayError");
        session.removeAttribute("birthdateMonthError");
        session.removeAttribute("birthdateYearError");
        session.removeAttribute("updateFail");
        session.removeAttribute("updateSuccess");

        // Lưu giá trị tạm thời vào session
        session.setAttribute("tempFullName", fullName);
        session.setAttribute("tempAccount", account);
        session.setAttribute("tempEmail", email);
        session.setAttribute("tempPhone", phone);
        session.setAttribute("tempGender", gender);
        session.setAttribute("birth_day", day);
        session.setAttribute("birth_month", month);
        session.setAttribute("birth_year", year);

        // Kiểm tra hợp lệ
        boolean isValid = true;

        // Kiểm tra account
        if (account.isEmpty()) {
            session.setAttribute("accountError", "ACcount cannot be left blank");
            isValid = false;
        }

        // Kiểm tra full name
        if (fullName.isEmpty()) {
            session.setAttribute("fullNameError", "Full name cannot be left blank");
            isValid = false;
        }

        // Kiểm tra email
        if (email.isEmpty() || !email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            session.setAttribute("emailError", "Invelid email");
            isValid = false;
        }

        // Kiểm tra phone
        if (!phone.isEmpty() && !phone.matches("^\\d{10,11}$")) {
            session.setAttribute("phoneError", "Phone number must be 10 to 11 digits");
            isValid = false;
        }

        // Kiểm tra gender
        if (gender.isEmpty()) {
            session.setAttribute("genderError", "Please select gender");
            isValid = false;
        } else {
            try {
                int genderValue = Integer.parseInt(gender);
                if (genderValue < 1 || genderValue > 3) {
                    session.setAttribute("genderError", "Invalid gender");
                    isValid = false;
                } else {
                    user.setGender(Gender.fromGender(genderValue));
                }
            } catch (NumberFormatException e) {
                session.setAttribute("genderError", "Invalid gender");
                isValid = false;
            }
        }

        // Kiểm tra birthdate
        if (!day.isEmpty() && !month.isEmpty() && !year.isEmpty()) {
            try {
                int d = Integer.parseInt(day);
                int m = Integer.parseInt(month);
                int y = Integer.parseInt(year);
                if (y < 1900 || y > LocalDate.now().getYear()) {
                    session.setAttribute("birthdateYearError", "Year must be from 1900 to " + LocalDate.now().getYear());
                    isValid = false;
                } else if (m < 1 || m > 12) {
                    session.setAttribute("birthdateMonthError", "Month must be from 1 to 12");
                    isValid = false;
                } else if (d < 1 || d > 31) {
                    session.setAttribute("birthdateDayError", "Date must be between 1 and 31");
                    isValid = false;
                } else {
                    try {
                        LocalDate birthdate = LocalDate.of(y, m, d);
                        user.setBirthdate(birthdate);
                    } catch (DateTimeException e) {
                        session.setAttribute("birthdateError", "Invalid date (e.g. April 31 or February 29 in non-leap year))");
                        isValid = false;
                    }
                }
            } catch (NumberFormatException e) {
                session.setAttribute("birthdateError", "Invalid date format (day, month, year must be numbers)");
                isValid = false;
            }
        } else if (!day.isEmpty() || !month.isEmpty() || !year.isEmpty()) {
            session.setAttribute("birthdateError", "Please select full day, month, year");
            isValid = false;
        }

        // Kiểm tra trùng lặp
        int userId = user.getUserId();
        if (isValid && isDuplicateUserInfo(userDAO, account, email, phone, userId, session)) {
            session.setAttribute("updateFail", "Account, email or phone number already exists!");
            isValid = false;
        }

        // Nếu không hợp lệ, chuyển hướng
        if (!isValid) {
            session.setAttribute("updateFail", "Update information failed!");
            setBirthdateAttributes(user, session);
            response.sendRedirect(request.getContextPath() + "/account?view=info");
            return;
        }

        // Cập nhật thông tin người dùng
        user.setAccount(account);
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPhone(phone.isEmpty() ? null : phone);

        // Lưu vào cơ sở dữ liệu
        if (userDAO.update(user)) {
            session.setAttribute("user", user);
            session.setAttribute("updateSuccess", "Information updated successfully!");
            // Xóa các giá trị tạm thời
            session.removeAttribute("tempFullName");
            session.removeAttribute("tempAccount");
            session.removeAttribute("tempEmail");
            session.removeAttribute("tempPhone");
            session.removeAttribute("tempGender");
            session.removeAttribute("birth_day");
            session.removeAttribute("birth_month");
            session.removeAttribute("birth_year");
        } else {
            session.setAttribute("updateFail", "Update information failed!");
        }

        // Cập nhật birthdate attributes
        setBirthdateAttributes(user, session);
        response.sendRedirect(request.getContextPath() + "/account?view=info");
    }


    private void setBirthdateAttributes(User user, HttpSession session) {
        int currentYear = LocalDate.now().getYear();
        DateTimeFormatter textFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String birthdateInputValue = user.getBirthdate() != null
                ? user.getBirthdate().format(inputFormatter)
                : "";
        String birthdateTextValue = user.getBirthdate() != null
                ? user.getBirthdate().format(textFormatter)
                : "";
        session.setAttribute("currentYear", currentYear);
        session.setAttribute("birthdateInputValue", birthdateInputValue);
        session.setAttribute("birthdateTextValue", birthdateTextValue);
    }

    private boolean isDuplicateUserInfo(UserDAO userDAO, String account, String email, String phone, int userId, HttpSession session) {
        boolean hasError = false;
        if (userDAO.isAccountTaken(account, userId)) {
            session.setAttribute("accountError", "Account name already exists");
            hasError = true;
        }
        if (userDAO.isEmailTaken(email, userId)) {
            session.setAttribute("emailError", "Email has been registered");
            hasError = true;
        }
        if (!phone.isEmpty() && userDAO.isPhoneTaken(phone, userId)) {
            session.setAttribute("phoneError", "Phone number already in use");
            hasError = true;
        }
        return hasError;
    }

}
