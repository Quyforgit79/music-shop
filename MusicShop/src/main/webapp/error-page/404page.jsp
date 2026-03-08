<%-- 
    Document   : 404page
    Created on : Jul 8, 2025, 10:56:43 AM
    Author     : Nguyen Hoang Thai Vinh - CE190384
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>404 - Page Not Found</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #f8f9fa;
            height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        .error-container {
            text-align: center;
        }
        .error-code {
            font-size: 8rem;
            font-weight: bold;
            color: #dc3545;
        }
        .error-message {
            font-size: 1.5rem;
            color: #6c757d;
        }
    </style>
</head>
<body>
<div class="error-container">
    <h1 class="error-code">404</h1>
    <p class="error-message">The page you are looking for does not exist.</p>
    <a href="${pageContext.request.contextPath}/home" class="btn btn-primary mt-3">
        Back to home page
    </a>
</div>
</body>
</html>