<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Order Confirmation</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/assets/css/order.confirm.css" rel="stylesheet"/>
        <link href="${pageContext.request.contextPath}/assets/css/header.css" rel="stylesheet"/>
        <link href="${pageContext.request.contextPath}/assets/css/footer.css" rel="stylesheet"/>
    </head>
    <body>
        <%@ include file="/WEB-INF/include/header.jsp" %>
        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>
        <c:if test="${discountAmount == null}">
            <c:set var="discountAmount" value="0"/>
        </c:if>
            <div class="musicshop-buy-container" style="margin-top: 100px">
            <div class="musicshop-header bg-warning">
                <i class="fa fa-shopping-bag"></i> Order Confirmation
            </div>

            <!-- Delivery address -->
            <div class="musicshop-section">
                <div class="musicshop-section-title text-info"><i class="fa fa-map-marker-alt me-2"></i>Delivery address</div>
                <div class="musicshop-address">
                    <c:if test="${not empty addressFail}">
                        <div class="alert alert-warning">You have no saved address. Please add your delivery address in <a href="${pageContext.request.contextPath}/account?view=address">Address Management</a>.</div>
                    </c:if>
                    <c:if test="${not empty defaultAddress}">
                        <div>
                            <strong id="receiverName">${defaultAddress.receiverName}</strong> |
                            <span id="receiverPhone">${defaultAddress.receiverPhone}</span><br>
                            <span id="receiverAddress">${defaultAddress.fullAddress}</span>
                        </div>
                        <button type="button" class="btn btn-outline-primary btn-sm me-2" data-bs-toggle="modal" data-bs-target="#addressModal">
                            <i class="fa fa-map-pin"></i> Other address
                        </button>
                    </c:if>
                </div>
            </div>

            <!-- Product -->
            <div class="musicshop-section">
                <div class="musicshop-section-title text-info">Product</div>
                <div class="musicshop-product-row">
                    <img src="${pageContext.request.contextPath}/upload/${product.imageUrl}" alt="${product.name}" width="100">
                    <div class="musicshop-product-info">
                        <div class="musicshop-product-name">${product.name}</div>
                        <div class="musicshop-product-attr">Quantity: <span id="quantity">${quantity}</span></div>
                    </div>
                    <div class="musicshop-product-price"><fmt:formatNumber value="${product.price}" pattern="#,##0"/>đ</div>
                </div>
            </div>

            <!-- Select voucher -->
            <div class="musicshop-section">
                <div class="musicshop-section-title text-info">
                    <i class="fa fa-ticket-alt me-2"></i>Select vouchers
                </div>
                <div class="d-flex align-items-center flex-wrap gap-2">
                    <form method="post" action="${pageContext.request.contextPath}/order-confirm" class="d-flex flex-wrap gap-2" id="voucherForm">
                        <input type="hidden" name="productId" value="${product.productId}" />
                        <input type="hidden" name="quantity" value="${quantity}" />
                        <input type="hidden" name="paymentMethod" id="voucherPaymentMethod" value="${paymentMethod != null ? paymentMethod : '1'}" />
                        <select class="form-select w-auto" id="voucherSelect" name="voucherId" style="min-width:180px;">
                            <option value="not">-- Not applicable --</option>
                            <c:if test="${empty discountList}">
                                <option value="not" disabled>No vouchers available</option>
                            </c:if>
                            <c:forEach var="discount" items="${discountList}">
                                <option value="${discount.discountId}" <c:if test="${selectedDiscount != null && selectedDiscount.discountId == discount.discountId}">selected</c:if>>
                                    ${discount.description}
                                </option>
                            </c:forEach>
                        </select>
                        <button type="submit" class="btn btn-success btn-sm">
                            <i class="fa fa-check me-1"></i>Apply
                        </button>
                    </form>
                </div>
                <c:if test="${not empty selectedDiscount}">
                    <div id="voucherInfo" class="form-text text-success mt-1">
                        <strong>${selectedDiscount.description}</strong><br/>
                        <c:choose>
                            <c:when test="${selectedDiscount.discountType.label == 'percentage'}">
                                Discount: ${selectedDiscount.discountValue}% 
                            </c:when>
                            <c:otherwise>
                                Discount: <fmt:formatNumber value="${selectedDiscount.discountValue}" pattern="#,##0"/> VNĐ
                            </c:otherwise>
                        </c:choose>
                    </div>
                </c:if>
            </div>

            <!-- Payment method -->
            <div class="musicshop-section">
                <div class="musicshop-section-title text-info">Payment method</div>
                <div class="d-flex align-items-center flex-wrap gap-2">
                    <i class="fa fa-money-bill-wave me-2"></i>
                    <select id="paymentMethodSelect" name="paymentMethod" class="form-select w-auto" style="min-width:220px;">
                        <option value="1" <c:if test="${paymentMethod == 1 || empty paymentMethod}">selected</c:if>>Cash on Delivery (COD)</option>
                        <option value="2" <c:if test="${paymentMethod == '2'}">selected</c:if>>Bank transfer</option>
                        </select>
                    </div>
                </div>

                <!-- Order Summary -->
                <div class="musicshop-section">
                    <div class="musicshop-section-title text-info">Order Overview</div>
                    <div class="musicshop-summary-row">
                        <span>Discount:</span>
                        <span id="discountAmount">
                            -<c:choose>
                            <c:when test="${not empty discountAmount && discountAmount > 0}">
                                <fmt:formatNumber value="${discountAmount}" pattern="#,##0"/>đ
                            </c:when>
                            <c:otherwise>0đ</c:otherwise>
                        </c:choose>
                    </span>
                </div>
                <div class="musicshop-summary-row">
                    <span>Total product price</span>
                    <span><fmt:formatNumber value="${productPrice}" type="number" pattern="#,##0"/>đ</span>
                </div>
                <div class="musicshop-summary-row total">
                    <span class="text-warning">Total payment</span>
                    <span id="finalAmount">
                        <c:choose>
                            <c:when test="${not empty totalAmount && totalAmount > 0}">
                                <strong><fmt:formatNumber value="${totalAmount}" pattern="#,##0"/>đ</strong>
                            </c:when>
                            <c:otherwise>Unknown</c:otherwise>
                        </c:choose>
                    </span>
                </div>
            </div>

            <!-- Order Form -->
            <form id="orderForm" method="post" action="${pageContext.request.contextPath}/order-submit">
                <input type="hidden" name="addressId" id="addressIdInput" value="${defaultAddress.addressId}" />
                <input type="hidden" name="productId" value="${product.productId}" />
                <input type="hidden" name="quantity" value="${quantity}" />
                <input type="hidden" name="voucherId" id="voucherIdInput" value="${selectedDiscount != null ? selectedDiscount.discountId : 'not'}" />
                <input type="hidden" name="paymentMethod" id="paymentMethodInput" value="${paymentMethod != null ? paymentMethod : '1'}" />
                <button type="submit" class="musicshop-btn-order btn btn-primary py-1" <c:if test="${empty defaultAddress}">disabled</c:if>>
                        <i class="fa fa-check me-2"></i>Order
                    </button>
                </form>

                <!-- Address Modal -->
                <div class="modal fade" id="addressModal" tabindex="-1" aria-labelledby="addressModalLabel" aria-hidden="true">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title"><i class="fa fa-map-marker-alt me-2"></i>Select Delivery Address</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body">
                                <div id="addressListSection">
                                    <div class="list-group" id="addressListContainer">
                                    <c:if test="${not empty addressList}">
                                        <form action="${pageContext.request.contextPath}/address" method="post">
                                            <input type="hidden" name="productId" value="${product.productId}">
                                            <input type="hidden" name="quantity" value="${quantity}">
                                            <input type="hidden" name="action" value="updateDefaultAddress">
                                            <input type="hidden" name="page" value="orderConfirm">
                                            <input type="hidden" name="userId" value="${user.userId}">
                                            <input type="hidden" name="voucherId" value="${selectedDiscount != null ? selectedDiscount.discountId : '1'}">
                                            <input type="hidden" name="paymentMethod" value="${paymentMethod != null ? paymentMethod : '1'}">
                                            <c:forEach var="address" items="${addressList}">
                                                <button type="submit" name="addressId" value="${address.addressId}" class="list-group-item list-group-item-action">
                                                    ${address.receiverName} | ${address.receiverPhone}<br>
                                                    <small>${address.fullAddress}</small>
                                                </button>
                                            </c:forEach>
                                        </form>
                                    </c:if>
                                    <c:if test="${empty addressList}">
                                        <div class="text-muted">You have no addresses. Please add a new one in <a href="${pageContext.request.contextPath}/address-management">Address Management</a>.</div>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
        <!-- footer -->
        <%@ include file="/WEB-INF/include/footer.jsp" %>
        <!-- Scripts -->
        <%@include file="/WEB-INF/include/toast.jsp" %>
        <script src="${pageContext.request.contextPath}/assets/js/order-confirm.js"></script>
    </body>
</html>