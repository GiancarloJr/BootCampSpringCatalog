package com.bootcamp.dscatalog.services;

import com.bootcamp.dscatalog.repository.ProductRepository;
import com.bootcamp.dscatalog.services.exceptions.DataBaseException;
import com.bootcamp.dscatalog.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

        @InjectMocks
        private ProductServices service;

        @Mock
        private ProductRepository repository;

        private long existingId;
        private long noExistingId;
        private long dependentId;

        @BeforeEach
        void setUp() throws Exception{
                existingId = 1L;
                noExistingId = 1000L;
                dependentId = 4L;
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
