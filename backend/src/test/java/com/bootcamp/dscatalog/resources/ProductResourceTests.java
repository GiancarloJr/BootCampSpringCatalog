package com.bootcamp.dscatalog.resources;

import com.bootcamp.dscatalog.dto.ProductDTO;
import com.bootcamp.dscatalog.services.ProductServices;
import com.bootcamp.dscatalog.services.exceptions.DataBaseException;
import com.bootcamp.dscatalog.services.exceptions.ResourceNotFoundException;
import com.bootcamp.dscatalog.tests.Factory;
import com.bootcamp.dscatalog.tests.TokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductResourceTests {

        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private ProductServices productServices;

        @Autowired
        private TokenUtil tokenUtil;

        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
        @Autowired
        private ObjectMapper object;

        private ProductDTO productDTO;
        private PageImpl<ProductDTO> page;
        private Long noExistingId;
        private Long existingId;
        private Long dependentId;
        private String username;
        private String password;


    @BeforeEach
    void setUp() throws Exception {

        username = "maria@gmail.com";
        password = "123456";
        productDTO = Factory.createProductDTO();
        page = new PageImpl<>(List.of(productDTO));
        existingId = 1L;
        noExistingId = 1000L;
        dependentId = 2L;



        Mockito.when(productServices.findAllPaged(any(), any(), any())).thenReturn(page);
        Mockito.when(productServices.findById(existingId)).thenReturn(productDTO);
        Mockito.when(productServices.findById(noExistingId)).thenThrow(ResourceNotFoundException.class);

        Mockito.when(productServices.update2(eq(existingId),any())).thenReturn(productDTO);
        Mockito.when(productServices.update2(eq(noExistingId),any())).thenThrow(ResourceNotFoundException.class);

        Mockito.when(productServices.save(any())).thenReturn(productDTO);
        Mockito.doNothing().when(productServices).delete(existingId);
        Mockito.doThrow(ResourceNotFoundException.class).when(productServices).delete(noExistingId);
        Mockito.doThrow(DataBaseException.class).when(productServices).delete(dependentId);


    }
    @Test
    public void findAllShouldReturnPage() throws Exception{
        String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);

        ResultActions result =
                mockMvc.perform(MockMvcRequestBuilders.get("/products/")
                        .header("Authorization", "Bearer " + accessToken)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().is(200));
    }
    @Test
    public void findByIdShouldReturnProductDTOWhenIdExisting() throws Exception{

        String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);

        ResultActions result =
                mockMvc.perform(MockMvcRequestBuilders.get("/products/{id}", existingId)
                        .header("Authorization", "Bearer " + accessToken)
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
    public void updateShouldReturnProductDTOWhenIdExists() throws Exception {

        String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);

        String jsonBody = object.writeValueAsString(productDTO);

        ResultActions result =
                mockMvc.perform(MockMvcRequestBuilders.put("/products/{id}", existingId)
                        .header("Authorization", "Bearer " + accessToken)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").exists());
//        result.andExpect(jsonPath("$.name").exists());
//        result.andExpect(jsonPath("$.description").exists());
    }
    @Test
    public void updateShouldReturnThrowNotFoundWhenIDNotExists() throws Exception{

        String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);

        String jsonBody = object.writeValueAsString(productDTO);

        ResultActions result =
                mockMvc.perform(MockMvcRequestBuilders.put("/products/{id}", noExistingId)
                                .header("Authorization", "Bearer " + accessToken)
                                .content(jsonBody)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                );
        result.andExpect(status().isNotFound());
    }
    @Test
    public void deleteShouldDeleteByIdWhenIdExists() throws Exception{

        String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.delete("/products/{id}", existingId)
                .header("Authorization", "Bearer " + accessToken));

        result.andExpect(status().isNoContent());
    }
    @Test
    public void deleteShouldReturnNotFoundByIdWhenIdDoesNotExists() throws Exception{

        String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.delete("/products/{id}", noExistingId)
                .header("Authorization", "Bearer " + accessToken)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }

    @Test
    public void insertShouldInsertProductDTO() throws Exception{

        String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);

        String jsonBody = object.writeValueAsString(productDTO);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/products/")
                .header("Authorization", "Bearer " + accessToken)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
    }



}
