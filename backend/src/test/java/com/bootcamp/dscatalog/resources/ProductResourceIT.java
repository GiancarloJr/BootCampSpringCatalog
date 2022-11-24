package com.bootcamp.dscatalog.resources;

import com.bootcamp.dscatalog.dto.ProductDTO;
import com.bootcamp.dscatalog.tests.Factory;
import com.bootcamp.dscatalog.tests.TokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductResourceIT {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private TokenUtil tokenUtil;

        @Autowired
        private ObjectMapper object;

        private ProductDTO productDTO;
        private Long existingId;
        private Long noExistingId;
        private Long countTotalProduct;

        private String adminUsername;
        private String adminPassword;

        @BeforeEach
        void setUp() throws Exception {

                adminUsername = "maria@gmail.com";
                adminPassword = "123456";
                productDTO = Factory.createProductDTO();
                existingId = 1L;
                noExistingId = 1000L;
                countTotalProduct = 25L;

        }

        @Test
        public void findAllShouldReturnSortedPageWhenSortByName() throws Exception{

                String accessToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, adminPassword);

                ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/products/?page=0&size=12&sort=name,asc")
                        .header("Authorization", "Bearer " + accessToken)
                        .accept(MediaType.APPLICATION_JSON));

                result.andExpect(status().isOk());
                result.andExpect(jsonPath("$.totalElements").value(countTotalProduct));
                result.andExpect(jsonPath("$.content").exists());
                result.andExpect(jsonPath("$.content[0].name").value("Macbook Pro"));
        }

        @Test
        public void updateShouldReturnProductDTOWhenUpdate() throws Exception{

                String accessToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, adminPassword);

                String jsonBody = object.writeValueAsString(productDTO);

                String expectedName = productDTO.getName();

                ResultActions result = mockMvc.perform(MockMvcRequestBuilders.put("/products/{id}", existingId)
                                .header("Authorization", "Bearer " + accessToken)
                                .content(jsonBody)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON));

                result.andExpect(status().isOk());
                result.andExpect(jsonPath("$.name").value("Phone"));
                result.andExpect(jsonPath("$.id").value(existingId));
                result.andExpect(jsonPath("$.description").exists());
        }


        @Test
        public void updateShouldReturnNotFoundWhenIdDoesNotExists() throws Exception{

                String accessToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, adminPassword);

                String jsonBody = object.writeValueAsString(productDTO);

                ResultActions result = mockMvc.perform(MockMvcRequestBuilders.put("/products/{id}", noExistingId)
                                .header("Authorization", "Bearer " + accessToken)
                                .content(jsonBody)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON));

                result.andExpect(status().isNotFound());
        }

}
