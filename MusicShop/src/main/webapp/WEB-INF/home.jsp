<%-- 
    Document   : home
    Created on : May 17, 2025, 3:46:27 PM
    Author     : Nguyen Hoang Thai Vinh - CE190384
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Music Shop - Home</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/header.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/footer.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/home.css"/>
    </head>
    <body>
        <!-- Header -->
        <%@ include file="/WEB-INF/include/header.jsp" %>

        <!-- Intro Section -->
        <section class="intro-section position-relative text-center text-white" style="margin-top: 60px">
            <video class="intro-video" autoplay muted loop playsinline>
                <source src="${pageContext.request.contextPath}/video/intro.mp4" type="video/mp4">
                Your browser does not support the video tag.
            </video>
            <div class="video-blocker"></div>
        </section>

        <!-- Hero Section -->
        <section class="py-5 bg-light text-center">
            <div class="container">
                <h1 class="mb-3">Welcome to MusicShop</h1>
                <p class="lead mb-4">Discover high-quality musical instruments.</p>
                <a href="#guitar" class="btn btn-primary btn-lg">Shop Now</a>
            </div>
        </section>

        <!-- Categories Section -->
        <section class="container py-5">
            <h3 class="text-center mb-4">Explore Categories</h3>
            <div class="row g-3">
                <div class="col-4">
                    <a href="${pageContext.request.contextPath}/guitar?page=1" class="text-decoration-none">
                        <div class="card border-0 shadow-sm text-center position-relative category-card">
                            <img src="${pageContext.request.contextPath}/image/guitar.jpg" class="card-img-top" alt="Guitars" style="height: 200px; object-fit: cover;">
                            <div class="category-overlay">
                                <h5 class="category-title">Guitars</h5>
                            </div>
                        </div>
                    </a>
                </div>
                <div class="col-4">
                    <a href="${pageContext.request.contextPath}/violin?page=1" class="text-decoration-none">
                        <div class="card border-0 shadow-sm text-center position-relative category-card">
                            <img src="${pageContext.request.contextPath}/image/violin.webp" class="card-img-top" alt="Violins" style="height: 200px; object-fit: cover;">
                            <div class="category-overlay">
                                <h5 class="category-title">Violins</h5>
                            </div>
                        </div>
                    </a>
                </div>
                <div class="col-4">
                    <a href="${pageContext.request.contextPath}/piano?page=1" class="text-decoration-none">
                        <div class="card border-0 shadow-sm text-center position-relative category-card">
                            <img src="${pageContext.request.contextPath}/image/piano.jpg" class="card-img-top" alt="Pianos" style="height: 200px; object-fit: cover;">
                            <div class="category-overlay">
                                <h5 class="category-title">Pianos</h5>
                            </div>
                        </div>
                    </a>
                </div>
            </div>
        </section>

        <!-- Guitar -->
        <section id="guitar" class="container py-5">
            <h3 class="text-center mb-4">Guitar</h3>
            <div class="row g-3">
                <c:forEach var="product" items="${guitars}">
                    <div class="col-6 col-md-4">
                        <div class="card border-0 shadow-sm text-center">
                            <img src="${pageContext.request.contextPath}/upload/${product.imageUrl}" class="card-img-top" alt="${product.name}" style="height: 250px;">
                            <div class="card-body">
                                <h5 class="card-title">${product.name}</h5>
                                <p class="card-text text-muted">
                                    <fmt:formatNumber value="${product.price}" pattern="#,##0"/> VNĐ
                                </p>
                                <a href="${pageContext.request.contextPath}/product?id=${product.productId}" class="btn btn-outline-primary btn-sm">Buy Now</a>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </section>

        <!-- Violin -->
        <section class="container py-5">
            <h3 class="text-center mb-4">Piano</h3>
            <div class="row g-3">
                <c:forEach var="product" items="${pianos}">
                    <div class="col-6 col-md-4">
                        <div class="card border-0 shadow-sm text-center">
                            <img src="${pageContext.request.contextPath}/upload/${product.imageUrl}" class="card-img-top" alt="${product.name}" style="height: 250px;">
                            <div class="card-body">
                                <h5 class="card-title">${product.name}</h5>
                                <p class="card-text text-muted">
                                    <fmt:formatNumber value="${product.price}" pattern="#,##0"/> VNĐ
                                </p>
                                <a href="${pageContext.request.contextPath}/product?id=${product.productId}" class="btn btn-outline-primary btn-sm">Buy Now</a>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </section>

        <!-- Piano -->
        <section class="container py-5">
            <h3 class="text-center mb-4">Violin</h3>
            <div class="row g-3">
                <c:forEach var="product" items="${violins}">
                    <div class="col-6 col-md-4">
                        <div class="card border-0 shadow-sm text-center">
                            <img src="${pageContext.request.contextPath}/upload/${product.imageUrl}" class="card-img-top" alt="${product.name}" style="height: 250px;">
                            <div class="card-body">
                                <h5 class="card-title">${product.name}</h5>
                                <p class="card-text text-muted">
                                    <fmt:formatNumber value="${product.price}" pattern="#,##0"/> VNĐ
                                </p>
                                <a href="${pageContext.request.contextPath}/product?id=${product.productId}" class="btn btn-outline-primary btn-sm">Buy Now</a>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </section>

        <!-- Footer -->
        <%@ include file="/WEB-INF/include/footer.jsp" %>
        <%@include file="/WEB-INF/include/btn-to-top.jsp" %>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>