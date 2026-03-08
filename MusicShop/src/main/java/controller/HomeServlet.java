/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.ProductDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.Product;

/**
 *
 * @author Nguyen Hoang Thai Vinh - CE190384
 */
@WebServlet(name = "HomeServlet", urlPatterns = {"/home"})
public class HomeServlet extends HttpServlet {

    private final int GUITAR_CATE = 1;
    private final int PIANO_CATE = 2;
    private final int VIOLIN_CATE = 3;

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
        // Initialize ProductDAO
        ProductDAO productDAO = new ProductDAO();

        List<Product> guitars = productDAO.getRandomProducts(GUITAR_CATE);
        List<Product> pianos = productDAO.getRandomProducts(PIANO_CATE);
        List<Product> violins = productDAO.getRandomProducts(VIOLIN_CATE);
        // Check if random products are already stored in ServletContext
        request.setAttribute("guitars", guitars);
        request.setAttribute("pianos", pianos);
        request.setAttribute("violins", violins);

        // Forward to home.jsp
        request.getRequestDispatcher("/WEB-INF/home.jsp").forward(request, response);
    }

}
