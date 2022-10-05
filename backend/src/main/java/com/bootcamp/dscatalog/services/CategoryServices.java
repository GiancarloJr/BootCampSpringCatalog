package com.bootcamp.dscatalog.services;

import java.util.NoSuchElementException;
import java.util.Optional;

import com.bootcamp.dscatalog.services.exceptions.DataBaseException;
import com.bootcamp.dscatalog.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.bootcamp.dscatalog.dto.CategoryDTO;
import com.bootcamp.dscatalog.entities.Category;
import com.bootcamp.dscatalog.repository.CategoryRepository;
import org.springframework.transaction.annotation.Transactional;


@Service
public class CategoryServices {
	
	@Autowired
	private CategoryRepository categoryRepository;

	@Transactional(readOnly = true)
	public Page<CategoryDTO> findAllPaged(Pageable pageable){
		//USANDO STREAM E MAP PARA DTO
		Page<Category>  list = categoryRepository.findAll(pageable);
		return list.map(cat -> new CategoryDTO(cat));
	
		//USANDO FOREACH PARA DTO
		//List<CategoryDTO>  dto = new ArrayList<>();
//		for (Category category : categoryRepository.findAll()) {
//			dto.add(new CategoryDTO(category));
//		}
//		return dto;
	}
	@Transactional(readOnly = true)
	public CategoryDTO findById(Long id){
		Optional<Category> obj = categoryRepository.findById(id);
		Category entity = obj.orElseThrow(()-> new ResourceNotFoundException("Objeto nao encontrado"));
		return entityParaDTO(entity);
		
	}

	public CategoryDTO save(CategoryDTO categoryDTO){
		Category obj = new Category();
		obj.setName(categoryDTO.getName());
		return entityParaDTO(categoryRepository.save(obj));
	}



	public CategoryDTO update(CategoryDTO categoryDTO){
		try {
			Optional<Category> obj = categoryRepository.findById(categoryDTO.getId());
			obj.get().setName(categoryDTO.getName());
			return entityParaDTO(categoryRepository.save(obj.get()));
		} catch (NoSuchElementException e){
			throw new ResourceNotFoundException("Entity not found");
		}
	}
	public void delete(Long id){
		try {
			categoryRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e){
			throw new ResourceNotFoundException("Entity not found");
		} catch (DataIntegrityViolationException e) {
			throw new DataBaseException("Integraty violation");
		}
	}

	public CategoryDTO entityParaDTO(Category category){
		return new CategoryDTO(category.getId(), category.getName());
	}


}
