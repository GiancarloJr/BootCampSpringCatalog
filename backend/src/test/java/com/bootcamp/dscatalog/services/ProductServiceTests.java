package com.bootcamp.dscatalog.services;

import com.bootcamp.dscatalog.dto.ProductDTO;
import com.bootcamp.dscatalog.entities.Category;
import com.bootcamp.dscatalog.entities.Product;
import com.bootcamp.dscatalog.repository.CategoryRepository;
import com.bootcamp.dscatalog.repository.ProductRepository;
import com.bootcamp.dscatalog.services.exceptions.DataBaseException;
import com.bootcamp.dscatalog.services.exceptions.ResourceNotFoundException;
import com.bootcamp.dscatalog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;


@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

        @InjectMocks
        private ProductServices service;

        @Mock
        private ProductRepository repository;

        @Mock
        private CategoryRepository categoryRepository;

        private long existingId;
        private long noExistingId;
        private long dependentId;
        private PageImpl<Product> page;
        private Product product;
        private Category category;
        private ProductDTO productDTO;


        @BeforeEach
        void setUp() throws Exception{
                existingId = 1L;
                noExistingId = 1000L;
                dependentId = 4L;
                product = Factory.createProduct();
                page = new PageImpl<>(List.of(product));
                category = Factory.createCategory();
                productDTO = Factory.createProductDTO();

                Mockito.when(repository.save(any())).thenReturn(product);

                Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(product));
                Mockito.when(repository.findById(noExistingId)).thenReturn(Optional.empty());

                Mockito.when(repository.find(any(),any(), any())).thenReturn(page);

                Mockito.when(categoryRepository.getOne(existingId)).thenReturn(category);
                Mockito.when(categoryRepository.getOne(noExistingId)).thenThrow(ResourceNotFoundException.class);

                Mockito.when(repository.getOne(existingId)).thenReturn(product);
                Mockito.when(repository.getOne(noExistingId)).thenThrow(ResourceNotFoundException.class);

                Mockito.when(repository.findAll((Pageable) any())).thenReturn(page);

                Mockito.doNothing().when(repository).deleteById(existingId);

                Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(noExistingId);
                Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
        }

        @Test
        public void findAllPagedShouldReturnPage(){

                PageRequest pageable = PageRequest.of(0,10);
                Page<ProductDTO> result = service.findAllPaged(0L, "",pageable);

                Assertions.assertNotNull(result);

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
        @Test
        public void findByIdShouldFindByIdWhenReturnDTOIdExists(){

                ProductDTO productDTO = service.findById(existingId);

                Assertions.assertEquals(ProductDTO.class, productDTO.getClass());
                Assertions.assertNotNull(productDTO);
                Assertions.assertEquals(productDTO.getId(), existingId);
        }


        @Test
        public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists(){

                Assertions.assertThrows(ResourceNotFoundException.class, () -> {
                        service.findById(noExistingId);
                });
        }
        @Test
        public void updateShouldUpdateWhenIdExists(){

                ProductDTO result = service.update(productDTO);

                Assertions.assertNotNull(productDTO);
                Assertions.assertEquals(result.getClass(), ProductDTO.class);
        }
        @Test
        public void updateShouldUpdateWhenIdDoesNotExists(){

                Assertions.assertThrows(ResourceNotFoundException.class, () -> {
                        service.update(service.findById(noExistingId));
                });
        }


}
