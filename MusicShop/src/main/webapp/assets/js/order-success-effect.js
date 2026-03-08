/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */
// Simple confetti effect
const canvas = document.querySelector('.confetti');
const ctx = canvas.getContext('2d');
let W = 0, H = 0;
function resize() {
    W = canvas.width = canvas.offsetWidth;
    H = canvas.height = canvas.offsetHeight;
}
window.addEventListener('resize', resize);
resize();

const confettiColors = ['#8b5cf6', '#7c3aed', '#a5b4fc', '#22c55e', '#fbbf24', '#f472b6'];
const confettiPieces = [];
for (let i = 0; i < 32; i++) {
    confettiPieces.push({
        x: Math.random() * W,
        y: Math.random() * -H,
        r: 6 + Math.random() * 8,
        d: Math.random() * W / 2,
        color: confettiColors[Math.floor(Math.random() * confettiColors.length)],
        tilt: Math.random() * 10 - 10,
        tiltAngle: 0,
        tiltAngleIncremental: (Math.random() * 0.07) + .05
    });
}
let confettiInterval = setInterval(drawConfetti, 30);

let stopEmit = false;
// Sau 3 giây, ngừng tạo hiệu ứng mới (stopEmit = true)
setTimeout(function () {
    stopEmit = true;
}, 3000);

function drawConfetti() {
    ctx.clearRect(0, 0, W, H);
    confettiPieces.forEach(function (p) {
        ctx.beginPath();
        ctx.lineWidth = p.r;
        ctx.strokeStyle = p.color;
        ctx.moveTo(p.x + p.tilt + p.r / 3, p.y);
        ctx.lineTo(p.x + p.tilt, p.y + p.tilt + p.r / 3);
        ctx.stroke();
    });
    updateConfetti();
    // Nếu đã stopEmit và tất cả confetti đã rơi khỏi màn hình thì dừng hiệu ứng
    if (stopEmit && confettiPieces.every(p => p.y > H)) {
        clearInterval(confettiInterval);
        ctx.clearRect(0, 0, W, H);
        canvas.style.display = "none";
    }
}
function updateConfetti() {
    confettiPieces.forEach(function (p) {
        if (!stopEmit || p.y <= H) {
            p.y += (Math.cos(p.d) + 3 + p.r / 2) / 2;
            p.tiltAngle += p.tiltAngleIncremental;
            p.tilt = Math.sin(p.tiltAngle - (p.d / 3)) * 15;
            // Nếu chưa stopEmit thì reset lại confetti, nếu đã stopEmit thì không reset nữa
            if (p.y > H && !stopEmit) {
                p.x = Math.random() * W;
                p.y = -10;
            }
        }
    });
}