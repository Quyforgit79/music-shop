/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import db.JDBCUtil;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import model.Product;
import model.Review;
import model.User;

/**
 *
 * @author Nguyen Hoang Thai Vinh - CE190384
 */
public class ReviewDAO extends JDBCUtil {

    public Review getReviewById(int id) {
        return new Review();
    }

    public boolean createReview(int productId, int userId, int rating, String comment) {
        String sql = "INSERT Reviews (product_id, user_id, rating, comment) VALUES (?, ?, ?, ?)";
        Object[] params = {
            productId,
            userId,
            rating,
            comment
        };

        try {
            return execQuery(sql, params) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean createComment(int productId, int userId, int rating, String comment) {
        String sql = "INSERT Reviews (product_id, user_id, rating, comment) VALUES (?, ?, ?, ?)";
        Object[] params = {
            productId,
            userId,
            rating,
            comment
        };

        try {
            return execQuery(sql, params) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<Review> getReviewsByUserAndProduct(int userId, int productId) {
        String sql = "SELECT  \n"
                + "r.* , p.name, u.account \n"
                + "FROM  \n"
                + "Reviews r  \n"
                + "LEFT JOIN Products p ON p.product_id = r.product_id  \n"
                + "LEFT JOIN Users u ON u.user_id = r.user_id \n"
                + "WHERE r.user_id = ? AND r.product_id = ?";
        Object[] params = {userId, productId};
        List<Review> reviewList = new ArrayList<>();

        try ( ResultSet rs = execSelectQuery(sql, params)) {
            while (rs.next()) {
                Product p = new Product();
                p.setProductId(rs.getInt("product_id"));

                User u = new User();
                u.setUserId(rs.getInt("user_id"));
                u.setAccount(rs.getString("account"));

                Review r = new Review();
                r.setReviewId(rs.getInt("review_id"));
                r.setProduct(p);
                r.setComment(rs.getString("comment"));
                r.setRating(rs.getInt("rating"));

                // Lấy created_at
                Timestamp ts = rs.getTimestamp("created_at");
                if (ts != null) {
                    r.setCommentDate(ts.toLocalDateTime());
                }

                reviewList.add(r);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return reviewList;
    }

    public List<Review> getUserReview(int userId, int productId) {
        List<Review> list = new ArrayList<>();
        String sql = "SELECT r.* , u.account\n"
                + "FROM Reviews r\n"
                + "LEFT JOIN Users u ON u.user_id = r.user_id\n"
                + "where r.user_id = ? AND product_id = ?";
        Object[] params = {userId, productId};

        try ( ResultSet rs = execSelectQuery(sql, params)) {
            while (rs.next()) {
                Product p = new Product();
                p.setProductId(rs.getInt("product_id"));

                User u = new User();
                u.setUserId(rs.getInt("user_id"));
                u.setAccount(rs.getString("account"));

                Review r = new Review();
                r.setReviewId(rs.getInt("review_id"));
                r.setProduct(p);
                r.setComment(rs.getString("comment"));
                r.setRating(rs.getInt("rating"));
                r.setUser(u);

                // Lấy created_at
                Timestamp ts = rs.getTimestamp("created_at");
                if (ts != null) {
                    r.setCommentDate(ts.toLocalDateTime());
                }

                list.add(r);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<Review> getReviewsProductByProductId(int productId) {
        String sql = "SELECT r.*, u.account \n"
                + "FROM Reviews r \n"
                + "JOIN Users u ON r.user_id = u.user_id \n"
                + "WHERE r.product_id = ? ORDER BY r.review_id DESC";
        Object[] params = {productId};
        List<Review> reviewList = new ArrayList<>();

        try ( ResultSet rs = execSelectQuery(sql, params)) {
            while (rs.next()) {
                Product p = new Product();
                p.setProductId(rs.getInt("product_id"));

                User u = new User();
                u.setUserId(rs.getInt("user_id"));
                u.setAccount(rs.getString("account"));

                Review r = new Review();
                r.setReviewId(rs.getInt("review_id"));
                r.setProduct(p);
                r.setComment(rs.getString("comment"));
                r.setRating(rs.getInt("rating"));
                r.setUser(u);

                // Lấy created_at
                Timestamp ts = rs.getTimestamp("created_at");
                if (ts != null) {
                    r.setCommentDate(ts.toLocalDateTime());
                }

                reviewList.add(r);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return reviewList;
    }

    public Review getReviewByOrderId(int orderId) {
        String sql = "select * \n"
                + "from Reviews r\n"
                + "left join Orders o on o.user_id = r.user_id\n"
                + "where o.is_review = 1 and o.order_id = ?";
        Object[] params = {orderId};

        try ( ResultSet rs = execSelectQuery(sql, params)) {
            if (rs.next()) {
                Product p = new Product();
                p.setProductId(rs.getInt("product_id"));

                User u = new User();
                u.setUserId(rs.getInt("user_id"));
                u.setAccount(rs.getString("account"));

                Review r = new Review();
                r.setReviewId(rs.getInt("review_id"));
                r.setProduct(p);
                r.setComment(rs.getString("comment"));
                r.setRating(rs.getInt("rating"));
                r.setUser(u);

                // Lấy created_at
                Timestamp ts = rs.getTimestamp("created_at");
                if (ts != null) {
                    r.setCommentDate(ts.toLocalDateTime());
                }

                return r;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean deleteReview(int reviewId) {
        String sql = "DELETE FROM Reviews WHERE review_id = ?";
        Object[] params = {reviewId};

        try {
            return execQuery(sql, params) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean updateReview(int rating, String comment, int reviewId, int userId) {
        String sql = "UPDATE Reviews SET rating = ?, comment = ? WHERE review_id = ? AND user_id = ?";
        Object[] params = {
            rating, comment, reviewId, userId
        };

        try {
            return execQuery(sql, params) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
