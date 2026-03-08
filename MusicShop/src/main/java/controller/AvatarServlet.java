/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.UserDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.User;

/**
 *
 * @author Nguyen Hoang Thai Vinh - CE190384
 */
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 5, // 5MB
        maxRequestSize = 1024 * 1024 * 10 // 10MB
)
@WebServlet(name = "AvatarServlet", urlPatterns = {"/avatar", "/avatars/*"})
public class AvatarServlet extends HttpServlet {

    private static final String UPLOAD_PATH = "D:/Java_Web/MusicShop/upload/avatar";  // Directory for avatar uploads
    private static final Logger LOGGER = Logger.getLogger(AvatarServlet.class.getName());
    private static final String AVATAR_URL_PREFIX = "/avatars"; // URL prefix for serving avatars
    private static final String[] ALLOWED_EXTENSIONS = {".jpg", ".jpeg", ".png", ".gif"};  // Valid image extensions

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
        // Get the requested file path from the URL
        String requestedFile = request.getPathInfo(); // "/abc.jpg"

        // Validate the file path
        if (requestedFile == null || requestedFile.equals("/")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "File name missing.");
            return;
        }

        // Create a File object for the requested file
        File file = new File(UPLOAD_PATH, requestedFile);

        // If the file doesn't exist or is a directory, return 404
        if (!file.exists() || file.isDirectory()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "File not found.");
            return;
        }

        // Determine the MIME type of the file
        String mimeType = Files.probeContentType(file.toPath());
        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }

        // Set response headers
        response.setContentType(mimeType);
        response.setContentLengthLong(file.length());

        // Stream the file content to the response output
        try ( OutputStream out = response.getOutputStream();  FileInputStream in = new FileInputStream(file)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
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

        // Ensure the user is logged in
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");
        Part filePart = request.getPart("avatar");
        String updateFail = null;

        // Check if the file part is provided
        if (filePart == null || filePart.getSize() == 0) {
            updateFail = "Please select an image file.";
            session.setAttribute("updateAvatarFail", updateFail);
            response.sendRedirect(request.getContextPath() + "/account?view=info");
            return;
        }

        // Extract the original filename and its extension
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        String fileExt = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();

        // Validate file extension
        boolean validExtension = false;
        for (String ext : ALLOWED_EXTENSIONS) {
            if (fileExt.equals(ext)) {
                validExtension = true;
                break;
            }
        }

        if (!validExtension) {
            updateFail = "Invalid image format. Only JPG, JPEG, PNG, GIF allowed.";
            session.setAttribute("updateAvatarFail", updateFail);
            response.sendRedirect(request.getContextPath() + "/account?view=info");
            return;
        }

        // Generate unique filename
        String newFileName = UUID.randomUUID().toString() + fileExt;
        File uploadDir = new File(UPLOAD_PATH);

        // Create upload directory if it doesn't exist
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        String filePath = UPLOAD_PATH + File.separator + newFileName;
        String avatarUrl = AVATAR_URL_PREFIX + "/" + newFileName;

        try {
            // Save the new avatar file
            filePart.write(filePath);
            LOGGER.log(Level.INFO, "File saved to: {0}", filePath);
            LOGGER.log(Level.INFO, "Avatar URL: {0}", avatarUrl);

            // Delete old avatar if it exists and is not the default
            if (user.getImageUrl() != null && !user.getImageUrl().contains("default")) {
                String oldFileName = user.getImageUrl().substring(user.getImageUrl().lastIndexOf("/") + 1);
                String oldPath = UPLOAD_PATH + File.separator + oldFileName;
                Files.deleteIfExists(Paths.get(oldPath));
                LOGGER.log(Level.INFO, "Deleted old avatar: {0}", oldPath);
            }

            // Update avatar URL in the database
            UserDAO dao = new UserDAO();
            boolean updateSuccess = dao.uploadAvatar(avatarUrl, user.getUserId());
            if (!updateSuccess) {
                updateFail = "Failed to update avatar in the database.";
                session.setAttribute("updateAvatarFail", updateFail);
                Files.deleteIfExists(Paths.get(filePath));
                response.sendRedirect(request.getContextPath() + "/account?view=info");
                return;
            }

            // Update user object with new avatar URL
            user.setImageUrl(avatarUrl);
            session.setAttribute("user", user);
            session.setAttribute("updateSuccess", "Avatar uploaded successfully!");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error processing avatar upload", e);
            updateFail = "Failed to upload avatar. Please try again.";
            session.setAttribute("updateAvatarFail", updateFail);

            Files.deleteIfExists(Paths.get(filePath));
        }

        response.sendRedirect(request.getContextPath() + "/account?view=info");
    }
}
