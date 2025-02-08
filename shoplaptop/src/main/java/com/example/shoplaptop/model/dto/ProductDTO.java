package com.example.shoplaptop.model.dto;


import com.example.shoplaptop.model.Category;
import com.example.shoplaptop.model.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class ProductDTO implements Validator {
    private Integer id;
    private String name;
    private String description;
    private Double price;
    @NotNull(message = "This field must not be blank")
    private String image;
    private Integer stock;
    private Category category;

    public ProductDTO() {}

    public ProductDTO(Integer id, String name, String description, Double price, String image, Integer stock, Category category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
        this.stock = stock;
        this.category = category;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
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
            errors.rejectValue("name", "", "The brand name is too long");
        }

        String description = product.getDescription();
        if(description.trim().isEmpty()) {
            errors.rejectValue("description", "input.null");
        } else if (description.length() > 250) {
            errors.rejectValue("description", "", "Product description contains a maximum of 250 characters");
        }

        Double price = product.getPrice();
        if(price == null) {
            errors.rejectValue("price", "input.null");
        } else if (price <= 0 || price > 500000000) {
            errors.rejectValue("price", "", "Invalid price value! Price value must be between 0 and 500000000");
        }

        Integer stock = product.getStock();
        if(stock == null) {
            errors.rejectValue("stock", "input.null");
        } else if (stock < 0) {
            errors.rejectValue("stock", "", "Stock value must be greater than zero");
        }
    }
}
