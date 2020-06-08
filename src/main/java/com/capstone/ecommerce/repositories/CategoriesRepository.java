package com.capstone.ecommerce.repositories;

import com.capstone.ecommerce.model.Categories;
import com.capstone.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CategoriesRepository extends JpaRepository<Categories, Long> {
    @Transactional
    Categories findById(long id);
}
