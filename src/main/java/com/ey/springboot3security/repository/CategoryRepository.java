package com.ey.springboot3security.repository;

import com.ey.springboot3security.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Integer> {
}
