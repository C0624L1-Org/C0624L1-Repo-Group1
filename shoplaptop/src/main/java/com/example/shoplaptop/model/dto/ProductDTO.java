package com.example.shoplaptop.model.dto;


import com.example.shoplaptop.model.Category;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.math.BigDecimal;

public class ProductDTO implements Validator {
    private Integer id;
    private String name;
    private String description;
    @NumberFormat(pattern = "############")
    private BigDecimal price;
    @NotNull(message = "This field must not be blank")
    private String image;
    private Integer stock;
    private Category category;
    private Integer discount;

    public ProductDTO() {}

    public ProductDTO(Integer id, String name, String description, BigDecimal price, String image, Integer stock, Category category, Integer discount) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
        this.stock = stock;
        this.category = category;
        this.discount = discount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public BigDecimal getDiscountPrice() {
        if (discount != null && discount > 0) {
            BigDecimal discountPrice = BigDecimal.valueOf(100 - discount).divide(BigDecimal.valueOf(100));
            return price.multiply(discountPrice);
        }
        return price;
    }

    @Override
    public String toString() {
        return "ProductDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", image='" + image + '\'' +
                ", stock=" + stock +
                ", category=" + category.toString() +
                '}';
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {
        ProductDTO product = (ProductDTO) target;

        String name = product.getName();
        if(name.trim().isEmpty()) {
            errors.rejectValue("name", "input.null");
        } else if (name.length() > 50) {
            errors.rejectValue("name", "", "Tên thương hiệu quá dài");
        }

        String description = product.getDescription();
        if(description.trim().isEmpty()) {
            errors.rejectValue("description", "input.null");
        } else if (description.length() > 250) {
            errors.rejectValue("description", "", "Mô tả sản phẩm chứa tối đa 250 ký tự");
        }

        BigDecimal price = product.getPrice();
        if(price == null) {
            errors.rejectValue("price", "input.null");
        } else if (price.compareTo(BigDecimal.ZERO) < 0 || price.compareTo(new BigDecimal("500000000")) > 0) {
            errors.rejectValue("price", "", "Giá trị giá phải nằm trong khoảng từ 0 đến 500000000");
        }

        Integer stock = product.getStock();
        if(stock == null) {
            errors.rejectValue("stock", "input.null");
        } else if (stock < 0) {
            errors.rejectValue("stock", "", "Số lượng không thể bé hơn 0");
        }

        String logo = product.getImage();
        if (logo == null || logo.isEmpty()) {
            errors.rejectValue("image", "input.null");
        } else if (logo.length() > 255) {
            errors.rejectValue("image", "", "URl quá dài");
        }

        Integer discount = product.getDiscount();
        if(discount == null) {
            errors.rejectValue("discount", "input.null");
        } else if (discount < 0 || discount > 100) {
            errors.rejectValue("discount", "", "Không được bé hơn 0% và lớn hơn 100%");
        }
    }
}
