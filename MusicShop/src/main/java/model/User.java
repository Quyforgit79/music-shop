/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import enums.Gender;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 * @author Nguyen Hoang Thai Vinh - CE190384
 */
public class User {

    private int userId;
    private String fullName;
    private String email;
    private String password;
    private String phone;
    private Role role; // "customer" hoặc "admin"
    private String account;
    private LocalDateTime createDateTime;
    private String imageUrl;
    private Gender gender;
    private LocalDate birthdate;
    private boolean isActive;

    public User() {
    }

    // constructor to insert user to db
    public User(String fullName, String email, String phone, Role role, String account, LocalDateTime createDateTime, Gender gender, LocalDate birthdate, boolean isActive) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.account = account;
        this.createDateTime = createDateTime;
        this.birthdate = birthdate;
        this.gender = gender;
        this.isActive = isActive;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
    
    

    // constructor to get user from db
    public User(int userId, String fullName, String password, String phone, String address, Role role, String account, LocalDateTime createDateTime, String imageUrl, Gender gender, LocalDate birthdate) {
        this.userId = userId;
        this.fullName = fullName;
        this.password = password;
        this.phone = phone;
        this.role = role;
        this.account = account;
        this.createDateTime = createDateTime;
        this.imageUrl = imageUrl;
        this.birthdate = birthdate;
        this.gender = gender;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAccount() {
        return account;
    }

    public LocalDateTime getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(LocalDateTime createDateTime) {
        this.createDateTime = createDateTime;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    @Override
    public String toString() {
        return "User{" + "userId=" + userId + ", fullName=" + fullName + ", email=" + email + ", password=" + password + ", phone=" + phone + ", role=" + role + ", account=" + account + ", createDateTime=" + createDateTime + ", imageUrl=" + imageUrl + ", gender=" + gender + ", birthdate=" + birthdate + ", isActive=" + isActive + '}';
    }
}
