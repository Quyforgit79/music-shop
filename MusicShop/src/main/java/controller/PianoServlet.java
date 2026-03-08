/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.ProductDAO;
import dao.SubcategoryDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.CategoryType;
import model.PageSize;
import model.Product;
import model.SubCategory;

/**
 *
 * @author Nguyen Hoang Thai Vinh - CE190384
 */
@WebServlet(name = "PianoServlet", urlPatterns = {"/piano"})
public class PianoServlet extends HttpServlet {

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
            out.println("<title>Servlet PianoServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet PianoServlet at " + request.getContextPath() + "</h1>");
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
        int page = 1;
        int pageSize = PageSize.PAGE_SIZE;
        int categoryId = CategoryType.CATEGORY_PIANO;

        // Lấy tham số page
        String pageParam = request.getParameter("page");
        try {
            page = Integer.parseInt(pageParam);
        } catch (Exception e) {
            page = 1;
        }

        ProductDAO dao = new ProductDAO();
        SubcategoryDAO subcategoryDAO = new SubcategoryDAO();
        List<SubCategory> sybList = subcategoryDAO.getAllSubcategoryByCategorys(2);

        // Lấy tham số lọc và tìm kiếm
        String[] subcateId = request.getParameterValues("subcateId");
        String priceStr = request.getParameter("price");
        boolean sale = "true".equals(request.getParameter("sale"));
        boolean bestSeller = "true".equals(request.getParameter("bestSeller"));

        List<Product> products;
        int totalProducts;

        // Áp dụng lọc nếu có tham số
        if (subcateId != null || priceStr != null || sale || bestSeller) {
            totalProducts = dao.countFilteredProducts(categoryId, subcateId, priceStr, sale, bestSeller);
            products = dao.filterProductsByPage(categoryId, subcateId, priceStr, sale, bestSeller, (page - 1) * pageSize, pageSize);
        } else {
            totalProducts = dao.countProductsByCategory(categoryId);
            products = dao.getProductsByCategoryAndPage(categoryId, (page - 1) * pageSize, pageSize);
        }

        int totalPages = (int) Math.ceil((double) totalProducts / pageSize);
        if (totalPages == 0) {
            totalPages = 1;
        }
        if (page < 1) {
            page = 1;
        }
        if (page > totalPages) {
            page = totalPages;
        }

        // Gửi dữ liệu tới JSP
        request.setAttribute("pianos", products);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("categoryId", categoryId);
        request.setAttribute("selectedTypes", subcateId);
        request.setAttribute("selectedPrice", priceStr);
        request.setAttribute("isSale", sale);
        request.setAttribute("isBestSeller", bestSeller);
        request.setAttribute("sybList", sybList);

        request.getRequestDispatcher("/WEB-INF/collections/piano.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int page = 1;
        int pageSize = PageSize.PAGE_SIZE;
        int categoryId = CategoryType.CATEGORY_PIANO;

        // Lấy tham số page
        String pageParam = request.getParameter("page");
        try {
            page = Integer.parseInt(pageParam);
        } catch (Exception e) {
            page = 1;
        }

        // Lấy tham số tìm kiếm
        String searchQuery = request.getParameter("search");

        ProductDAO dao = new ProductDAO();
        SubcategoryDAO subcategoryDAO = new SubcategoryDAO();
        List<SubCategory> sybList = subcategoryDAO.getAllSubcategoryByCategorys(categoryId);

        List<Product> products;
        int totalProducts;

        // Thực hiện tìm kiếm
        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            totalProducts = dao.countSearchProducts(searchQuery, categoryId);
            products = dao.searchProducts(searchQuery, categoryId, (page - 1) * pageSize, pageSize);
        } else {
            totalProducts = dao.countProductsByCategory(categoryId);
            products = dao.getProductsByCategoryAndPage(categoryId, (page - 1) * pageSize, pageSize);
        }

        int totalPages = (int) Math.ceil((double) totalProducts / pageSize);
        if (totalPages == 0) {
            totalPages = 1;
        }
        if (page < 1) {
            page = 1;
        }
        if (page > totalPages) {
            page = totalPages;
        }

        // Gửi dữ liệu tới JSP
        request.setAttribute("pianos", products);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("categoryId", categoryId);
        request.setAttribute("searchQuery", searchQuery);
        request.setAttribute("sybList", sybList);

        request.getRequestDispatcher("/WEB-INF/collections/piano.jsp").forward(request, response);
    }

}
