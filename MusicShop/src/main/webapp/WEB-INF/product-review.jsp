<%-- 
    Document   : product-review
    Created on : Jul 18, 2025, 11:22:44 AM
    Author     : Nguyen Hoang Thai Vinh - CE190384
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Product Review</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/header.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/footer.css">
        <style>
            .star-rating {
                font-size: 2rem;
                cursor: pointer;
                color: #ccc;
            }
            .star-rating .fas {
                color: #f5c518;
            }
        </style>
    </head>
    <body>
        <%@ include file="/WEB-INF/include/header.jsp" %>
        <div class="container mt-5" style="padding: 50px 0;">
            <div class="card p-4 shadow-lg">
                <h3 class="mb-3"><i class="fa fa-star me-2"></i>Product Review</h3>

                <!-- Hiển thị thông báo lỗi -->
                <c:if test="${not empty errorMessage}">
                    <div class="alert alert-danger">
                        <i class="fa fa-exclamation-circle me-2"></i>${errorMessage}
                    </div>
                </c:if>

                <form action="${pageContext.request.contextPath}/review" method="post">
                    <input type="hidden" name="action" value="createReview">
                    <input type="hidden" name="orderId" value="${orderId}">
                    <input type="hidden" name="productId" value="${productId}">
                    <!-- Product info -->
                    <div class="mb-3">
                        <label class="form-label"><strong>Product name:</strong></label>
                        <div class="form-control-plaintext">${productName}</div>
                    </div>

                    <!-- Star rating -->
                    <div class="mb-3">
                        <label class="form-label"><strong>Select rating:</strong></label>
                        <div id="rating" class="star-rating">
                            <i class="far fa-star" data-value="1"></i>
                            <i class="far fa-star" data-value="2"></i>
                            <i class="far fa-star" data-value="3"></i>
                            <i class="far fa-star" data-value="4"></i>
                            <i class="far fa-star" data-value="5"></i>
                        </div>
                        <input type="hidden" name="rating" id="ratingValue" value="0">
                    </div>

                    <!-- Review text -->
                    <div class="mb-3">
                        <label for="reviewText" class="form-label"><strong>Comment:</strong></label>
                        <textarea class="form-control" id="reviewText" name="reviewText" rows="4" placeholder="Write your review..." required></textarea>
                    </div>

                    <!-- Submit -->
                    <button type="submit" class="btn btn-primary">
                        <i class="fa fa-paper-plane me-2"></i>Submit a review
                    </button>
                </form>
            </div>
        </div>
        <%@ include file="/WEB-INF/include/footer.jsp" %>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
        <script>
            const stars = document.querySelectorAll('#rating i');
            const ratingValue = document.getElementById('ratingValue');

            stars.forEach(star => {
                star.addEventListener('click', function () {
                    let value = this.getAttribute('data-value');
                    ratingValue.value = value;
                    stars.forEach(s => {
                        s.classList.remove('fas');
                        s.classList.add('far');
                    });
                    for (let i = 0; i < value; i++) {
                        stars[i].classList.remove('far');
                        stars[i].classList.add('fas');
                    }
                });
            });
        </script>
    </body>
</html>
