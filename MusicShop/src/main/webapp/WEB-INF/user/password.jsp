<%-- 
    Document   : passwordChange
    Created on : Jun 21, 2025, 2:50:52 PM
    Author     : Nguyen Hoang Thai Vinh - CE190384
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<div class="change-password-container">
    <div class="icon-lock">
        <i class="fa fa-lock"></i>
    </div>
    <h2 class="mb-4 text-center mt-4">CHANGE PASSWORD</h2>

    <form id="changePasswordForm" action="change-password" method="post">
        <div class="mb-3">
            <label for="oldPw" class="text">Current Password</label>
            <input type="password" class="form-control" id="oldPw" name="oldPw" placeholder="Enter current password" required>
            <c:if test="${not empty oldPwError}">
                <div class="text-danger mt-1">${oldPwError}</div>
            </c:if>
        </div>

        <div class="mb-3">
            <label for="newPw" class="text">New Password</label>
            <input type="password" class="form-control" id="newPw" name="newPw" placeholder="Enter new Password" required>
            <c:if test="${not empty newPwError}">
                <div class="text-danger mt-1">${newPwError}</div>
            </c:if>
        </div>

        <div class="mb-3">
            <label for="confirmPw" class="text">Confirm new password</label>
            <input type="password" class="form-control" id="confirmPw" name="confirmPw" placeholder="Re-enter new password" required>
            <c:if test="${not empty confirmPwError}">
                <div class="text-danger mt-1">${confirmPwError}</div>
            </c:if>
        </div>
        <button type="submit" class="btn btn-success w-100 mt-2">Change password</button>
    </form>
</div>