package com.bootcamp.dscatalog.services;

import com.bootcamp.dscatalog.repository.ProductRepository;
import com.bootcamp.dscatalog.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ProductServiceIT {

    @Autowired
    private ProductServices productServices;

    @Autowired
    private ProductRepository productRepository;

    private Long existingId;
    private Long noExistingId;
    private Long countTotalProduct;

    @BeforeEach
    void setUp() throws Exception {

        existingId = 1L;
        noExistingId = 1000L;
        countTotalProduct = 25L;

    }

    @Test
    public void deleteShouldDeleteResourceWhenIdExists(){

        productServices.delete(existingId);

        Assertions.assertEquals(countTotalProduct-1, productRepository.count());
    }
    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists(){

        Assertions.assertThrows(ResourceNotFoundException.class, ()-> {
            productServices.delete(noExistingId);
        });
    }

}
