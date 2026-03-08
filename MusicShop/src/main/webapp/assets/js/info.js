/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

//window.addEventListener("DOMContentLoaded", function () {
//    const editBtn = document.getElementById("editBtn");
//    const saveBtn = document.getElementById("saveBtn");
//    const userForm = document.getElementById("userForm");
//
//    // Log để kiểm tra các phần tử
//    console.log("Elements:", {
//        editBtn: editBtn,
//        saveBtn: saveBtn,
//        userForm: userForm
//    });
//
//    // Chuyển sang chế độ chỉnh sửa khi nhấp vào nút Edit
//    if (editBtn && saveBtn) {
//        editBtn.addEventListener("click", function () {
//            console.log("Edit button clicked");
//            document.querySelectorAll(".info-value").forEach(el => el.classList.add("d-none"));
//            document.querySelectorAll("input.form-control, .gender-group, select.form-select").forEach(el => el.classList.remove("d-none"));
//            document.getElementById("birthdateDisplay").classList.add("d-none");
//            document.getElementById("birthdateInputs").classList.remove("d-none");
//            editBtn.classList.add("d-none");
//            saveBtn.classList.remove("d-none");
//        });
//    } else {
//        console.error("Edit button or Save button not found");
//    }
//
//    // Tự động kích hoạt chế độ chỉnh sửa nếu có updateFail từ server
//    <c:if test="${not empty updateFail}">
//console.log("Auto-triggering edit mode due to updateFail");
//editBtn.click();
//</c:if>//
//
//    // Log khi form được gửi
//    if (userForm) {
//        userForm.addEventListener("submit", function (e) {
//            console.log("Form submit event triggered");
//            const birthDay = document.getElementById("birthDay") ? document.getElementById("birthDay").value : "";
//            const birthMonth = document.getElementById("birthMonth") ? document.getElementById("birthMonth").value : "";
//            const birthYear = document.getElementById("birthYear") ? document.getElementById("birthYear").value : "";
//            console.log("Form submitted with values:", {birthDay, birthMonth, birthYear});
//        });
//    } else {
//        console.error("Form element not found");
//    }
//});
//
//// Xác thực ngày sinh trước khi gửi form
//userForm.addEventListener("submit", function (e) {
//    console.log("Form submit event triggered");
//    const birthDay = document.getElementById("birthDay").value;
//    const birthMonth = document.getElementById("birthMonth").value;
//    const birthYear = document.getElementById("birthYear").value;
//    const birthdateErrorClient = document.getElementById("birthdateErrorClient");
//
//    // Kiểm tra nếu một phần của ngày sinh được chọn nhưng không đầy đủ
//    if ((birthDay || birthMonth || birthYear) && !(birthDay && birthMonth && birthYear)) {
//        e.preventDefault(); // Ngăn gửi form
//        birthdateErrorClient.textContent = "Please select complete birthdate (day, month, year)";
//        birthdateErrorClient.classList.remove("d-none");
//        return;
//    }
//
//    // Kiểm tra ngày sinh hợp lệ (ví dụ: 31/04 hoặc 29/02 không hợp lệ)
//    if (birthDay && birthMonth && birthYear) {
//        const date = new Date(birthYear, birthMonth - 1, birthDay);
//        if (date.getDate() != birthDay || date.getMonth() + 1 != birthMonth || date.getFullYear() != birthYear) {
//            e.preventDefault(); // Ngăn gửi form
//            birthdateErrorClient.textContent = "Invalid date (e.g., 31/04 or 29/02 in non-leap year)";
//            birthdateErrorClient.classList.remove("d-none");
//            return;
//        }
//        // Kiểm tra năm hợp lệ
//        const currentYear = new Date().getFullYear();
//        if (birthYear < 1900 || birthYear > currentYear) {
//            e.preventDefault();
//            birthdateErrorClient.textContent = `Year must be between 1900 and ${currentYear}`;
//            birthdateErrorClient.classList.remove("d-none");
//            return;
//        }
//    }
//
//    console.log("Form submitted with values:", {birthDay, birthMonth, birthYear});
//});

