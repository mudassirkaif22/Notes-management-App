package com.ey.springboot3security.service;

import com.ey.springboot3security.entity.Category;
import com.ey.springboot3security.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category saveCategory(Category category) {
        if (category.getNotes() != null) {
            category.getNotes().forEach(notes -> notes.setCategory(category));
        }
        return categoryRepository.save(category);
    }
}

