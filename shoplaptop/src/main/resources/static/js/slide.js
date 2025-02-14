document.addEventListener("DOMContentLoaded", function() {
    const slider = document.querySelector('.brand-items');
    const brandCards = document.querySelectorAll('.brand-items .brand-card');

    // Lấy chiều rộng của một card (bao gồm cả margin-right, giả sử .me-3 có khoảng 16px)
    const cardStyle = getComputedStyle(brandCards[0]);
    const cardMarginRight = parseInt(cardStyle.marginRight);
    const cardWidth = brandCards[0].offsetWidth + cardMarginRight;

    let currentIndex = 0;
    const totalCards = brandCards.length;

    // Clone 5 card đầu tiên để tạo hiệu ứng vòng lặp mượt
    for (let i = 0; i < 5; i++) {
        let clone = brandCards[i].cloneNode(true);
        slider.appendChild(clone);
    }

    setInterval(function() {
        currentIndex++;
        slider.style.transition = 'transform 0.5s ease';
        slider.style.transform = `translateX(-${currentIndex * cardWidth}px)`;

        // Khi đã trượt hết số lượng gốc, reset về ban đầu mượt mà
        if (currentIndex >= totalCards) {
            setTimeout(function() {
                slider.style.transition = 'none';
                slider.style.transform = 'translateX(0)';
                currentIndex = 0;
            }, 500);
        }
    }, 3000);
});
