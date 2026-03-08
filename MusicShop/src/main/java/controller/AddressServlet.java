/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.AddressDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Address;
import model.User;
import java.io.IOException;

@WebServlet(name = "AddressServlet", urlPatterns = {"/address"})
public class AddressServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        // Redirect to login if no user session exists
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        User user = (User) session.getAttribute("user");

        // Determine which action to perform based on the 'action' parameter
        String action = request.getParameter("action");
        AddressDAO addressDAO = new AddressDAO();

        switch (action) {
            case "add":
                addAddress(request, response, user, addressDAO, session);
                break;
            case "update":
                updateAddress(request, response, user, addressDAO, session);
                break;
            case "delete":
                deleteAddress(request, response, user, addressDAO, session);
                break;
            case "updateDefaultAddress":
                updateDefaultAddress(request, response, user, addressDAO);
                break;
        }
    }

    // Handle adding a new address
    private void addAddress(HttpServletRequest request, HttpServletResponse response, User user, AddressDAO addressDAO, HttpSession session)
            throws ServletException, IOException {
        // Retrieve form parameters
        String receiverName = request.getParameter("receiverName");
        String receiverPhone = request.getParameter("receiverPhone");
        String street = request.getParameter("street");
        String ward = request.getParameter("ward");
        String district = request.getParameter("district");
        String city = request.getParameter("city");
        String isSetDefault = request.getParameter("isDefault");
        String styleStr = request.getParameter("style");

        // Validate required fields
        if (receiverName == null || receiverPhone == null || street == null || city == null) {
            // Store input values back to session for re-render
            session.setAttribute("addFail", "Add address fail! All required fields must be filled.");
            session.setAttribute("receiverName", receiverName);
            session.setAttribute("receiverPhone", receiverPhone);
            session.setAttribute("street", street);
            session.setAttribute("ward", ward);
            session.setAttribute("district", district);
            session.setAttribute("city", city);
            session.setAttribute("style", styleStr);
            session.setAttribute("isDefault", "true".equals(isSetDefault));
            response.sendRedirect(request.getContextPath() + "/account?view=address");
            return;
        }

        // Validate fields with regex
        boolean hasError = false;
        if (!receiverName.matches("^[\\p{L} .'-]+$")) {
            session.setAttribute("nameError", "Full name is invalid.");
            hasError = true;
        }
        if (!receiverPhone.matches("^(0[2-9][0-9]{8,9})$")) {
            session.setAttribute("phoneError", "Phone number must be 10-11 digits and start with 0.");
            hasError = true;
        }
        if (!street.matches("^.{5,100}$")) {
            session.setAttribute("streetError", "Street must be 5-100 characters.");
            hasError = true;
        }
        if (city.trim().isEmpty()) {
            session.setAttribute("cityError", "City/Province is required.");
            hasError = true;
        }

        // Parse style parameter (1-3), fallback to 1
        int style = 1;
        if (styleStr != null && !styleStr.trim().isEmpty()) {
            try {
                style = Integer.parseInt(styleStr);
                if (style < 1 || style > 3) {
                    style = 1; // fallback nếu ngoài range
                }
            } catch (NumberFormatException e) {
                style = 1;
            }
        }

        // If there are validation errors, redirect back with errors
        if (hasError) {
            session.setAttribute("addFail", "Add address fail!");
            session.setAttribute("receiverName", receiverName);
            session.setAttribute("receiverPhone", receiverPhone);
            session.setAttribute("street", street);
            session.setAttribute("ward", ward);
            session.setAttribute("district", district);
            session.setAttribute("city", city);
            session.setAttribute("style", styleStr);
            session.setAttribute("isDefault", "true".equals(isSetDefault));
            response.sendRedirect(request.getContextPath() + "/account?view=address");
            return;
        }

        // Create a new Address object
        Address address = new Address();
        address.setUser(user);
        address.setReceiverName(receiverName);
        address.setReceiverPhone(receiverPhone);
        address.setStreet(street);
        address.setWard(ward != null ? ward : null);
        address.setDistrict(district != null ? district : null);
        address.setCity(city);
        address.setType(enums.AddressStyle.fromStyle(style));
        address.setIsDefault("true".equals(isSetDefault));

        // Insert address into database
        boolean isInsert = addressDAO.insertAddress(address);
        if (!isInsert) {
            session.setAttribute("addFail", "Add address failed!");
            response.sendRedirect(request.getContextPath() + "/account?view=address");
            return;
        }

        session.setAttribute("addSuccess", "Add address success!");
        response.sendRedirect(request.getContextPath() + "/account?view=address");
    }

    // Handle updating an existing address
    private void updateAddress(HttpServletRequest request, HttpServletResponse response, User user, AddressDAO addressDAO, HttpSession session)
            throws ServletException, IOException {
        // Get parameters for the update
        String addressIdStr = request.getParameter("addressId");
        String receiverName = request.getParameter("receiverName");
        String receiverPhone = request.getParameter("receiverPhone");
        String street = request.getParameter("street");
        String ward = request.getParameter("ward");
        String district = request.getParameter("district");
        String city = request.getParameter("city");
        String isSetDefault = request.getParameter("isDefault");
        String styleStr = request.getParameter("style");

        // Parse address ID
        int addressId;
        try {
            addressId = Integer.parseInt(addressIdStr);
        } catch (NumberFormatException e) {
            session.setAttribute("updateFail", "Invalid address ID");
            response.sendRedirect(request.getContextPath() + "/account?view=address");
            return;
        }

        // Check if the address belongs to the current user
        Address existingAddress = addressDAO.getAddressById(addressId, user.getUserId());
        if (existingAddress == null) {
            session.setAttribute("updateFail", "Address not found or you do not have permission to update it.");
            response.sendRedirect(request.getContextPath() + "/account?view=address");
            return;
        }

        // Validate input fields
        boolean hasError = false;
        if (receiverName == null || !receiverName.matches("^[\\p{L} .'-]+$")) {
            session.setAttribute("nameError", "Full name is invalid.");
            hasError = true;
        }
        if (receiverPhone == null || !receiverPhone.matches("^(0[2-9][0-9]{8,9})$")) {
            session.setAttribute("phoneError", "Phone number must be 10-11 digits and start with 0.");
            hasError = true;
        }
        if (street == null || !street.matches("^.{5,100}$")) {
            session.setAttribute("streetError", "Street must be 5-100 characters.");
            hasError = true;
        }
        if (city == null || city.trim().isEmpty()) {
            session.setAttribute("cityError", "City/Province is required.");
            hasError = true;
        }

        // Parse style parameter
        int style = 1;
        if (styleStr != null && !styleStr.trim().isEmpty()) {
            try {
                style = Integer.parseInt(styleStr);
                if (style < 1 || style > 3) {
                    style = 1; // fallback nếu ngoài range
                }
            } catch (NumberFormatException e) {
                style = 1;
            }
        }

        // If validation fails, redirect with error
        if (hasError) {
            session.setAttribute("updateFail", "Update address failed!");
            session.setAttribute("receiverName", receiverName);
            session.setAttribute("receiverPhone", receiverPhone);
            session.setAttribute("street", street);
            session.setAttribute("ward", ward);
            session.setAttribute("district", district);
            session.setAttribute("city", city);
            session.setAttribute("editAddressId", addressIdStr);
            session.setAttribute("isDefault", "true".equals(isSetDefault));
            session.setAttribute("style", styleStr);
            response.sendRedirect(request.getContextPath() + "/account?view=address");
            return;
        }

        // Build updated Address object
        Address address = new Address();
        address.setAddressId(addressId);
        address.setUser(user);
        address.setReceiverName(receiverName);
        address.setReceiverPhone(receiverPhone);
        address.setStreet(street);
        address.setWard(ward != null ? ward : null);
        address.setDistrict(district != null ? district : null);
        address.setCity(city);
        address.setType(enums.AddressStyle.fromStyle(style));
        address.setIsDefault("true".equals(isSetDefault));

        // Perform the update
        if (!addressDAO.updateAddress(address)) {
            session.setAttribute("updateFail", "Update address failed due to database error.");
            response.sendRedirect(request.getContextPath() + "/account?view=address");
            return;
        }

        session.setAttribute("updateSuccess", "Update address success!");
        response.sendRedirect(request.getContextPath() + "/account?view=address");
    }

    // Handle deleting an address
    private void deleteAddress(HttpServletRequest request, HttpServletResponse response, User user, AddressDAO addressDAO, HttpSession session)
            throws ServletException, IOException {
        String addressIdStr = request.getParameter("addressId");
        System.out.println(addressIdStr);
        int addressId;
        try {
            addressId = Integer.parseInt(addressIdStr);
        } catch (NumberFormatException e) {
            session.setAttribute("deleteFail", "Invalid address ID");
            response.sendRedirect(request.getContextPath() + "/account?view=address");
            return;
        }

        // Attempt to delete address, check for constraints
        if (!addressDAO.deleteAddress(addressId, user.getUserId())) {
            session.setAttribute("deleteFail", "Delete address fail! This address is used in existing orders and cannot be deleted.");
            response.sendRedirect(request.getContextPath() + "/account?view=address");
            return;
        }

        session.setAttribute("deleteSuccess", "Delete address success!");
        response.sendRedirect(request.getContextPath() + "/account?view=address");
    }

    // Handle setting a default address
    private void updateDefaultAddress(HttpServletRequest request, HttpServletResponse response, User user, AddressDAO addressDAO)
            throws ServletException, IOException {
        String page = request.getParameter("page");
        String productId = request.getParameter("productId");
        String quantity = request.getParameter("quantity");
        String voucherId = request.getParameter("voucherId");
        String paymentMethod = request.getParameter("paymentMethod");
        String shippingMethod = request.getParameter("shippingMethod");

        // Parse address ID
        String addressIdStr = request.getParameter("addressId");
        int addressId;
        try {
            addressId = Integer.parseInt(addressIdStr);
        } catch (NumberFormatException e) {
            setErrorAndForward(request, response, "Invalid address ID", page, productId, quantity, voucherId, paymentMethod, shippingMethod);
            return;
        }

        // Update default address
        if (!addressDAO.setDefaultAddress(user.getUserId(), addressId)) {
            setErrorAndForward(request, response, "Failed to update default address", page, productId, quantity, voucherId, paymentMethod, shippingMethod);
            return;
        }

        response.sendRedirect(buildRedirectUrl(request, page, productId, quantity, voucherId, paymentMethod, shippingMethod));
    }

    // Helper method to set error and forward to appropriate page
    private void setErrorAndForward(HttpServletRequest request, HttpServletResponse response, String errorMessage,
            String page, String productId, String quantity, String voucherId, String paymentMethod, String shippingMethod)
            throws ServletException, IOException {
        request.setAttribute("inputError", errorMessage);
        if ("orderConfirm".equals(page)) {
            request.setAttribute("productId", productId);
            request.setAttribute("quantity", quantity);
            request.setAttribute("voucherId", voucherId);
            request.setAttribute("paymentMethod", paymentMethod);
            request.setAttribute("shippingMethod", shippingMethod);
            request.getRequestDispatcher("/WEB-INF/order-confirmation.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("/WEB-INF/user/address-management.jsp").forward(request, response);
        }
    }

    // Helper method to build redirect URL after updating default address
    private String buildRedirectUrl(HttpServletRequest request, String page, String productId, String quantity,
            String voucherId, String paymentMethod, String shippingMethod) {
        if ("orderConfirm".equals(page)) {
            String redirectUrl = request.getContextPath() + "/order-confirm?productId=" + productId + "&quantity=" + quantity;
            if (voucherId != null && !voucherId.isEmpty() && !voucherId.equals("not")) {
                redirectUrl += "&voucherId=" + voucherId;
            }
            redirectUrl += "&paymentMethod=" + (paymentMethod != null ? paymentMethod : "1");
            redirectUrl += "&shippingMethod=" + (shippingMethod != null ? shippingMethod : "1");
            return redirectUrl;
        } else {
            return request.getContextPath() + "/address-management";
        }
    }
}
