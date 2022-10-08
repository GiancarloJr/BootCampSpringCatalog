package com.bootcamp.dscatalog.resources;

import com.bootcamp.dscatalog.dto.ProductDTO;
import com.bootcamp.dscatalog.services.ProductServices;
import com.bootcamp.dscatalog.tests.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductResource.class)
public class ProductResourceTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
        private ProductServices productServices;

        private ProductDTO productDTO;

        private PageImpl<ProductDTO> page;

    @BeforeEach
    void setUp() throws Exception {

        productDTO = Factory.createProductDTO();
        page = new PageImpl<>(List.of(productDTO));

        Mockito.when(productServices.findAllPaged(any())).thenReturn(page);

    }
    @Test
    public void findAllShouldReturnPage() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/products/")).andExpect(status().is(200));
    }

}
