<%-- 
    Document   : profile
    Created on : May 30, 2025, 3:43:11 PM
    Author     : Nguyen Hoang Thai Vinh - CE190384
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>User Profile</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/choices.js/public/assets/styles/choices.min.css">
    <!-- FontAwesome CSS -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
    <!-- Custom CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/footer.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/user.profile.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/user.order.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/user.address.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/user.cart.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/user.changePassword.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/user.info.css"/>
</head>
<body>
    <!-- Header -->
    <%@include file="/WEB-INF/include/header.jsp" %>

    <!-- Content -->
    <div class="container py-4" style="margin-top: 100px">
        <div class="d-flex justify-content-between">
            <div class="col-md-4 col-lg-3"> 
                <div class="sidebar">
                    <div class="text-center">
                        <c:choose>
                            <c:when test="${not empty user.imageUrl}">
                                <img src="${pageContext.request.contextPath}${user.imageUrl}?t=${System.currentTimeMillis()}"
                                     alt="Profile Picture" class="profile-img rounded-circle">
                            </c:when>
                            <c:otherwise>
                                <img src="${pageContext.request.contextPath}/assets/images/default-avatar.png"
                                     alt="Default Avatar" class="profile-img rounded-circle">
                            </c:otherwise>
                        </c:choose>
                        <h4>${user.fullName}</h4>
                    </div>
                    <hr>
                    <ul>
                        <li><a href="${pageContext.request.contextPath}/account?view=info" 
                               class="${view == 'info' or empty view ? 'active' : ''}" onclick="activeBtn(event, this)">
                                <i class="fas fa-user-circle"></i> Profile</a></li>
                        <li><a href="${pageContext.request.contextPath}/account?view=address" 
                               class="${view == 'address' ? 'active' : ''}" onclick="activeBtn(event, this)">
                                <i class="fas fa-map-marker-alt"></i> Address</a></li>
                        <li><a href="${pageContext.request.contextPath}/account?view=order" 
                               class="${view == 'order' ? 'active' : ''}" onclick="activeBtn(event, this)">
                                <i class="fas fa-box"></i> Order</a></li>
                        <li><a href="${pageContext.request.contextPath}/account?view=cart" 
                               class="${view == 'cart' ? 'active' : ''}" onclick="activeBtn(event, this)">
                                <i class="fas fa-shopping-cart"></i> Cart</a></li>
                        <li><a href="${pageContext.request.contextPath}/account?view=history" 
                               class="${view == 'history' ? 'active' : ''}" onclick="activeBtn(event, this)">
                                <i class="fas fa-history"></i> History</a></li>
                        <li><a href="${pageContext.request.contextPath}/account?view=password" 
                               class="${view == 'password' ? 'active' : ''}" onclick="activeBtn(event, this)">
                                <i class="fas fa-lock"></i> Change Password</a></li>
                    </ul>
                </div>
            </div>
            <div class="col-md-4 col-md-8">
                <c:choose>
                    <c:when test="${view == 'info' or empty view}">
                        <jsp:include page="/WEB-INF/user/info.jsp" />
                    </c:when>
                    <c:when test="${view == 'address'}">
                        <jsp:include page="/WEB-INF/user/address.jsp" />
                    </c:when>
                    <c:when test="${view == 'cart'}">
                        <jsp:include page="/WEB-INF/user/cart.jsp" />
                    </c:when>
                    <c:when test="${view == 'order'}">
                        <jsp:include page="/WEB-INF/user/order.jsp" />
                    </c:when>
                    <c:when test="${view == 'history'}">
                        <jsp:include page="/WEB-INF/user/history.jsp" />
                    </c:when>
                    <c:when test="${view == 'password'}">
                        <jsp:include page="/WEB-INF/user/password.jsp" />
                    </c:when>
                </c:choose>
            </div>
        </div>
    </div>

    <!-- Footer -->
    <%@include file="/WEB-INF/include/footer.jsp" %>
    <!-- Toast -->
    <%@include file="/WEB-INF/include/profile-toast.jsp" %>

    <!-- Scripts -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/choices.js/public/assets/scripts/choices.min.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/info.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/address.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/cart.js"></script>
</html>