package com.example.shoplaptop.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "varchar(50) not null")
    private String name;

    @Column(columnDefinition = "varchar(200) not null")
    private String description;

    @Column(columnDefinition = "decimal(10,2) not null")
    private BigDecimal price;

    @Column(nullable = false)
    private String image;

    @Column(nullable = false)
    private Integer stock;

    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    @OneToMany(mappedBy="product",cascade=CascadeType.ALL)
    private List<CartItem> cartItemList;

    @OneToMany(mappedBy = "product")
    private List<OrderItem> orderItemList;

    @Column()
    private Integer discount=0;

    public Product() {}

    public Product(Integer id, String name, String description, BigDecimal price, String image, Integer stock, Category category, List<CartItem> cartItemList, List<OrderItem> orderItemList, Integer discount) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
        this.stock = stock;
        this.category = category;
        this.cartItemList = cartItemList;
        this.orderItemList = orderItemList;
        this.discount = discount;
    }

    public List<OrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
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

    public List<CartItem> getCartItemList() {
        return cartItemList;
    }

    public void setCartItemList(List<CartItem> cartItemList) {
        this.cartItemList = cartItemList;
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
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", image='" + image + '\'' +
                ", stock=" + stock +
                ", category=" + category +
                ", cartItemList=" + cartItemList +
                ", orderItemList=" + orderItemList +
                ", discount=" + discount +
                '}';
    }
}
