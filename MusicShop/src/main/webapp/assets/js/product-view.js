/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

// Thumbnail click to change main image
document.querySelectorAll('.product-thumbnail-images img').forEach(img => {
    img.addEventListener('click', function() {
        document.getElementById('mainProductImage').src = this.src;
    });
});

// Lấy danh sách các ảnh thumbnail
const thumbnails = Array.from(document.querySelectorAll('.product-thumbnail-images img'));
const mainImage = document.getElementById('mainProductImage');
let currentIndex = 0;

// Đặt ảnh chính ban đầu là ảnh đầu tiên trong thumbnails nếu có
if (thumbnails.length > 0) {
    mainImage.src = thumbnails[0].src;
    currentIndex = 0;
}

// Khi nhấn vào thumbnail, đổi ảnh chính và cập nhật chỉ số
thumbnails.forEach((img, idx) => {
    img.addEventListener('click', function () {
        mainImage.src = this.src;
        currentIndex = idx;
        highlightThumbnail(idx);
    });
});

// Nút chuyển trái
document.getElementById('prevImageBtn').addEventListener('click', function () {
    if (thumbnails.length === 0)
        return;
    currentIndex = (currentIndex - 1 + thumbnails.length) % thumbnails.length;
    mainImage.src = thumbnails[currentIndex].src;
    highlightThumbnail(currentIndex);
});

// Nút chuyển phải
document.getElementById('nextImageBtn').addEventListener('click', function () {
    if (thumbnails.length === 0)
        return;
    currentIndex = (currentIndex + 1) % thumbnails.length;
    mainImage.src = thumbnails[currentIndex].src;
    highlightThumbnail(currentIndex);
});

// Hiển thị viền cho thumbnail đang chọn
function highlightThumbnail(idx) {
    thumbnails.forEach((img, i) => {
        img.style.border = i === idx ? '2px solid #8b5cf6' : '1px solid #ddd';
    });
}
// Khởi tạo viền cho thumbnail đầu tiên
highlightThumbnail(currentIndex);

// Helper function to escape special characters for JavaScript
function escapeJsString(text) {
    if (!text)
        return '';
    return text.replace(/'/g, "\\'").replace(/"/g, '\\"').replace(/\n/g, '\\n');
}

// Update quantity for forms
function updateQuantityForms() {
    const quantityInput = document.getElementById('userQuantityInput');
    const buyNowQuantity = document.getElementById('buyNowQuantity');
    const addToCartQuantity = document.getElementById('addToCartQuantity');
    if (quantityInput && buyNowQuantity && addToCartQuantity) {
        buyNowQuantity.value = quantityInput.value;
        addToCartQuantity.value = quantityInput.value;
    }
}

// Handle quantity change
function changeQuantity(delta) {
    const input = document.getElementById('userQuantityInput');
    if (input) {
        let value = parseInt(input.value);
        const min = parseInt(input.min);
        const max = parseInt(input.max);
        value = Math.min(max, Math.max(min, value + delta));
        input.value = value;
        updateQuantityForms();
    }
}

// Initialize quantity
updateQuantityForms();
const quantityInput = document.getElementById('userQuantityInput');
if (quantityInput) {
    quantityInput.addEventListener('input', updateQuantityForms);
}

// Scroll to Top Button for Comment Section
const commentList = document.getElementById('commentList');
const scrollToTopComment = document.getElementById('scrollToTopComment');
if (commentList && scrollToTopComment) {
    commentList.addEventListener('scroll', () => {
        scrollToTopComment.style.display = commentList.scrollTop > 100 ? 'block' : 'none';
    });
    scrollToTopComment.addEventListener('click', () => {
        commentList.scrollTo({top: 0, behavior: 'smooth'});
    });
}

// Gán nội dung review vào modal
function openEditReviewModal(reviewId, rating, comment) {
    try {
        const modalReviewId = document.getElementById('modalReviewId');
        const modalRatingValue = document.getElementById('modalRatingValue');
        const modalReviewText = document.getElementById('modalReviewText');
        const stars = document.querySelectorAll('#modalRating i');

        if (!modalReviewId || !modalRatingValue || !modalReviewText || !stars) {
            console.error('Modal elements not found');
            return;
        }

        modalReviewId.value = reviewId || '';
        modalRatingValue.value = rating || 0;
        modalReviewText.value = comment ? comment.replace(/\\'/g, "'").replace(/\\"/g, '"').replace(/\\n/g, '\n') : '';

        stars.forEach(star => {
            star.classList.remove('fas');
            star.classList.add('far');
        });
        for (let i = 0; i < rating; i++) {
            stars[i].classList.remove('far');
            stars[i].classList.add('fas');
        }
    } catch (error) {
        console.error('Error populating edit review modal:', error);
    }
}

// Star rating for modal
const modalStars = document.querySelectorAll('#modalRating i');
const modalRatingValue = document.getElementById('modalRatingValue');
if (modalStars && modalRatingValue) {
    modalStars.forEach(star => {
        star.addEventListener('click', function () {
            try {
                let value = this.getAttribute('data-value');
                modalRatingValue.value = value;
                modalStars.forEach(s => {
                    s.classList.remove('fas');
                    s.classList.add('far');
                });
                for (let i = 0; i < value; i++) {
                    modalStars[i].classList.remove('far');
                    modalStars[i].classList.add('fas');
                }
            } catch (error) {
                console.error('Error handling star rating click:', error);
            }
        });
    });
}

// Ensure backdrop is removed when modal is closed
document.addEventListener('DOMContentLoaded', () => {
    const modalElement = document.getElementById('editReviewModal');
    if (modalElement) {
        modalElement.addEventListener('hidden.bs.modal', () => {
            console.log('Modal hidden, removing backdrops');
            const backdrops = document.querySelectorAll('.modal-backdrop');
            backdrops.forEach(backdrop => {
                backdrop.remove();
            });
            // Ensure body classes are reset
            document.body.classList.remove('modal-open');
            document.body.style.overflow = '';
            document.body.style.paddingRight = '';
        });
    }
});