package com.anubhav.dev.store.repositories;

import java.util.List;

import com.anubhav.dev.store.entities.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @EntityGraph(attributePaths = "category")
    List<Product> findByCategoryId(Byte category_id);

    @EntityGraph(attributePaths = "category")
    @Query("SELECT p from Product p")
    List<Product> findAllWithCategory();
}