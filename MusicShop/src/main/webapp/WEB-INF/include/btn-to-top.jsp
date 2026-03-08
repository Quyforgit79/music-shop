<%-- 
    Document   : btn-to-top
    Created on : Jul 10, 2025, 10:04:22 AM
    Author     : Nguyen Hoang Thai Vinh - CE190384
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">

<style>
    /* Back to Top Button */
        #backToTopBtn {
            position: fixed;
            bottom: 18px;
            right: 12px;
            z-index: 999;
            background: rgba(255, 255, 255, 0.7);
            color: #222;
            border: 1px solid rgba(0, 0, 0, 0.08);
            border-radius: 10px;
            width: 44px;
            height: 44px;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.10);
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 1.5rem;
            cursor: pointer;
            opacity: 0;
            pointer-events: none;
            transition: opacity 0.2s, background 0.2s, color 0.2s, border 0.2s;
            backdrop-filter: blur(2px);
        }

        #backToTopBtn.show {
            opacity: 1;
            pointer-events: auto;
            transform: translateY(0);
        }

        #backToTopBtn:hover {
            background: rgba(34, 34, 34, 0.13);
            color: #111;
            border: 1px solid rgba(0, 0, 0, 0.16);
        }
</style>

<!-- Back to Top Button -->
<button id="backToTopBtn" title="Back to top">
    <i class="fa-solid fa-arrow-turn-up"></i>
</button>
<script>
    const backToTopBtn = document.getElementById('backToTopBtn');
    window.addEventListener('scroll', function () {
        if (window.scrollY > 200) {
            backToTopBtn.classList.add('show');
        } else {
            backToTopBtn.classList.remove('show');
        }
    });
    backToTopBtn.addEventListener('click', function () {
        window.scrollTo({top: 0, behavior: 'smooth'});
    });
</script>
