package com.bootcamp.dscatalog.services;

import com.bootcamp.dscatalog.dto.CategoryDTO;
import com.bootcamp.dscatalog.dto.ProductDTO;
import com.bootcamp.dscatalog.entities.Category;
import com.bootcamp.dscatalog.entities.Product;
import com.bootcamp.dscatalog.repository.CategoryRepository;
import com.bootcamp.dscatalog.repository.ProductRepository;
import com.bootcamp.dscatalog.services.exceptions.DataBaseException;
import com.bootcamp.dscatalog.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;


@Service
public class ProductServices {
	
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private CategoryRepository categoryRepository;

	@Transactional(readOnly = true)
	public Page<ProductDTO> findAllPaged(Pageable pageable){
		//USANDO STREAM E MAP PARA DTO
		Page<Product>  list = productRepository.findAll(pageable);
		return list.map(cat -> new ProductDTO(cat));
	
		//USANDO FOREACH PARA DTO
		//List<ProductDTO>  dto = new ArrayList<>();
//		for (product product : productRepository.findAll()) {
//			dto.add(new ProductDTO(product));
//		}
//		return dto;
	}
	@Transactional(readOnly = true)
	public ProductDTO findById(Long id){
		Optional<Product> obj = productRepository.findById(id);
		Product entity = obj.orElseThrow(()-> new ResourceNotFoundException("Objeto nao encontrado"));
		return new ProductDTO(entity, entity.getCategories());
	}
	@Transactional
	public ProductDTO save(ProductDTO productDTO){
		Product obj = new Product();
		copyDtoToEntity(productDTO, obj);
		return new ProductDTO(productRepository.save(obj));
	}

	private void copyDtoToEntity(ProductDTO dto,Product entity){
		entity.setName(dto.getName());
		entity.setDate(dto.getDate());
		entity.setDescription(dto.getDescription());
		entity.setImgUrl(dto.getImgUrl());
		entity.setPrice(dto.getPrice());

		entity.getCategories().clear();
		for(CategoryDTO catDTO: dto.getCategories()){
			Category category = categoryRepository.getReferenceById(catDTO.getId());
			entity.getCategories().add(category);
		}

	}
	@Transactional
	public ProductDTO update(ProductDTO productDTO){
		try {
			Optional<Product> obj = productRepository.findById(productDTO.getId());
			copyDtoToEntity(productDTO, obj.get());
			return new ProductDTO(productRepository.save(obj.get()));
		} catch (NoSuchElementException e){
			throw new ResourceNotFoundException("Entity not found");
		}
	}
	public void delete(Long id){
		try {
			productRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e){
			throw new ResourceNotFoundException("Entity not found");
		} catch (DataIntegrityViolationException e) {
			throw new DataBaseException("Integraty violation");
		}
	}

//	public ProductDTO entityParaDTO(Product product){
//		return new ProductDTO(product);
//	}


}
