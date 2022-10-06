package com.bootcamp.dscatalog.repositories;

import com.bootcamp.dscatalog.entities.Product;
import com.bootcamp.dscatalog.repository.ProductRepository;
import com.bootcamp.dscatalog.services.exceptions.ResourceNotFoundException;
import com.bootcamp.dscatalog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Optional;

@DataJpaTest
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository productRepository;

    private long existingId;
    private long notExistingId;
    private long countTotalProducts;

    @BeforeEach
    void setUp() throws Exception{
          existingId = 1L;
          notExistingId = 30L;
          countTotalProducts = 25L;
    }
    @Test
    public void saveShouldPersistWithAutoIncrementWhenIdIsNull(){

        Product product = Factory.createProduct();
        product.setId(null);

        product = productRepository.save(product);

        Assertions.assertNotNull(product.getId());
        Assertions.assertEquals(countTotalProducts+1, product.getId());

    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExists(){

         productRepository.deleteById(existingId);
         Optional<Product> result = productRepository.findById(existingId);

         //Assertions.assertEquals(result, Optional.empty());
         Assertions.assertFalse(result.isPresent());
    }
    @Test
    public void deleteShouldThrowEmptyResultDataAccessExceptionWhenIdDoesNotExists(){

        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
                    productRepository.deleteById(notExistingId);
                });
    }
    @Test
    public void findByIdShouldFindByIdWhenIdExists(){

        Optional<Product> result = productRepository.findById(existingId);


        Assertions.assertEquals(Product.class, result.get().getClass());
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(result.get().getId(), 1L);
    }
    @Test
    public void findByIdShouldThrowEntityNotFoundWhenIdDoesNotExists(){

        Optional<Product> result = productRepository.findById(notExistingId);

        Assertions.assertTrue(result.isEmpty());
        Assertions.assertFalse(result.isPresent());
    }

}
