/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

/**
 *
 * @author Nguyen Hoang Thai Vinh - CE190384
 */
@WebServlet(name = "ImageServlet", urlPatterns = {"/upload/*"})
public class ImageServlet extends HttpServlet {

    private static final String UPLOAD_LOCATION = "D:\\Java_Web\\MusicShop\\upload";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy đường dẫn tương đối từ URL (ví dụ: /productImage/123456789_uuid.jpg)
        String filePath = request.getPathInfo();
        if (filePath == null || filePath.isEmpty()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // Tạo đường dẫn đầy đủ tới file
        File file = new File(UPLOAD_LOCATION, filePath.substring(1)); // Bỏ dấu / đầu tiên

        if (file.exists()) {
            // Xác định Content-Type dựa trên phần mở rộng của file
            String contentType = getServletContext().getMimeType(file.getName());
            response.setContentType(contentType != null ? contentType : "image/jpeg");

            // Đọc và gửi file ảnh
            try ( FileInputStream fis = new FileInputStream(file);  OutputStream os = response.getOutputStream()) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
