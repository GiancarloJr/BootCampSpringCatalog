package com.bootcamp.dscatalog.dto;

import com.bootcamp.dscatalog.entities.Category;
import com.bootcamp.dscatalog.entities.Product;

import javax.validation.constraints.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ProductDTO {

    private Long id;

    @Size(min = 5, max = 60, message = "Entre 5 e 60 caracteres")
    @NotBlank(message = "Nao pode ser vazio")
    private String name;

    @Positive(message = "Valor deve ser positivo")
    private Double price;

    @NotBlank(message = "Campo requerido")
    private String description;

    private String imgUrl;

    @PastOrPresent(message = "Data do produto nao pode ser futura")
    private Instant date;

    private List<CategoryDTO> categories = new ArrayList<>();

    public ProductDTO() {
    }

    public ProductDTO(Long id, String name, Double price, String description, String imgUrl, Instant date, List<CategoryDTO> categories) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.imgUrl = imgUrl;
        this.date = date;
    }
    public ProductDTO(Product entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.price = entity.getPrice();
        this.description = entity.getDescription();
        this.imgUrl = entity.getImgUrl();
        this.date = entity.getDate();
    }
    public ProductDTO(Product entity, Set<Category> categories) {
        this(entity);
        for(Category cat : categories){
            this.categories.add(new CategoryDTO(cat));
        }
        //categories.forEach(cat -> this.categories.add(new CategoryDTO(cat)));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public List<CategoryDTO> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryDTO> categories) {
        this.categories = categories;
    }
}