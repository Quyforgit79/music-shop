<%-- 
    Document   : order-view
    Created on : Jun 30, 2025, 11:43:01 PM
    Author     : Nguyen Hoang Thai Vinh - CE190384
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Order Details</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/header.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/footer.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/order.view.css"/>
    </head>
    <body>
        <%@ include file="/WEB-INF/include/header.jsp" %>

        <!-- Set default locale for currency formatting -->
        <fmt:setLocale value="vi_VN"/>

        <div class="container" style="margin-top: 80px">
            <div class="order-confirmation">
                <!-- Order Information -->
                <div class="order-info">
                    <h2><i class="fa fa-info-circle me-2"></i>Order Information</h2>
                    <div><strong>Order ID:</strong> #${order.orderId}</div>
                    <div>
                        <strong>Order Date:</strong> 
                        <fmt:formatDate value="${order.orderDate}" pattern="dd/MM/yyyy HH:mm:ss" />
                    </div>
                    <div>
                        <strong>Estimated Delivery:</strong>
                        <fmt:formatDate value="${order.estimatedDelivery}" pattern="dd/MM/yyyy" />
                    </div>
                    <div>
                        <strong>Status:</strong> 
                        <span class="badge bg-warning text-dark">
                            <i class="fa fa-clock me-1"></i>${order.status}
                        </span>
                    </div>
                </div>

                <!-- Product Items -->
                <div class="order-summary">
                    <h2><i class="fa fa-shopping-cart me-2"></i>Products</h2>
                    <c:forEach var="detail" items="${order.orderDetails}">
                        <div class="order-item">
                            <img src="${pageContext.request.contextPath}/upload/${detail.product.imageUrl}" alt="${detail.product.name}" width="100">
                            <div class="item-info">
                                <div class="item-name">${detail.product.name}</div>
                                <div>Quantity: ${detail.quantity}</div>
                                <div class="item-price">
                                    <fmt:formatNumber value="${detail.price}" currencySymbol="đ" groupingUsed="true" pattern="#,##0"/> VNÐ
                                </div>
                            </div>
                        </div>
                    </c:forEach>

                    <div class="total">
                        <span>Total:</span>
                        <span><fmt:formatNumber value="${order.totalAmount}" currencySymbol="đ" groupingUsed="true" pattern="#,##0"/> VNÐ</span>
                    </div>
                    <div class="total">
                        <span>Discount:</span>
                        <c:choose>
                            <c:when test="${not empty discountPercent}">
                                <span><fmt:formatNumber value="${discountPercent}" pattern="#,##0"/>%</span>
                            </c:when>
                            <c:when test="${not empty discountNull}">
                                <span>${discountNull}</span>
                            </c:when>
                            <c:otherwise>
                                <span><fmt:formatNumber value="${discountValue}" pattern="#,##0"/> VNÐ</span>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <!-- Shipping Address -->
                <div class="shipping-address">
                    <h2><i class="fa fa-map-marker-alt me-2"></i>Shipping Address</h2>
                    <div><strong>Recipient Name:</strong> ${order.address.receiverName}</div>
                    <div><strong>Phone Number:</strong> ${order.address.receiverPhone}</div>
                    <div><strong>Address:</strong> ${order.address.getFullAddress()}</div>
                </div>

                <!-- Payment Method -->
                <div class="payment-method">
                    <h2><i class="fa fa-credit-card me-2"></i>Payment Method</h2>
                    <div class="btn bg-info"><strong>${order.paymentMethod}</strong></div>
                </div>

                <c:if test="${order.status.label == 'Pending'}">
                    <div class="action-buttons">
                        <button class="btn btn-danger p-2" data-bs-toggle="modal" data-bs-target="#returnModal">
                            <i class="fa fa-undo me-2"></i>cancel order
                        </button>
                    </div>
                </c:if>

                <div class="d-flex justify-content-center">
                    <c:if test="${order.status.label == 'Delivered'}">
                        <div class="action-buttons">
                            <button  class="btn btn-success p-2">
                                <a href="${pageContext.request.contextPath}/product-review?orderId=${order.orderId}" class="text-light text-decoration-none"/>
                                <i class="fa fa-check me-2"></i>Confirm Receipt
                                </a>
                            </button>
                            <button class="btn btn-danger p-2" data-bs-toggle="modal" data-bs-target="#returnModal">
                                <i class="fa fa-undo me-2"></i>Return Order
                            </button>
                        </div>
                    </c:if>
                </div>
            </div>
        </div>

        <!-- Return Modal -->
        <div class="modal fade" id="returnModal" tabindex="-1" aria-labelledby="returnModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="returnModalLabel">Return Order</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form id="returnForm" action="${pageContext.request.contextPath}/order" method="post">
                            <input type="hidden" name="action" value="createReturn">
                            <input type="hidden" name="id" value="${order.orderId}">
                            <div class="return-reason">
                                <label for="returnReason"><strong>Reason for Return</strong></label>
                                <textarea class="form-control" id="returnDetails" name="reason" rows="4" placeholder="Provide more details about the return"></textarea>
                            </div>
                            <button type="submit" class="btn btn-danger p-2 mt-2">Submit Return Request</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <%@ include file="/WEB-INF/include/footer.jsp" %>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
