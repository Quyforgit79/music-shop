<%-- 
   Document   : order-history
   Created on : Jul 18, 2025, 11:20:18 AM
   Author     : Nguyen Hoang Thai Vinh - CE190384
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div class="container">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2 class="mb-4 fw-bold text-center">ORDER HISTORY</h2>
        <c:if test="${not empty histories}">
            <button class="btn btn-danger btn-sm" data-bs-toggle="modal" data-bs-target="#confirmDeleteModal" data-action="deleteAll">
                Clear All History
            </button>
        </c:if>
    </div>

    <c:choose>
        <c:when test="${not empty histories}">
            <div class="row d-flex flex-column">
                <c:forEach var="order" items="${histories}">
                    <div class="col">
                        <div class="card border-0 shadow-sm p-3">
                            <div class="d-flex align-items-center">
                                <img src="${pageContext.request.contextPath}/upload/${order.orderDetail.product.imageUrl}" alt="${order.orderDetail.product.name}" class="me-3" style="width: 80px; height: 80px; object-fit: cover;">
                                <div class="flex-grow-1">
                                    <strong>#${order.orderId}</strong><br>
                                    <small>${order.orderDetail.product.name}</small><br>
                                    <small class="text-muted"> 
                                        <span>Order date: </span><fmt:formatDate value="${order.orderDateAsDate}" pattern="dd/MM/yyyy"/>
                                    </small>
                                    <br>
                                    <small class="text-muted"> 
                                        <span>Estimate delivery date: </span><fmt:formatDate value="${order.estimatedDeliveryAsDate}" pattern="dd/MM/yyyy"/>
                                    </small>
                                </div>
                                <div class="text-end d-flex">
                                    <a href="${pageContext.request.contextPath}/product?id=${order.orderDetail.product.productId}" class="btn btn-outline-primary btn-sm">See Details</a>
                                    <button class="btn btn-outline-danger btn-sm me-3" data-bs-toggle="modal" data-bs-target="#confirmDeleteModal" data-action="delete" data-order-id="${order.orderId}">
                                        Delete
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:when>
        <c:otherwise>
            <div class="text-center mt-4 p-4 bg-light rounded">
                <h5 class="text-muted">No Order History</h5>
                <p class="text-muted">You haven't placed any orders yet.</p>
                <a href="${pageContext.request.contextPath}/home" class="btn btn-primary btn-sm">Shop Now</a>
            </div>
        </c:otherwise>
    </c:choose>

    <!-- Modal xác nhận xóa -->
    <div class="modal fade" id="confirmDeleteModal" tabindex="-1" aria-labelledby="confirmDeleteModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">  <!-- thêm modal-dialog-centered -->
            <div class="modal-content">
                <div class="modal-body text-center">  <!-- căn giữa nội dung -->
                    <p class="mb-0">Are you sure you want to <span id="deleteActionText"></span>?</p>
                </div>
                <div class="modal-footer border-0 justify-content-center">
                    <button type="button" class="btn btn-light" data-bs-dismiss="modal">Cancel</button>
                    <form id="deleteForm" action="${pageContext.request.contextPath}/order" method="post" class="m-0">
                        <input type="hidden" name="action" id="deleteAction">
                        <input type="hidden" name="orderId" id="deleteOrderId">
                        <button type="submit" class="btn btn-danger">Delete</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    // Xử lý sự kiện khi modal được hiển thị
    const confirmDeleteModal = document.getElementById('confirmDeleteModal');
    confirmDeleteModal.addEventListener('show.bs.modal', function (event) {
        const button = event.relatedTarget; // Nút kích hoạt modal
        const action = button.getAttribute('data-action');
        const orderId = button.getAttribute('data-order-id') || '';

        // Cập nhật nội dung modal
        const actionText = document.getElementById('deleteActionText');
        const deleteActionInput = document.getElementById('deleteAction');
        const deleteOrderIdInput = document.getElementById('deleteOrderId');

        if (action === 'deleteAll') {
            actionText.textContent = 'delete all order history';
        } else {
            actionText.textContent = 'delete this order';
        }

        deleteActionInput.value = action;
        deleteOrderIdInput.value = orderId;
    });
</script>
