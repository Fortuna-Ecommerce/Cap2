package com.capstone.ecommerce.repositories;

import com.capstone.ecommerce.model.Product;
import com.capstone.ecommerce.model.ProductImages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductImagesRepository extends JpaRepository<ProductImages, Long> {

//    ProductImages getProductImagesBy(String path);
}
