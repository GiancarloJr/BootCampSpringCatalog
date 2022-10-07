package com.bootcamp.dscatalog.services;

import com.bootcamp.dscatalog.dto.ProductDTO;
import com.bootcamp.dscatalog.entities.Category;
import com.bootcamp.dscatalog.entities.Product;
import com.bootcamp.dscatalog.repository.ProductRepository;
import com.bootcamp.dscatalog.services.exceptions.DataBaseException;
import com.bootcamp.dscatalog.services.exceptions.ResourceNotFoundException;
import com.bootcamp.dscatalog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

        @InjectMocks
        private ProductServices service;

        @Mock
        private ProductRepository repository;

        private long existingId;
        private long noExistingId;
        private long dependentId;
        private PageImpl<Product> page;
        private Product product;


        @BeforeEach
        void setUp() throws Exception{
                existingId = 1L;
                noExistingId = 1000L;
                dependentId = 4L;
                product = Factory.createProduct();
                page = new PageImpl<>(List.of(product));

                Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(product);

                Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(product));

                Mockito.when(repository.findById(noExistingId)).thenReturn(Optional.empty());

                Mockito.when(repository.findAll((org.springframework.data.domain.Pageable)ArgumentMatchers.any())).thenReturn(page);

                Mockito.doNothing().when(repository).deleteById(existingId);
                Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(noExistingId);
                Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
        }

        @Test
        public void deleteShouldDoNothingWhenIdExists(){

                Assertions.assertDoesNotThrow(()->{
                        service.delete(existingId);
                });
                Mockito.verify(repository, Mockito.times(1)).deleteById(existingId);
        }
        @Test
        public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists(){

                Assertions.assertThrows(ResourceNotFoundException.class,()->{
                        service.delete(noExistingId);
                });
                Mockito.verify(repository, Mockito.times(1)).deleteById(noExistingId);
        }
        @Test
        public void deleteShouldThrowDataIntegrityViolationExceptionWhenDependentId(){

                Assertions.assertThrows(DataBaseException.class,()->{
                        service.delete(dependentId);
                });
                Mockito.verify(repository, Mockito.times(1)).deleteById(dependentId);
        }


}
