document.addEventListener("DOMContentLoaded", function () {
    let carousel = document.querySelector("#customCarousel");
    let carouselInstance = new bootstrap.Carousel(carousel, {
        interval: 5000,
        ride: "carousel",
        pause: false,
        wrap: true
    });

    // Hiệu ứng hover cho indicators
    let indicators = document.querySelectorAll(".carousel-indicators button");
    indicators.forEach(indicator => {
        indicator.addEventListener("mouseenter", function () {
            this.style.transform = "scale(1.5)";
        });
        indicator.addEventListener("mouseleave", function () {
            if (!this.classList.contains("active")) {
                this.style.transform = "scale(1)";
            }
        });
        indicator.addEventListener("click", function () {
            indicators.forEach(btn => btn.style.transform = "scale(1)");
            this.style.transform = "scale(1.3)";
        });
    });

    // Dừng khi hover vào carousel
    carousel.addEventListener("mouseenter", function () {
        carouselInstance.pause();
    });
    carousel.addEventListener("mouseleave", function () {
        carouselInstance.cycle();
    });
});
