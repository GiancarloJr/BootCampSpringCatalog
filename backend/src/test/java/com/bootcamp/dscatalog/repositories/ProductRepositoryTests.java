package com.bootcamp.dscatalog.repositories;

import com.bootcamp.dscatalog.entities.Product;
import com.bootcamp.dscatalog.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Optional;

@DataJpaTest
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void deleteShouldDeleteObjectWhenIdExists(){

        long existingId = 1L;
         productRepository.deleteById(existingId);
         Optional<Product> result = productRepository.findById(existingId);

         //Assertions.assertEquals(result, Optional.empty());
         Assertions.assertFalse(result.isPresent());
    }
    @Test
    public void deleteShouldThrowEmptyResultDataAccessExceptionWhenIdDoesNotExists(){

        long NotExistingId = 35L;

        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
                    productRepository.deleteById(NotExistingId);
                });
    }
}
