<%-- 
    Document   : index
    Created on : May 20, 2025, 8:59:47 PM
    Author     : Nguyen Hoang Thai Vinh - CE190384
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    response.sendRedirect(request.getContextPath() + "/home");
//request.getRequestDispatcher("/WEB-INF/home.jsp").forward(request, response);
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hello World!</h1>
    </body>
</html>
