package com.bootcamp.dscatalog.resources;

import com.bootcamp.dscatalog.dto.ProductDTO;
import com.bootcamp.dscatalog.services.ProductServices;
import com.bootcamp.dscatalog.services.exceptions.ResourceNotFoundException;
import com.bootcamp.dscatalog.tests.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductResource.class)
public class ProductResourceTests {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private ProductServices productServices;

        @Autowired
        private ObjectMapper object;

        private ProductDTO productDTO;
        private PageImpl<ProductDTO> page;
        private Long noExistingId;
        private Long existingId;


    @BeforeEach
    void setUp() throws Exception {

        productDTO = Factory.createProductDTO();
        page = new PageImpl<>(List.of(productDTO));
        existingId = 1L;
        noExistingId = 1000L;



        Mockito.when(productServices.findAllPaged(any())).thenReturn(page);
        Mockito.when(productServices.findById(existingId)).thenReturn(productDTO);
        Mockito.when(productServices.findById(noExistingId)).thenThrow(ResourceNotFoundException.class);
        Mockito.when(productServices.update(any())).thenReturn(productDTO);
        Mockito.when(productServices.update(any())).thenThrow(ResourceNotFoundException.class);

    }
    @Test
    public void findAllShouldReturnPage() throws Exception{

        ResultActions result =
                mockMvc.perform(MockMvcRequestBuilders.get("/products/")
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().is(200));
    }
    @Test
    public void findByIdShouldReturnProductDTOWhenIdExisting() throws Exception{

        ResultActions result =
                mockMvc.perform(MockMvcRequestBuilders.get("/products/{id}", existingId)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.description").exists());
    }
    @Test
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExisting() throws Exception{

        ResultActions result =
                mockMvc.perform(MockMvcRequestBuilders.get("/products/{id}", noExistingId)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }
    @Test
    public void updateShouldReturnProductDTOWhenIDExists() throws Exception{

        String jsonBody = object.writeValueAsString(productDTO);

        ResultActions result =
                mockMvc.perform(MockMvcRequestBuilders.put("/products/{id}", existingId)
                                .content(jsonBody)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)

                );

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.description").exists());
    }
    @Test
    public void updateShouldReturnThrowNotFoundWhenIDExists() throws Exception{

        String jsonBody = object.writeValueAsString(productDTO);

        ResultActions result =
                mockMvc.perform(MockMvcRequestBuilders.put("/products/{id}", noExistingId)
                                .content(jsonBody)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                );
        result.andExpect(status().isNotFound());
    }

}
