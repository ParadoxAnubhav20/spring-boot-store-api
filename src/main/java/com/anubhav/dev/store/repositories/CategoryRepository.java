package com.anubhav.dev.store.repositories;

import com.anubhav.dev.store.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


public interface CategoryRepository extends JpaRepository<Category, Byte> {
}