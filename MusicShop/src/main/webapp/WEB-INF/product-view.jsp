<%-- 
    Document   : productView
    Created on : Jun 17, 2025, 5:41:46 PM
    Author     : Nguyen Hoang Thai Vinh - CE190384
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Product View - MusicShop</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/product.view.css">
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
            .user-reviews, .other-reviews {
                margin-bottom: 2rem;
            }

            .product-rating i {
                color: gold;
                margin-right: 2px;
                font-size: 18px;
            }
        </style>
    </head>
    <body>
        <%@ include file="/WEB-INF/include/header.jsp" %>

        <div class="container py-5">
            <c:choose>
                <c:when test="${not empty product}">
                    <div class="product-detail">
                        <div class="row">
                            <!-- Product Images -->
                            <div class="col-md-6 product-images">
                                <c:if test="${product.soldQuantity ge 10}">
                                    <span class="badge">Favourite</span>
                                </c:if>
                                <div style="position:relative;">
                                    <!-- Nút chuyển ảnh trái -->
                                    <button id="prevImageBtn" type="button" class="btn btn-light btn-sm"
                                            style="position:absolute;top:50%;left:10px;transform:translateY(-50%);z-index:2;border-radius:50%;box-shadow:0 2px 8px rgba(0,0,0,0.08);">
                                        <i class="fa fa-chevron-left"></i>
                                    </button>
                                    <!-- Ảnh chính -->
                                    <img id="mainProductImage" src="${pageContext.request.contextPath}/upload/${product.imageUrl}" alt="Guitar Acoustic" class="product-main-image" style="display:block;margin:auto;">
                                    <!-- Nút chuyển ảnh phải -->
                                    <button id="nextImageBtn" type="button" class="btn btn-light btn-sm"
                                            style="position:absolute;top:50%;right:10px;transform:translateY(-50%);z-index:2;border-radius:50%;box-shadow:0 2px 8px rgba(0,0,0,0.08);">
                                        <i class="fa fa-chevron-right"></i>
                                    </button>
                                </div>
                                <div class="product-thumbnail-images mt-2" style="border-top:1px dashed #ccc;padding-top:10px;">
                                    <div class="mb-1 text-center" style="font-size:0.95rem;color:#888;">
                                        <i class="fa fa-hand-pointer"></i> Click to view photo
                                    </div>
                                    <c:forEach var="image" items="${productImages}">
                                        <img src="${pageContext.request.contextPath}/upload/${image.imageUrl}" alt="${product.name}">
                                    </c:forEach>
                                </div>
                            </div>

                            <!-- Product Info -->
                            <div class="col-md-6 product-info">
                                <h1>${product.name}</h1>
                                <c:set var="rating" value="${avgRating}" />

                                <div class="product-rating">
                                    <c:forEach var="i" begin="1" end="5">
                                        <c:choose>
                                            <c:when test="${i <= rating}">
                                                <i class="fas fa-star"></i> <!-- sao đầy -->
                                            </c:when>
                                            <c:when test="${i - rating < 1 && i > rating}">
                                                <i class="fas fa-star-half-alt"></i> <!-- nửa sao -->
                                            </c:when>
                                            <c:otherwise>
                                                <i class="far fa-star"></i> <!-- sao rỗng -->
                                            </c:otherwise>
                                        </c:choose>
                                    </c:forEach>
                                    <span>(${avgRating})</span>
                                </div>
                                <div class="price">
                                    Price: 
                                    <strong>
                                        <fmt:formatNumber value="${product.price}" type="number" groupingUsed="true"/>đ
                                    </strong>
                                </div>
                                <p class="text-muted">Quantity in stock: ${product.stockQuantity}</p>
                                <p>
                                    Brand: <strong>${product.brand.name}</strong><br>
                                    Category: <strong>${not empty product.category.name ? product.category.name : 'Unknown'}</strong><br>
                                    Material: <strong>${not empty product.material ? product.material : 'Unknown'}</strong><br>
                                    Year of manufacture: <strong>${product.yearOfManufacture != 0 ? product.yearOfManufacture : 'Unknown'}</strong><br>
                                    Made in: <strong>${not empty product.madeIn ? product.madeIn : 'Unknown'}</strong>
                                </p>

                                <!-- Quantity -->
                                <div class="quantity d-flex align-items-center">
                                    <label for="userQuantityInput">Quantity: </label>
                                    <button type="button" class="btn btn-outline-secondary" onclick="changeQuantity(-1)">−</button>
                                    <input type="number" id="userQuantityInput" name="quantity"
                                           class="form-control text-center mx-2"
                                           value="1" min="1" max="${product.stockQuantity}" style="width: 60px;">
                                    <button type="button" class="btn btn-outline-secondary" onclick="changeQuantity(1)">+</button>
                                </div>

                                <!-- Actions -->
                                <div class="actions mt-3">
                                    <div class="d-flex">
                                        <c:choose>
                                            <c:when test="${product.stockQuantity > 0}">
                                                <!-- Add to Cart -->
                                                <form action="${pageContext.request.contextPath}/cart" method="get">
                                                    <input type="hidden" name="productId" value="${product.productId}">
                                                    <input type="hidden" name="quantity" id="addToCartQuantity" value="1">
                                                    <input type="hidden" name="action" value="add">
                                                    <button type="submit" class="btn btn-danger add-to-cart">
                                                        <i class="fa fa-cart-plus"></i> Add to cart
                                                    </button>
                                                </form>

                                                <!-- Buy Now -->
                                                <form id="buyNowForm" action="${pageContext.request.contextPath}/order-confirm" method="post" class="d-inline">
                                                    <input type="hidden" name="productId" value="${product.productId}">
                                                    <input type="hidden" name="quantity" id="buyNowQuantity" value="1">
                                                    <button type="submit" class="btn btn-primary">
                                                        <i class="fa fa-shopping-cart"></i> Buy now
                                                    </button>
                                                </form>
                                            </c:when>
                                            <c:otherwise>
                                                <button type="button" class="btn btn-danger add-to-cart" disabled>
                                                    <i class="fa fa-cart-plus"></i> Sold out
                                                </button>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Product Description -->
                        <div class="product-description mt-5">
                            <h2>Product Description</h2>
                            <p>${product.description}</p>
                        </div>

                        <!-- User Reviews -->
                        <div class="product-reviews mt-5">
                            <h2>Product Reviews</h2>
                            <!-- User's Review -->
                            <div class="user-reviews">
                                <c:if test="${not empty sessionScope.user && not empty userReview}">
                                    <h4>Your Reviews</h4>
                                    <c:forEach var="userReview" items="${userReview}">
                                        <div class="comment-item border-bottom p-1 mb-3" data-review-id="${userReview.reviewId}">
                                            <div class="comment-header">
                                                <span class="comment-username">${userReview.user.account}</span>
                                                <span class="comment-date">
                                                    <fmt:formatDate value="${userReview.reviewDateAsDate}" pattern="dd/MM/yyyy HH:mm:ss"/>
                                                </span>
                                            </div>
                                            <div class="comment-rating">
                                                <c:forEach begin="1" end="${userReview.rating}">
                                                    <i class="fa fa-star text-warning"></i>
                                                </c:forEach>
                                                <c:forEach begin="${userReview.rating + 1}" end="5">
                                                    <i class="fa fa-star text-muted"></i>
                                                </c:forEach>
                                            </div>
                                            <div class="comment-content">${userReview.comment}</div>

                                            <c:if test="${sessionScope.user != null && (sessionScope.user.userId == userReview.user.userId || sessionScope.user.role.id == 1)}">
                                                <div class="comment-actions">
                                                    <button class="btn btn-sm btn-outline-primary edit-comment-btn" 
                                                            data-bs-toggle="modal" data-bs-target="#editReviewModal"
                                                            onclick="openEditReviewModal(${userReview.reviewId}, ${userReview.rating}, '${userReview.comment.replace("'", "\\'")}')">
                                                        <i class="fa-solid fa-edit"></i> Edit
                                                    </button>
                                                    <form action="${pageContext.request.contextPath}/review" method="post" style="display:inline;">
                                                        <input type="hidden" name="action" value="delete">
                                                        <input type="hidden" name="reviewId" value="${userReview.reviewId}">
                                                        <input type="hidden" name="productId" value="${product.productId}">
                                                        <button type="submit" class="btn btn-sm btn-outline-danger delete-comment-btn">
                                                            <i class="fa-solid fa-trash"></i> Delete
                                                        </button>
                                                    </form>
                                                </div>
                                            </c:if>
                                        </div>
                                    </c:forEach>
                                </c:if>
                                <c:if test="${empty sessionScope.user}">
                                    <p class="text-muted">
                                        Please <a href="${pageContext.request.contextPath}/login">log in</a> to post a review.
                                    </p>
                                </c:if>
                            </div>

                            <!-- Other Reviews -->
                            <div class="other-reviews">
                                <h4>Other Reviews</h4>
                                <div class="comment-list" id="commentList">
                                    <c:choose>
                                        <c:when test="${not empty reviews}">
                                            <c:forEach var="review" items="${reviews}">
                                                <c:if test="${review.user.userId != sessionScope.user.userId}">
                                                    <div class="comment-item border-bottom p-1 mb-3" data-review-id="${review.reviewId}">
                                                        <div class="comment-header">
                                                            <span class="comment-username">${review.user.account}</span>
                                                            <span class="comment-date">
                                                                <fmt:formatDate value="${review.reviewDateAsDate}" pattern="dd/MM/yyyy HH:mm:ss"/>
                                                            </span>
                                                        </div>
                                                        <div class="comment-rating">
                                                            <c:forEach begin="1" end="${review.rating}">
                                                                <i class="fa fa-star text-warning"></i>
                                                            </c:forEach>
                                                            <c:forEach begin="${review.rating + 1}" end="5">
                                                                <i class="fa fa-star text-muted"></i>
                                                            </c:forEach>
                                                        </div>
                                                        <div class="comment-content">${review.comment}</div>
                                                    </div>
                                                </c:if>
                                            </c:forEach>
                                        </c:when>
                                        <c:otherwise>
                                            <p class="text-muted">No other reviews yet.</p>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>

                            <!-- Scroll to Top Button -->
                            <button class="btn btn-primary scroll-to-top-comment" id="scrollToTopComment" style="display: none;">
                                <i class="fa-solid fa-arrow-up"></i> Back to Top
                            </button>
                        </div>

                        <!-- Edit Review Modal -->
                        <div class="modal fade" id="editReviewModal" tabindex="-1" aria-labelledby="editReviewModalLabel" aria-hidden="true">
                            <div class="modal-dialog">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h5 class="modal-title" id="editReviewModalLabel">Edit Review</h5>
                                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                    </div>
                                    <div class="modal-body">
                                        <form action="${pageContext.request.contextPath}/review" method="post" id="editReviewForm">
                                            <input type="hidden" name="action" value="edit">
                                            <input type="hidden" name="productId" value="${product.productId}">
                                            <input type="hidden" name="reviewId" id="modalReviewId">
                                            <!-- Product Info -->
                                            <div class="mb-3">
                                                <label class="form-label"><strong>Product name:</strong></label>
                                                <div class="form-control-plaintext">${product.name}</div>
                                            </div>
                                            <!-- Star rating -->
                                            <div class="mb-3">
                                                <label class="form-label"><strong>Select rating:</strong></label>
                                                <div id="modalRating" class="star-rating">
                                                    <i class="far fa-star" data-value="1"></i>
                                                    <i class="far fa-star" data-value="2"></i>
                                                    <i class="far fa-star" data-value="3"></i>
                                                    <i class="far fa-star" data-value="4"></i>
                                                    <i class="far fa-star" data-value="5"></i>
                                                </div>
                                                <input type="hidden" name="rating" id="modalRatingValue" value="0">
                                            </div>
                                            <!-- Review text -->
                                            <div class="mb-3">
                                                <label for="modalReviewText" class="form-label"><strong>Comment:</strong></label>
                                                <textarea class="form-control" id="modalReviewText" name="reviewText" rows="4" placeholder="Write your review..." required></textarea>
                                            </div>
                                        </form>
                                    </div>
                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                        <button type="submit" class="btn btn-primary" form="editReviewForm">
                                            <i class="fa fa-paper-plane me-2"></i>Save changes
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Related Products -->
                        <div class="product-related mt-5">
                            <h2>Related Products</h2>
                            <div class="row">
                                <c:forEach var="relatedProduct" items="${relatedList}">
                                    <div class="col-md-3">
                                        <div class="card">
                                            <img src="${pageContext.request.contextPath}/upload//${relatedProduct.imageUrl}" 
                                                 class="card-img-top" alt="${relatedProduct.name}">
                                            <div class="card-body">
                                                <h5 class="card-title">${relatedProduct.name}</h5>
                                                <p class="card-text">
                                                    <fmt:formatNumber value="${relatedProduct.price}" type="number" groupingUsed="true"/>đ
                                                </p>
                                            </div>
                                            <a href="${pageContext.request.contextPath}/product?id=${relatedProduct.productId}" 
                                               class="text-decoration-none text-center text-dark">
                                                View detail
                                            </a>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>
                    </div>
                </c:when>
                <c:otherwise>
                    <div>
                        <h3>Product does not exist!</h3>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>

        <%@include file="/WEB-INF/include/toast.jsp" %>
        <%@ include file="/WEB-INF/include/footer.jsp" %>
        <%@ include file="/WEB-INF/include/btn-to-top.jsp" %>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
        <script src="${pageContext.request.contextPath}/assets/js/product-view.js"></script>

    </body>
</html>