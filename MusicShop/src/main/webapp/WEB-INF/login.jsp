<%-- 
    Document   : login
    Created on : May 17, 2025, 4:41:02 PM
    Author     : Nguyen Hoang Thai Vinh - CE190384
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Login - MusicShop</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
        <style>
            body {
                background: linear-gradient(135deg, #000000 0%, #ffffff 100%);
                min-height: 100vh;
                display: flex;
                align-items: center;
                justify-content: center;
                padding: 1rem;
            }
            .login-container {
                background: #ffffff;
                border-radius: 1rem;
                box-shadow: 0 8px 32px rgba(0, 0, 0, 0.2);
                border: 1px solid #e5e7eb;
                padding: 0;
                max-width: 900px;
                width: 100%;
                display: flex;
                overflow: hidden;
                margin: 1rem 0;
            }
            .login-img {
                background: url('https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?auto=format&fit=crop&w=600&q=80') center/cover no-repeat;

                width: 40%;
                min-height: 440px;
                display: none;
                position: relative;
            }
            .login-img::before {
                content: '';
                position: absolute;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
                background: rgba(255, 255, 255, 0.1);
            }
            @media (min-width: 992px) {
                .login-img {
                    display: block;
                }
            }
            @media (max-width: 991.98px) {
                .login-container {
                    flex-direction: column;
                    max-width: 98vw;
                }
                .login-img {
                    display: none;
                }
            }
            .login-box {
                flex: 1;
                padding: 2rem;
                display: flex;
                flex-direction: column;
                justify-content: center;
                min-width: 0;
            }
            @media (max-width: 575.98px) {
                .login-box {
                    padding: 1.5rem;
                }
            }
            .login-logo {
                font-size: 2.25rem;
                color: #111827;
                font-weight: 700;
                margin-bottom: 1.5rem;
                text-align: center;
                display: flex;
                align-items: center;
                justify-content: center;
                gap: 0.5rem;
            }
            .login-logo i {
                color: #000000;
            }
            .form-label {
                font-weight: 600;
                color: #111827;
            }
            .input-group-text {
                background: #e5e7eb;
                border: none;
                color: #000000;
            }
            .form-control {
                border: 1px solid #000000;
                border-radius: 0.5rem;
            }
            .form-control:focus {
                border-color: #1f2937;
                box-shadow: 0 0 0 0.25rem rgba(31, 41, 55, 0.2);
            }
            .btn-primary {
                background: #1f2937;
                border: none;
                border-radius: 0.5rem;
                padding: 0.75rem;
                font-weight: 600;
            }
            .btn-primary:hover {
                background: #4b5563;
            }
            .form-check-label {
                color: #111827;
            }
            .error-message, .client-error {
                color: #dc2626;
                font-size: 0.875rem;
                margin-top: 0.25rem;
            }
            .client-error {
                display: none;
            }
            .is-invalid ~ .client-error {
                display: block;
            }
            .form-text a {
                color: #1f2937;
                font-weight: 500;
            }
            .form-text a:hover {
                color: #4b5563;
            }
            @media (max-width: 575.98px) {
                .form-control, .input-group-text {
                    font-size: 0.9rem;
                }
                .btn-primary {
                    padding: 0.6rem;
                }
            }
        </style>
    </head>
    <body>
        <div class="login-container">
            <div class="login-box">
                <div class="login-logo">
                    <i class="fa-solid fa-music"></i> MusicShop
                </div>
                <!-- Hiển thị thông báo lỗi -->
                <c:if test="${not empty error}">
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        ${error}
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                    </div>
                </c:if>

                <form id="loginForm" action="${pageContext.request.contextPath}/login" method="post" novalidate>
                    <div class="mb-3">
                        <label for="username" class="form-label">User name</label>
                        <div class="input-group">
                            <span class="input-group-text"><i class="fa-solid fa-user"></i></span>
                            <input type="text" class="form-control" id="username" name="info" 
                                   value="${not empty info ? info : savedUserAccount}" placeholder="Enter user name" 
                                   required aria-describedby="usernameClientError">
                        </div>
                        <div id="usernameClientError" class="client-error">Username must be 3-20 characters, containing only letters, numbers, or underscores.</div>
                    </div>
                    <div class="mb-3">
                        <label for="password" class="form-label">Password</label>
                        <div class="input-group">
                            <span class="input-group-text"><i class="fa-solid fa-lock"></i></span>
                            <input type="password" class="form-control" id="password" name="password" 
                                   placeholder="Enter password" required aria-describedby="passwordClientError">
                        </div>
                        <div id="passwordClientError" class="client-error">Password must be at least 8 characters, including uppercase, lowercase, numbers, and special characters.</div>
                    </div>
                    <div class="mb-3 form-check">
                        <input type="checkbox" name="rememberMe" class="form-check-input" id="rememberMe">
                        <label class="form-check-label" for="rememberMe">Remember me</label>
                    </div>
                    <button type="submit" class="btn btn-primary w-100">
                        <i class="fa-solid fa-arrow-right-to-bracket me-2"></i>Login
                    </button>
                    <div class="text-center mt-3">
                        <span class="form-text">No account yet? <a href="${pageContext.request.contextPath}/register" class="text-decoration-none">Register</a></span>
                    </div>
                </form>
            </div>
            <div class="login-img"></div>
        </div>
    </body>
</html>