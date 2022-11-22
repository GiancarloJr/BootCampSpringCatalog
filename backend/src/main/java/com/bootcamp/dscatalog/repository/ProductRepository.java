package com.bootcamp.dscatalog.repository;

import com.bootcamp.dscatalog.entities.Category;
import com.bootcamp.dscatalog.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT DISTINCT obj FROM Product obj INNER JOIN obj.categories cats WHERE " +
            "(:category IS NULL OR :category IN cats) AND " +
            "(LOWER(obj.name) LIKE LOWER(CONCAT('%',:name,'%')) )")
    Page<Product> find(Category category,String name, Pageable pageable);

}
