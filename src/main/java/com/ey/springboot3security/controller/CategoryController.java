package com.ey.springboot3security.controller;

import com.ey.springboot3security.entity.Category;
import com.ey.springboot3security.entity.Notes;
import com.ey.springboot3security.repository.CategoryRepository;
import com.ey.springboot3security.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    // get all categories
    @GetMapping("/categories/all")
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    // get category by id
    @GetMapping("/categories/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable int id) {
        Optional<Category> category = categoryRepository.findById(id);
        return category.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

//    delete category
    @DeleteMapping("/categories/{id}")
    public ResponseEntity<?> deleteNotes(@PathVariable int id) {
        categoryRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // create category
    @PostMapping("/categories")
    public Category saveCategory(@RequestBody Category category) {
        return categoryService.saveCategory(category);
    }

    // update category
    @PutMapping("/categories/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable int id, @RequestBody Category updatedCategory) {
        Optional<Category> existingCategory = categoryRepository.findById(id);

        if (existingCategory.isPresent()) {
            Category category = existingCategory.get();
            category.setName(updatedCategory.getName());
            category.setNotes(updatedCategory.getNotes());

            Category savedCategory = categoryRepository.save(category);
            return ResponseEntity.ok(savedCategory);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}


