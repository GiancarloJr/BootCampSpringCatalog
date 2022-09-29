package com.bootcamp.dscatalog.services;

import com.bootcamp.dscatalog.dto.ProductDTO;
import com.bootcamp.dscatalog.entities.Product;
import com.bootcamp.dscatalog.repository.ProductRepository;
import com.bootcamp.dscatalog.services.exceptions.DataBaseException;
import com.bootcamp.dscatalog.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;


@Service
public class ProductServices {
	
	@Autowired
	private ProductRepository productRepository;

	@Transactional(readOnly = true)
	public Page<ProductDTO> findAllPaged(PageRequest pageRequest){
		//USANDO STREAM E MAP PARA DTO
		Page<Product>  list = productRepository.findAll(pageRequest);
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

	public ProductDTO save(ProductDTO productDTO){
		Product obj = new Product();
		//obj.setName(productDTO.getName());
		return new ProductDTO(productRepository.save(obj));
	}
	public ProductDTO update(ProductDTO ProductDTO){
		try {
			Optional<Product> obj = productRepository.findById(ProductDTO.getId());
			//obj.get().setName(ProductDTO.getName());
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
