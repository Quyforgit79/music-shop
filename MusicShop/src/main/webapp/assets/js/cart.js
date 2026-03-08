/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

let confirmDeleteBtn = document.getElementById("confirmDeleteBtn");

function showDeleteModal(deleteUrl, isAll = false) {
    let modalBody = document.querySelector("#confirmDeleteModal .modal-body");
    modalBody.textContent = isAll
            ? "Are you sure you want to delete ALL items in your cart?"
            : "Are you sure you want to delete this item?";
    confirmDeleteBtn.setAttribute("href", deleteUrl);

    let modal = new bootstrap.Modal(document.getElementById("confirmDeleteModal"));
    modal.show();
}