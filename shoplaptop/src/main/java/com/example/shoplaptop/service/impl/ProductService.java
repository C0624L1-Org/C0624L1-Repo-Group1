package com.example.shoplaptop.service.impl;

import com.example.shoplaptop.model.Product;
import com.example.shoplaptop.repository.ICategoryRepository;
import com.example.shoplaptop.repository.IProductRepository;
import com.example.shoplaptop.service.ICategoryService;
import com.example.shoplaptop.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductService implements IProductService {
    @Autowired
    private IProductRepository iProductRepository;

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return iProductRepository.findAll(pageable);
    }

    @Override
    public List<Product> findAll() {
        return iProductRepository.findAll();
    }

    @Override
    public Product getById(Integer id) {
        return iProductRepository.getById(id);
    }

    @Override
    public void save(Product product) {
        iProductRepository.save(product);
    }

    @Override
    public boolean existsByName(String name) {
        return iProductRepository.existsByName(name);
    }

    @Override
    public void delete(Product product) {
        iProductRepository.delete(product);
    }

    @Override
    public long countProducts() {
        return iProductRepository.count();
    }


    @Override
    public Page<Product> searchProductByNameAndCategory(String name, String category, Pageable pageable) {
        return iProductRepository.findByNameContainingAndCategory(name, category, pageable);
    }

    @Override
    public Page<Product> findByFilters(String productName, String categoryName, BigDecimal priceMin, BigDecimal priceMax, Pageable pageable) {
        return iProductRepository.findByFilters(productName, categoryName, priceMin, priceMax, pageable);
    }

    @Override
    public List<Product> findProductsByCategoryId(Long categoryId) {
        return iProductRepository.findProductsByCategoryId(categoryId);
    }

}
