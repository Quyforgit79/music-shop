<%-- 
    Document   : toast
    Created on : Jun 25, 2025, 5:14:01 PM
    Author     : Nguyen Hoang Thai Vinh - CE190384
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="position-fixed top-0 end-0 p-3" style="z-index: 1100;">
    <div id="toastNotification" class="toast align-items-center text-white border-0 shadow" role="alert" aria-live="assertive" aria-atomic="true">
        <div class="d-flex">
            <div class="toast-body" id="toastMessage"></div>
            <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="?óng"></button>
        </div>
    </div>
</div>

<script>
    function showToast(message, isSuccess = true) {
        const toastEl = document.getElementById('toastNotification');
        const toastMsg = document.getElementById('toastMessage');

        toastEl.classList.remove('bg-success', 'bg-danger');
        toastEl.classList.add(isSuccess ? 'bg-success' : 'bg-danger');

        toastMsg.textContent = message;

        const toast = new bootstrap.Toast(toastEl, {
            animation: true,
            delay: 3000
        });

        toast.show();
    }

    window.addEventListener("DOMContentLoaded", () => {
    <c:if test="${not empty updateSuccess}">
        showToast("${updateSuccess}", true);
    </c:if>
    <c:if test="${not empty updateFail}">
        showToast("${updateFail}", false);
    </c:if>
    <c:if test="${not empty addSuccess}">
        showToast("${addSuccess}", true);
    </c:if>
    <c:if test="${not empty addFail}">
        showToast("${addFail}", false);
    </c:if>
    <c:if test="${not empty deleteSuccess}">
        showToast("${deleteSuccess}", true);
    </c:if>
    <c:if test="${not empty deleteFail}">
        showToast("${deleteFail}", false);
    </c:if>
    });
</script>