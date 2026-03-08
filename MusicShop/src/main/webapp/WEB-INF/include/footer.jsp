<%-- 
    Document   : footer
    Created on : May 21, 2025, 10:43:52 AM
    Author     : Nguyen Hoang Thai Vinh - CE190384
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<footer class="bg-dark text-white py-4">
    <div class="container">
        <div class="row g-3">
            <!-- Links Section -->
            <div class="col-6 col-md-3">
                <h5 class="mb-3">Quick Links</h5>
                <ul class="list-unstyled">
                    <li><a href="${pageContext.request.contextPath}/home" class="text-white text-decoration-none">Home</a></li>
                    <li><a href="${pageContext.request.contextPath}/products" class="text-white text-decoration-none">Products</a></li>
                    <li><a href="${pageContext.request.contextPath}/order-history" class="text-white text-decoration-none">Order History</a></li>
                    <li><a href="${pageContext.request.contextPath}/about" class="text-white text-decoration-none">About</a></li>
                </ul>
            </div>
            <!-- Contact Section -->
            <div class="col-6 col-md-3">
                <h5 class="mb-3">Contact Us</h5>
                <ul class="list-unstyled">
                    <li>123 Music Street, District 1, Ho Chi Minh City</li>
                    <li>Email: <a href="mailto:support@musicshop.vn" class="text-white text-decoration-none">support@musicshop.vn</a></li>
                    <li>Phone: <a href="tel:+84234567890" class="text-white text-decoration-none">+84 234 567 890</a></li>
                </ul>
            </div>
            <!-- Social Media Section -->
            <div class="col-6 col-md-3">
                <h5 class="mb-3">Follow Us</h5>
                <ul class="list-unstyled">
                    <li><a href="https://facebook.com/musicshop" class="text-white text-decoration-none">Facebook</a></li>
                    <li><a href="https://instagram.com/musicshop" class="text-white text-decoration-none">Instagram</a></li>
                    <li><a href="https://twitter.com/musicshop" class="text-white text-decoration-none">Twitter</a></li>
                </ul>
            </div>
        </div>
        <div class="text-center mt-4">
            <p class="mb-0">© 2025 MusicShop. All rights reserved.</p>
        </div>
    </div>
</footer>