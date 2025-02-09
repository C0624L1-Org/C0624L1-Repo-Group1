package com.example.shoplaptop.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Product {

    @OneToMany(mappedBy="product")
    private List<CartItem> cartItems;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "varchar(50) not null")
    private String name;

    @Column(columnDefinition = "varchar(200) not null")
    private String description;

    @Column(columnDefinition = "double check (price >= 0) not null")
    private Double price;

    @Column(nullable = false)
    private String image;

    @Column(columnDefinition = "int check (stock >= 0) not null")
    private Integer stock;

    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;



    public Product() {}

    public Product(Integer id, String name, String description, Double price, String image, Integer stock, Category category) {
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
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", image='" + image + '\'' +
                ", stock=" + stock +
                ", category=" + category.getName() +
                '}';
    }
}
