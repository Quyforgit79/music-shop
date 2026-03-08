<%-- 
    Document   : address
    Created on : May 30, 2025, 15:01:39 AM
    Author     : Nguyen Hoang Thai Vinh - CE190384
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- Address Section -->
<div class="container">
    <!-- Header -->
    <div class="text-center mb-4">
        <h2 class="fw-bold">
            MY ADDRESS
        </h2>
    </div>
    <!-- Address List -->
    <div class="row g-4">
        <c:choose>
            <c:when test="${not empty addresses}">
                <c:forEach var="address" items="${addresses}">
                    <div class="col-12">
                        <div class="card address-card shadow-sm border-0 rounded-4 p-3">
                            <div class="d-flex justify-content-between align-items-start">
                                <div>
                                    <h5 class="fw-semibold mb-1 text-dark">
                                        <i class="fas fa-user text-primary me-2"></i>
                                        ${address.receiverName}
                                        <c:if test="${address.isDefault}">
                                            <span class="badge bg-success ms-2">Default</span>
                                        </c:if>
                                    </h5>
                                    <span class="badge bg-info text-dark">${address.type}</span>
                                </div>
                                <div class="btn-group">
                                    <button class="btn btn-warning btn-sm" 
                                            onclick="openAddressModal('edit', ${address.addressId}, '${address.receiverName.replace("'", "\\'")}',
                                                            '${address.receiverPhone}', '${address.street.replace("'", "\\'")}',
                                                            '${address.ward != null ? address.ward.replace("'", "\\'") : ''}',
                                                            '${address.district != null ? address.district.replace("'", "\\'") : ''}',
                                                            '${address.city.replace("'", "\\'")}', ${address.isDefault})">
                                        <i class="fas fa-edit"></i>
                                    </button>
                                    <!-- Nút mở modal xác nhận -->
                                    <button type="button" class="btn btn-danger btn-sm" data-bs-toggle="modal" data-bs-target="#deleteModal" 
                                            data-address-id="${address.addressId}">
                                        <i class="fas fa-trash"></i>
                                    </button>
                                </div>
                            </div>
                            <div class="row mt-3">
                                <p class="mb-1"><i class="fas fa-phone text-success me-2"></i><strong>Phone: </strong> ${address.receiverPhone}</p>
                                <p class="mb-1"><i class="fas fa-map-marker-alt text-danger me-2"></i><strong>Address: </strong> ${address.getFullAddress()}</p>
                            </div>
                        </div>
                    </div>

                    <!-- delete Address Model -->
                    <div class="modal fade" id="deleteModal" tabindex="-1" aria-labelledby="deleteModalLabel" aria-hidden="true">
                        <div class="modal-dialog modal-dialog-centered"> 
                            <div class="modal-content">
                                <form method="post" action="${pageContext.request.contextPath}/address">
                                    <input type="hidden" name="action" value="delete">
                                    <input type="hidden" name="addressId" id="deleteAddressId" value="${address.addressId}">
                                    <div class="modal-body text-center">
                                        <p class="mb-0">Are you sure you want to delete this address?</p>
                                    </div>
                                    <div class="modal-footer border-0 justify-content-center">
                                        <button type="button" class="btn btn-light" data-bs-dismiss="modal">Cancel</button>
                                        <button type="submit" class="btn btn-danger">Delete</button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <div class="card address-card shadow text-center mt-5 py-5 w-100">
                    <h5 class="text-muted">No Addresses Found</h5>
                    <p class="text-muted">You have not added any addresses yet.</p>
                </div>
            </c:otherwise>
        </c:choose>
        <!-- Add Address Button -->
        <div class="text-center mt-5">
            <button type="button" class="btn btn-primary" onclick="openAddressModal('add')">
                <i class="fas fa-plus me-2"></i> Add New Address
            </button>
        </div>
    </div>
    
    <!-- Address Modal -->
    <div class="modal fade" id="addressModal" tabindex="-1" aria-labelledby="addressModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <form id="addressForm" method="post" action="${pageContext.request.contextPath}/address">
                    <input type="hidden" name="action" id="formAction" value="add">
                    <input type="hidden" name="addressId" id="addressId" value="${editAddressId != null ? editAddressId : ''}">
                    <div class="modal-header bg-warning">
                        <h5 class="modal-title" id="addressModalLabel">Your Address</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <div class="row mb-3 align-items-center">
                            <label for="receiverName" class="col-sm-4 col-form-label text-end">Full Name <span class="text-danger">*</span></label>
                            <div class="col-sm-8">
                                <input type="text" class="form-control" id="receiverName" name="receiverName" placeholder="Enter full name" value="${receiverName != null ? receiverName : ''}">
                                <div class="invalid-feedback" id="nameError">${nameError != null ? nameError : ''}</div>
                            </div>
                        </div>
                        <div class="row mb-3 align-items-center">
                            <label for="receiverPhone" class="col-sm-4 col-form-label text-end">Phone Number <span class="text-danger">*</span></label>
                            <div class="col-sm-8">
                                <input type="text" class="form-control" id="receiverPhone" name="receiverPhone" placeholder="Enter phone number" value="${receiverPhone != null ? receiverPhone : ''}">
                                <div class="invalid-feedback" id="phoneError">${phoneError != null ? phoneError : ''}</div>
                            </div>
                        </div>
                        <div class="row mb-3 align-items-center">
                            <label for="street" class="col-sm-4 col-form-label text-end">Street & House No. <span class="text-danger">*</span></label>
                            <div class="col-sm-8">
                                <input type="text" class="form-control" id="street" name="street" placeholder="e.g., 123 ABC Street" value="${street != null ? street : ''}">
                                <div class="invalid-feedback" id="streetError">${streetError != null ? streetError : ''}</div>
                            </div>
                        </div>
                        <div class="row mb-3 align-items-center">
                            <label for="ward" class="col-sm-4 col-form-label text-end">Ward/Commune</label>
                            <div class="col-sm-8">
                                <input type="text" class="form-control" id="ward" name="ward" value="${ward != null ? ward : ''}">
                                <div class="invalid-feedback" id="wardError">${wardError != null ? wardError : ''}</div>
                            </div>
                        </div>
                        <div class="row mb-3 align-items-center">
                            <label for="district" class="col-sm-4 col-form-label text-end">District</label>
                            <div class="col-sm-8">
                                <input type="text" class="form-control" id="district" name="district" value="${district != null ? district : ''}">
                                <div class="invalid-feedback" id="districtError">${districtError != null ? districtError : ''}</div>
                            </div>
                        </div>
                        <div class="row mb-3 align-items-center">
                            <label for="city" class="col-sm-4 col-form-label text-end">City/Province <span class="text-danger">*</span></label>
                            <div class="col-sm-8">
                                <select id="city" name="city" class="form-select" required>
                                    <option value="" disabled ${city == null ? 'selected' : ''}>-- Select City/Province --</option>
                                </select>
                                <div class="invalid-feedback" id="cityError">${cityError != null ? cityError : ''}</div>
                            </div>
                        </div>
                        <div class="row mb-3 align-items-center">
                            <label class="col-sm-4 col-form-label text-end">Set Address type?</label>
                            <div class="col-sm-8">
                                <div class="form-check form-check-inline">
                                    <input class="form-check-input" type="radio" id="style1" name="style" value="1"
                                           ${empty style or style == '1' ? 'checked' : ''}>
                                    <label class="form-check-label" for="style1">Home</label>
                                </div>
                                <div class="form-check form-check-inline">
                                    <input class="form-check-input" type="radio" id="style2" name="style" value="2"
                                           ${style == '2' ? 'checked' : ''}>
                                    <label class="form-check-label" for="style2">Office</label>
                                </div>
                                <div class="form-check form-check-inline">
                                    <input class="form-check-input" type="radio" id="style3" name="style" value="3"
                                           ${style == '3' ? 'checked' : ''}>
                                    <label class="form-check-label" for="style3">Other</label>
                                </div>
                            </div>
                        </div>
                        <div class="row mb-3 align-items-center">
                            <label class="col-sm-4 col-form-label text-end">Set as Default Address?</label>
                            <div class="col-sm-8">
                                <div class="form-check">
                                    <input class="form-check-input" type="checkbox" id="isDefault" name="isDefault" value="true" ${isDefault eq 'true' ? 'checked' : ''}>
                                    <label class="form-check-label" for="isDefault">Yes, set as default</label>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                        <button type="submit" class="btn btn-primary" id="submitButton">${updateFail != null ? 'Update Address' : 'Save Address'}</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<script>
    window.addEventListener('DOMContentLoaded', () => {
        const hasError = ${not empty addFail or not empty updateFail ? "true" : "false"};
        if (hasError) {
            populateCitySelect('city');
            const modalEl = document.getElementById('addressModal');
            const modalInstance = new bootstrap.Modal(modalEl);
            modalInstance.show();
        }
    });
</script>