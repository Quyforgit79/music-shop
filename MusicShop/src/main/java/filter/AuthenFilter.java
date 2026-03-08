/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package filter;

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
import model.User;

/**
 *
 * @author Nguyen Hoang Thai Vinh - CE190384
 */
@WebFilter(filterName = "AuthenFilter", urlPatterns = {
    "/account", "/order", "/updateUser", "/order-confirm", "/address", "/cart",
    "/avatar", "/review"
})
public class AuthenFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        User user = (session != null) ? (User) session.getAttribute("user") : null;

        String loginURI = req.getContextPath() + "/login";

        boolean loggedIn = (user != null);
        boolean loginRequest = req.getRequestURI().equals(loginURI);

        if (!loggedIn && !loginRequest && !req.getRequestURI().contains("/public/")) {
            String currentUrl = req.getRequestURI()
                    + (req.getQueryString() != null ? "?" + req.getQueryString() : "");
            req.getSession(true).setAttribute("redirectAfterLogin", currentUrl);
            res.sendRedirect(loginURI);
        } else {
            chain.doFilter(req, res);
        }
    }
}
