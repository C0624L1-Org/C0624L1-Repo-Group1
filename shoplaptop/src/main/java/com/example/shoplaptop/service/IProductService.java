package com.example.shoplaptop.service;

import com.example.shoplaptop.model.Category;
import com.example.shoplaptop.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface IProductService {
    Page<Product> findAll(Pageable pageable);
    List<Product> findAll();
    Product getById(Integer id);
    void save(Product product);
    boolean existsByName(String name);
    void delete(Product product);

    long countProducts();

    Page<Product> searchProductByNameAndCategory(String name, String category, Pageable pageable);

    //Filter
    Page<Product> findByFilters(String productName, String categoryName, BigDecimal priceMin, BigDecimal priceMax, Pageable pageable);
    List<Product> findProductsByCategoryId(Long categoryId);
}