window.addEventListener("DOMContentLoaded", function () {
    const editBtn = document.getElementById("editBtn");
    const saveBtn = document.getElementById("saveBtn");
    const userForm = document.getElementById("userForm");
    const avatarInput = document.getElementById("avatarInput");
    const avatarPreview = document.getElementById("avatarPreview");
    const previewImage = document.getElementById("previewImage");
    const avatarErrorClient = document.getElementById("avatarErrorClient");

    // Log elements for debugging
    console.log("Elements:", {
        editBtn: editBtn,
        saveBtn: saveBtn,
        userForm: userForm,
        avatarInput: avatarInput
    });

    // Toggle edit mode when Edit button is clicked
    if (editBtn && saveBtn) {
        editBtn.addEventListener("click", function () {
            console.log("Edit button clicked");
            document.querySelectorAll(".info-value").forEach(el => el.classList.add("d-none"));
            document.querySelectorAll("input.form-control, .gender-group, select.form-select").forEach(el => el.classList.remove("d-none"));
            document.getElementById("birthdateDisplay").classList.add("d-none");
            document.getElementById("birthdateInputs").classList.remove("d-none");
            avatarInput.classList.remove("d-none");
            avatarPreview.classList.remove("d-none");
            editBtn.classList.add("d-none");
            saveBtn.classList.remove("d-none");
        });
    } else {
        console.error("Edit button or Save button not found");
    }

    // Handle avatar preview
    if (avatarInput) {
        avatarInput.addEventListener("change", function (e) {
            const file = e.target.files[0];
            if (file) {
                // Validate file format
                if (!file.type.startsWith("image/")) {
                    avatarErrorClient.textContent = "Please select an image file";
                    avatarErrorClient.classList.remove("d-none");
                    avatarInput.value = "";
                    previewImage.src = "#";
                    avatarPreview.classList.add("d-none");
                    return;
                }
                // Validate file size (max 5MB)
                if (file.size > 5 * 1024 * 1024) {
                    avatarErrorClient.textContent = "Image size must be less than 5MB";
                    avatarErrorClient.classList.remove("d-none");
                    avatarInput.value = "";
                    previewImage.src = "#";
                    avatarPreview.classList.add("d-none");
                    return;
                }
                const reader = new FileReader();
                reader.onload = function (e) {
                    previewImage.src = e.target.result;
                    avatarPreview.classList.remove("d-none");
                    avatarErrorClient.classList.add("d-none");
                };
                reader.readAsDataURL(file);
            } else {
                avatarPreview.classList.add("d-none");
            }
        });
    }

    // Auto-trigger edit mode if update fails
    <c:if test="${not empty updateFail}">
        console.log("Auto-triggering edit mode due to updateFail");
        editBtn.click();
    </c:if>

    // Log form submission
    if (userForm) {
        userForm.addEventListener("submit", function (e) {
            console.log("Form submit event triggered");
            const birthDay = document.getElementById("birthDay") ? document.getElementById("birthDay").value : "";
            const birthMonth = document.getElementById("birthMonth") ? document.getElementById("birthMonth").value : "";
            const birthYear = document.getElementById("birthYear") ? document.getElementById("birthYear").value : "";
            console.log("Form submitted with values:", {birthDay, birthMonth, birthYear});
        });
    } else {
        console.error("Form element not found");
    }
});

// Validate avatar before form submission
userForm.addEventListener("submit", function (e) {
    console.log("Form submit event triggered");
    const avatarInput = document.getElementById("avatarInput");
    const avatarErrorClient = document.getElementById("avatarErrorClient");

    // Validate avatar
    if (avatarInput.files[0]) {
        const file = avatarInput.files[0];
        if (!file.type.startsWith("image/")) {
            e.preventDefault();
            avatarErrorClient.textContent = "Please select an image file";
            avatarErrorClient.classList.remove("d-none");
            return;
        }
        if (file.size > 5 * 1024 * 1024) {
            e.preventDefault();
            avatarErrorClient.textContent = "Image size must be less than 5MB";
            avatarErrorClient.classList.remove("d-none");
            return;
        }
    }
});