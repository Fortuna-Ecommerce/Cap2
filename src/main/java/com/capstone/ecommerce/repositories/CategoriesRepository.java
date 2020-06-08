package com.capstone.ecommerce.repositories;

import com.capstone.ecommerce.model.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface CategoriesRepository extends JpaRepository<Categories, Long> {
    @Transactional
    Categories findById(long id);
}
