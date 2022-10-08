package com.bootcamp.dscatalog.tests;

import com.bootcamp.dscatalog.dto.CategoryDTO;
import com.bootcamp.dscatalog.dto.ProductDTO;
import com.bootcamp.dscatalog.entities.Category;
import com.bootcamp.dscatalog.entities.Product;

import java.time.Instant;

public class Factory {

    public static Product createProduct(){
        Product product = new Product(1L,"Phone","GoodPhone",800.0,"https.com.br", Instant.parse("2022-10-20T03:00:00Z"));
        product.getCategories().add(new Category(1L, "Eletrônicos"));
        return product;
    }

    public static ProductDTO createProductDTO(){
        return new ProductDTO(createProduct(), createProduct().getCategories());
    }

    public static Category createCategory(){
        Category category = new Category(1L, "Eletronics");
        return category;
    }
    public static CategoryDTO createCategoryDTO(){
        return new CategoryDTO(createCategory());
    }



}
