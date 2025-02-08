package com.example.shoplaptop.service.impl;

import com.example.shoplaptop.model.Category;
import com.example.shoplaptop.repository.ICategoryRepository;
import com.example.shoplaptop.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService implements ICategoryService {
    @Autowired
    private ICategoryRepository iCategoryRepository;

    @Override
    public List<Category> findAll() {
        return iCategoryRepository.findAll();
    }
}
