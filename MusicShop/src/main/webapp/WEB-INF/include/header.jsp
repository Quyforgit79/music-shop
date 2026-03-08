<%-- 
    Document   : header
    Created on : May 21, 2025, 10:38:27 AM
    Author     : Nguyen Hoang Thai Vinh - CE190384
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!-- Navbar -->
<nav class="navbar navbar-expand-lg bg-dark">
    <div class="container">
        <a class="navbar-brand fw-bold" href="${pageContext.request.contextPath}/home">
            <i class="fa-solid fa-music me-1"></i> MusicShop
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav ms-auto align-items-lg-center">
                <li class="nav-item">
                    <a class="nav-link fw-semibold ${view == 'home' ? 'active' : ''}" href="${pageContext.request.contextPath}/home">Home</a>
                </li>
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle fw-semibold" href="#" id="productDropdown" role="button"
                       data-bs-toggle="dropdown" aria-expanded="false">
                        Products
                    </a>
                    <ul class="dropdown-menu" aria-labelledby="productDropdown">
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/guitar?page=1">Guitar</a></li>
                        <li><hr class="dropdown-divider"></li>
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/piano?page=1">Piano & Keyboard</a></li>
                        <li><hr class="dropdown-divider"></li>
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/violin?page=1">Violin</a></li>
                    </ul>
                </li>
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle fw-semibold" href="#" id="accountDropdown" role="button"
                       data-bs-toggle="dropdown" aria-expanded="false">
                        <i class="fa-solid fa-user me-1"></i> Account
                    </a>
                    <ul class="dropdown-menu" aria-labelledby="accountDropdown">
                        <c:choose>
                            <c:when test="${not empty user}">
                                <li><a class="dropdown-item" href="${pageContext.request.contextPath}/account?view=info">Profile</a></li>
                                <li><a class="dropdown-item" href="${pageContext.request.contextPath}/account?view=history">History</a></li>
                                    <c:if test="${not empty admin}">
                                    <li><a class="dropdown-item" href="http://localhost:8080/Admin/login">Admin</a></li>
                                    </c:if>
                                <li><hr class="dropdown-divider"></li>
                                <li><a class="dropdown-item" href="${pageContext.request.contextPath}/logout">Logout</a></li>
                            </c:when>
                            <c:when test="${user.role.id == 1}">
                                <li><a class="dropdown-item" href="${pageContext.request.contextPath}/admin-dashboard">Admin Panel</a></li>
                                <li><hr class="dropdown-divider"></li>
                            </c:when>
                            <c:otherwise>
                                <li><a class="dropdown-item" href="${pageContext.request.contextPath}/login">Login</a></li>
                                <li><a class="dropdown-item" href="${pageContext.request.contextPath}/register">Register</a></li>
                            </c:otherwise> 
                        </c:choose>
                    </ul>
                </li>
                <li class="nav-item">
                    <a class="nav-link fw-semibold ${view == 'cart' ? 'active' : ''}" href="${pageContext.request.contextPath}/account?view=cart">
                        <i class="fa-solid fa-cart-shopping me-1"></i> Cart
                    </a>
                </li>
            </ul>
        </div>
    </div>
</nav>

<script>
    let lastScrollTop = 0;
    const navbar = document.querySelector('.navbar');

    window.addEventListener('scroll', function () {
        const currentScroll = window.pageYOffset || document.documentElement.scrollTop;

        if (currentScroll > lastScrollTop && currentScroll > 100) {
            // Scroll down and past 100px
            navbar.style.top = "-100px";
        } else {
            // Scroll up or near top
            navbar.style.top = "0";
        }

        lastScrollTop = currentScroll <= 0 ? 0 : currentScroll;
    });
</script>