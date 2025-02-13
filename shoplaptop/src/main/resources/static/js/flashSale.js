document.addEventListener("DOMContentLoaded", function() {
    const slider = document.querySelector('.flash-sale-items');
    const productCards = document.querySelectorAll('.flash-sale-items .product-card');
    
    const cardStyle = getComputedStyle(productCards[0]);
    const cardMarginRight = parseInt(cardStyle.marginRight);
    const cardWidth = productCards[0].offsetWidth + cardMarginRight;

    let currentIndex = 0;
    const totalCards = productCards.length;

    // Clone 5 card đầu tiên
    for (let i = 0; i < 5; i++) {
        let clone = productCards[i].cloneNode(true);
        slider.appendChild(clone);
    }

    setInterval(function() {
        currentIndex++;
        slider.style.transition = 'transform 0.5s ease';
        slider.style.transform = `translateX(-${currentIndex * cardWidth}px)`;

        if (currentIndex >= totalCards) {
            setTimeout(function() {
                slider.style.transition = 'none';
                slider.style.transform = 'translateX(0)';
                currentIndex = 0;
            }, 500);
        }
    }, 3000);
});
