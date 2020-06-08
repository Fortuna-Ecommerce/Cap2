package com.capstone.ecommerce.repositories;

import com.capstone.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

  void save(List<Product> products);
//  Product getOne(long id);

@Transactional
  Product findById(long id);

//  List<Product> findAll(String keyword);

//  Product getProductById(long id);


  //SEARCH METHOD
//  @Query("SELECT p FROM Product p WHERE p.name LIKE %?1%")
  List<Product> findByNameContaining(String keyword);

  @Query(value = "SELECT * FROM products JOIN product_categories on products.id = product_categories.product_id JOIN " +
          "categories c2 on product_categories.category_id = c2.id WHERE c2.category LIKE %:keyword%",
          nativeQuery = true)
  List<Product> findByCategoriesContaining(@Param("keyword") String keyword);

}

//  Product findByTitle(String title);



