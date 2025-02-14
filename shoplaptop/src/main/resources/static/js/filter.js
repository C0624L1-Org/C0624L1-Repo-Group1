document.getElementById("filterForm").addEventListener("submit", function(event) {
    // Nếu bạn muốn xử lý theo AJAX, hãy ngăn submit form truyền thống
    event.preventDefault();

    // Lấy dữ liệu từ form
    let formData = new FormData(this);

    // Ví dụ: gọi API lấy sản phẩm lọc được (sử dụng fetch)
    fetch(this.action, {
        method: "GET",
        body: formData
    })
        .then(response => response.json())
        .then(data => {
            // Cập nhật danh sách sản phẩm trên trang (bạn cần tự xử lý việc render lại HTML)
            console.log(data);
        })
        .catch(error => console.error("Lỗi:", error));
});
