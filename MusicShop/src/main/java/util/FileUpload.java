/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import jakarta.servlet.http.Part;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 *
 * @author Vo Chi Trong - CE191062
 */
public class FileUpload {

    public static final String UPLOAD_LOCATION = "D:\\Java_Web\\MusicShop\\upload";
    public static final String PRODUCT_IMAGE_SUBFOLDER = "productImage";

    public static String uploadPicture(Part img) throws FileNotFoundException, IOException {
        // Tạo thư mục chính nếu chưa tồn tại
        File uploadFolder = new File(UPLOAD_LOCATION);
        if (!uploadFolder.exists()) {
            uploadFolder.mkdirs();
        }

        // Tạo thư mục con productImage nếu chưa tồn tại
        File productImageFolder = new File(uploadFolder, PRODUCT_IMAGE_SUBFOLDER);
        if (!productImageFolder.exists()) {
            productImageFolder.mkdirs();
        }

        // Đổi tên file để tránh trùng
        String originalFileName = img.getSubmittedFileName();
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String uniqueFileName = System.currentTimeMillis() + "_" + UUID.randomUUID() + extension;

        // Lưu file vào thư mục productImage
        File fileOutput = new File(productImageFolder, uniqueFileName);
        try ( OutputStream os = new FileOutputStream(fileOutput);  InputStream is = img.getInputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) > 0) {
                os.write(buffer, 0, bytesRead);
            }
        }

        // Trả về đường dẫn tương đối của file (bao gồm thư mục con)
        return PRODUCT_IMAGE_SUBFOLDER + File.separator + uniqueFileName;
    }

    public static boolean deletePicture(String fileName) {
        if (fileName == null || fileName.isBlank()) {
            System.out.println("Không có tên file để xóa.");
            return false;
        }
        File file = new File(UPLOAD_LOCATION, fileName);
        if (file.exists()) {
            return file.delete(); // Trả về true nếu xóa thành công
        }
        return false; // Trả về false nếu file không tồn tại
    }
}
