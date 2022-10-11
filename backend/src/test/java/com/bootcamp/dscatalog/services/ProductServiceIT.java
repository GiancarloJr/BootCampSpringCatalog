package com.bootcamp.dscatalog.services;

import com.bootcamp.dscatalog.dto.ProductDTO;
import com.bootcamp.dscatalog.repository.ProductRepository;
import com.bootcamp.dscatalog.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
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
    @Test
    public void findAllPagedShouldReturnPageWhenPage0Size10(){

        PageRequest page = PageRequest.of(0,10);

        Page<ProductDTO> result = productServices.findAllPaged(page);

        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(result.getTotalElements(), 25L);
        Assertions.assertEquals(0, page.getPageNumber());
        Assertions.assertEquals(10, page.getPageSize());
    }
    @Test
    public void findAllPagedShouldReturnPageWhenPageDoesNotExists(){

        PageRequest page = PageRequest.of(50,10);

        Page<ProductDTO> result = productServices.findAllPaged(page);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void findAllPagedShouldReturnOrdererPageWhenSortByName(){

        PageRequest page = PageRequest.of(0,10, Sort.by("name"));

        Page<ProductDTO> result = productServices.findAllPaged(page);

        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals("Macbook Pro", result.getContent().get(0).getName());
        Assertions.assertEquals(25L, result.getTotalElements());

    }

}
