package com.bootcamp.dscatalog.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bootcamp.dscatalog.entities.Category;
import com.bootcamp.dscatalog.repository.CategoryRepository;

@Service
public class CategoryServices {
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	public List<Category> findAll(){
		return categoryRepository.findAll();
		
	}
	
	public Optional<Category> findById(Long id){
		return categoryRepository.findById(id);
		
	}
	
	

}
