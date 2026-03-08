/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 *
 * @author Nguyen Hoang Thai Vinh - CE190384
 */
public class Review {

    private int reviewId;
    private Product product;
    private User user;
    private int rating;
    private String comment;
    private LocalDateTime commentDate;

    public Review() {
    }

    public Review(int reviewId, Product product, User user, int rating, String comment, LocalDateTime commentDate) {
        this.reviewId = reviewId;
        this.product = product;
        this.user = user;
        this.rating = rating;
        this.comment = comment;
        this.commentDate = commentDate;
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(LocalDateTime commentDate) {
        this.commentDate = commentDate;
    }

    public Date getReviewDateAsDate() {
        return commentDate != null ? Date.from(commentDate.atZone(ZoneId.systemDefault()).toInstant()) : null;
    }

    @Override
    public String toString() {
        return "Review{" + "reviewId=" + reviewId + ", product=" + product + ", user=" + user + ", rating=" + rating + ", comment=" + comment + ", commentDate=" + commentDate + '}';
    }
}
