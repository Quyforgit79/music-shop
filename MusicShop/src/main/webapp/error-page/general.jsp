<%-- 
    Document   : general
    Created on : Jul 21, 2025, 11:03:15 PM
    Author     : Nguyen Hoang Thai Vinh - CE190384
--%>

<%@ page isErrorPage="true" contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Lỗi Hệ Thống</title>
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
                max-width: 600px;
            }
            .error-code {
                font-size: 6rem;
                font-weight: bold;
                color: #6f42c1;
            }
            .error-message {
                font-size: 1.25rem;
                color: #6c757d;
            }
            .error-details {
                margin-top: 20px;
                padding: 15px;
                background: #ffffff;
                border: 1px solid #ddd;
                border-radius: 5px;
                font-size: 0.95rem;
                color: #333;
            }
        </style>
    </head>
    <body>
        <div class="error-container">
            <h1 class="error-code">Oops!</h1>
            <p class="error-message">An error occurred in the system..</p>
            <div class="error-details">
                <strong>Error details:</strong>
                <p><%= (exception != null) ? exception.getMessage() : "Unknown cause." %></p>
            </div>
            <a href="${pageContext.request.contextPath}/index.jsp" class="btn btn-secondary mt-3">
                Back to home page
            </a>
        </div>
    </body>
</html>
