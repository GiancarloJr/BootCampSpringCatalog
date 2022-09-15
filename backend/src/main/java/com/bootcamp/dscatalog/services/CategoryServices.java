package com.bootcamp.dscatalog.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bootcamp.dscatalog.dto.CategoryDTO;
import com.bootcamp.dscatalog.entities.Category;
import com.bootcamp.dscatalog.repository.CategoryRepository;

@Service
public class CategoryServices {
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	public List<CategoryDTO> findAll(){
		
		
		//USANDO STREAM E MAP PARA DTO
		List<Category>  list = categoryRepository.findAll();
		return list.stream().map(cat -> new CategoryDTO(cat)).collect(Collectors.toList());
	
		//USANDO FOREACH PARA DTO
		//List<CategoryDTO>  dto = new ArrayList<>();
//		for (Category category : categoryRepository.findAll()) {
//			dto.add(new CategoryDTO(category));
//		}
//		return dto;
		
	}
	
	public Optional<Category> findById(Long id){
		return categoryRepository.findById(id);
		
	}
	
	

}
