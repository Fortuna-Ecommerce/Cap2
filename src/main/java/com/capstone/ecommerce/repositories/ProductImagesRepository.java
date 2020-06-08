package com.capstone.ecommerce.repositories;

import com.capstone.ecommerce.model.ProductImages;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImagesRepository extends JpaRepository<ProductImages, Long> {

//    ProductImages getProductImagesBy(String path);
}
