<%-- 
    Document   : unfind-product
    Created on : Jul 10, 2025, 11:35:47 AM
    Author     : Nguyen Hoang Thai Vinh - CE190384
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
<style>

    .not-found-container {
        background: #fff;
        border-radius: 22px;
        box-shadow: 0 8px 32px rgba(139, 92, 246, 0.13), 0 2px 12px rgba(0,0,0,0.06);
        padding: 48px 36px 36px 36px;
        text-align: center;
        max-width: 80%;
        margin: 60px auto;
        animation: fadeIn 0.7s cubic-bezier(.4,2,.6,1);
    }
    @keyframes fadeIn {
        from {
            opacity: 0;
            transform: translateY(40px);
        }
        to {
            opacity: 1;
            transform: none;
        }
    }
    .not-found-img {
        width: 140px;
        height: 140px;
        margin: 0 auto 18px auto;
        display: flex;
        align-items: center;
        justify-content: center;
        background: linear-gradient(135deg, #ede9fe 60%, #f3f0ff 100%);
        box-shadow: 0 2px 12px rgba(139,92,246,0.08);

        position: relative;
    }
    .not-found-img img {
        width: 350px;
        object-fit: contain;
        opacity: 0.95;
        z-index: 1;
    }
    .not-found-img::after {
        content: "\f187";
        font-family: "Font Awesome 6 Free";
        font-weight: 900;
        color: #c4b5fd;
        font-size: 2.8rem;
        position: absolute;
        right: 18px;
        bottom: 10px;
        opacity: 0.18;
        pointer-events: none;
    }
    .not-found-title {
        margin-top: 40px;
        font-size: 1.45rem;
        font-weight: 800;
        color: #ff0000;
        margin-bottom: 8px;
        letter-spacing: 0.2px;
    }
    .not-found-desc {
        color: #555;
        font-size: 1.08rem;
        margin-bottom: 30px;
        line-height: 1.6;
    }
    .back-btn {
        background: linear-gradient(90deg, #ede9fe 0%, #c7d2fe 100%);
        color: #7c3aed;
        border: none;
        border-radius: 12px;
        padding: 13px 32px;
        font-size: 1.08rem;
        font-weight: 700;
        cursor: pointer;
        transition: background 0.18s, color 0.18s, box-shadow 0.18s, transform 0.13s;
        box-shadow: 0 2px 8px rgba(139,92,246,0.08);
        display: inline-flex;
        align-items: center;
        gap: 9px;
    }
    .back-btn:hover, .back-btn:focus {
        background: linear-gradient(90deg, #8b5cf6 0%, #7c3aed 100%);
        color: #fff;
        box-shadow: 0 6px 24px rgba(139,92,246,0.18);
        transform: translateY(-2px) scale(1.04);
    }
    @media (max-width: 500px) {
        .not-found-container {
            padding: 32px 8vw 24px 8vw;
            max-width: 98vw;
        }
        .not-found-img {
            width: 90px;
            height: 90px;
        }
        .not-found-img img {
            width: 60px;
            height: 60px;
        }
    }
</style>

<div class="not-found-container">
    <div class="not-found-img">
        <img src="${pageContext.request.contextPath}/image/not_found_product.png" alt="Not found">
    </div>
    <div class="not-found-title">Product Not Found</div>
    <div class="not-found-desc">
        Sorry, we couldn't find any products matching your search.<br>
        Please try again or go back to the previous page.
    </div>
    <button class="back-btn" onclick="window.history.back()">
        <i class="fa-solid fa-arrow-left"></i> Go Back
    </button>
</div>

<script>
    window.onload = function () {
        var nf = document.querySelector('.not-found-container');
        if (nf)
            nf.scrollIntoView({behavior: "smooth", block: "center"});
    };
</script>
