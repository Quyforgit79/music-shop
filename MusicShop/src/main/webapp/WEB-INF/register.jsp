<%-- 
    Document   : register
    Created on : May 17, 2025, 4:41:08 PM
    Author     : Nguyen Hoang Thai Vinh - CE190384
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Register - MusicShop</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
        <style>
            body {
                background: linear-gradient(135deg, #ffffff 0%, #1f2937 100%);
                min-height: 100vh;
                display: flex;
                align-items: center;
                justify-content: center;
                padding: 1rem;
            }
            .register-container {
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
            .register-img {
                background: linear-gradient(135deg, #1f2937 0%, #000000 100%);
                width: 40%;
                min-height: 500px;
                display: none;
                position: relative;
            }
            .register-img::before {
                content: '';
                position: absolute;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
                background: rgba(255, 255, 255, 0.1);
            }
            @media (min-width: 992px) {
                .register-img {
                    display: block;
                }
            }
            .register-box {
                flex: 1;
                padding: 2rem;
            }
            .register-logo {
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
            .register-logo i {
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
            @media (max-width: 576px) {
                .register-box {
                    padding: 1.5rem;
                }
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
        <div class="register-container">
            <div class="register-box">
                <div class="register-logo">
                    <i class="fa-solid fa-music"></i> MusicShop
                </div>

                <c:if test="${not empty registerError}">
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        ${registerError}
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                    </div>
                </c:if>

                <form action="${pageContext.request.contextPath}/register" method="post" id="registerForm" novalidate>
                    <div class="mb-3">
                        <label for="fullName" class="form-label">
                            Full Name
                            <small class="text-muted d-block">At least 3 characters, letters and spaces only</small>
                        </label>
                        <div class="input-group">
                            <span class="input-group-text"><i class="fa-solid fa-user"></i></span>
                            <input type="text" class="form-control ${not empty fullNameError ? 'is-invalid' : ''}" 
                                   id="fullName" name="full_name" value="${full_name}" 
                                   required aria-describedby="fullNameError fullNameClientError">
                        </div>
                        <c:if test="${not empty fullNameError}">
                            <div id="fullNameError" class="error-message">${fullNameError}</div>
                        </c:if>
                        <div id="fullNameClientError" class="client-error">Full name must be at least 3 characters and contain only letters and spaces.</div>
                    </div>

                    <div class="mb-3">
                        <label for="account" class="form-label">
                            Account
                            <small class="text-muted d-block">3-20 characters, letters, numbers, underscores</small>
                        </label>
                        <div class="input-group">
                            <span class="input-group-text"><i class="fa-regular fa-user"></i></span>
                            <input type="text" class="form-control ${not empty accountError ? 'is-invalid' : ''}" 
                                   id="account" name="account" value="${account}" 
                                   required aria-describedby="accountError accountClientError">
                        </div>
                        <c:if test="${not empty accountError}">
                            <div id="accountError" class="error-message">${accountError}</div>
                        </c:if>
                        <div id="accountClientError" class="client-error">Account must be 3-20 characters, containing only letters, numbers, or underscores.</div>
                    </div>

                    <div class="mb-3">
                        <label for="email" class="form-label">
                            Email
                            <small class="text-muted d-block">Example: name@example.com</small>
                        </label>
                        <div class="input-group">
                            <span class="input-group-text"><i class="fa-solid fa-envelope"></i></span>
                            <input type="email" class="form-control ${not empty emailError ? 'is-invalid' : ''}" 
                                   id="email" name="email" value="${email}" 
                                   required aria-describedby="emailError emailClientError">
                        </div>
                        <c:if test="${not empty emailError}">
                            <div id="emailError" class="error-message">${emailError}</div>
                        </c:if>
                        <div id="emailClientError" class="client-error">Please enter a valid email address.</div>
                    </div>

                    <div class="mb-3">
                        <label for="phone" class="form-label">
                            Phone
                            <small class="text-muted d-block">Starts with 0, consists of 10-11 digits</small>
                        </label>
                        <div class="input-group">
                            <span class="input-group-text"><i class="fa-solid fa-phone"></i></span>
                            <input type="tel" class="form-control ${not empty phoneError ? 'is-invalid' : ''}" 
                                   id="phone" name="phone" value="${phone}" 
                                   required aria-describedby="phoneError phoneClientError">
                        </div>
                        <c:if test="${not empty phoneError}">
                            <div id="phoneError" class="error-message">${phoneError}</div>
                        </c:if>
                        <div id="phoneClientError" class="client-error">Phone number must start with 0 and contain 10-11 digits.</div>
                    </div>

                    <div class="mb-3">
                        <label for="password" class="form-label">
                            Password
                            <small class="text-muted d-block">At least 8 characters, including uppercase, lowercase, numbers, special characters</small>
                        </label>
                        <div class="input-group">
                            <span class="input-group-text"><i class="fa-solid fa-lock"></i></span>
                            <input type="password" class="form-control ${not empty passwordError ? 'is-invalid' : ''}" 
                                   id="password" name="password" 
                                   required aria-describedby="passwordError passwordClientError">
                        </div>
                        <c:if test="${not empty passwordError}">
                            <div id="passwordError" class="error-message">${passwordError}</div>
                        </c:if>
                        <div id="passwordClientError" class="client-error">Password must be at least 8 characters, including uppercase, lowercase, numbers, and special characters.</div>
                    </div>

                    <button type="submit" class="btn btn-primary w-100">
                        <i class="fa-solid fa-user-plus me-2"></i>Register
                    </button>
                    <div class="text-center mt-3">
                        <span class="form-text">Already have an account? <a href="${pageContext.request.contextPath}/login" class="text-decoration-none">Login</a></span>
                    </div>
                </form>
            </div>
            <div class="register-img">
                <img src="${pageContext.request.contextPath}/image/register-image.jpg" 
                     alt="Register Image" class="img-fluid w-100 h-100" 
                     style="object-fit: cover; object-position: center;">
            </div>
        </div>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
