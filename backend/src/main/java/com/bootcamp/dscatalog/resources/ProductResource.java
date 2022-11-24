package com.bootcamp.dscatalog.resources;


import com.bootcamp.dscatalog.dto.CategoryDTO;
import com.bootcamp.dscatalog.dto.ProductDTO;
import com.bootcamp.dscatalog.entities.Product;
import com.bootcamp.dscatalog.services.CategoryServices;
import com.bootcamp.dscatalog.services.ProductServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/products")
public class ProductResource {

	@Autowired
	private ProductServices productServices;

//	@GetMapping("/semcategoria")
//	public ResponseEntity<Page<ProductDTO>> findAll(Pageable pageable) {
//		// PARAMETROS:  page, size, sort.
//		Page<ProductDTO> list = productServices.findAllPaged(pageable);
//
//		return ResponseEntity.ok().body(list);
//	}

	//FILTRO COM CATEGORIA
	@GetMapping
	public ResponseEntity<Page<ProductDTO>> findAll(
			@RequestParam(value = "categoryId", defaultValue = "0") Long categoryId,
			@RequestParam(value = "name", defaultValue = "") String name,
			Pageable pageable) {

		Page<ProductDTO> list = productServices.findAllPaged(categoryId, name, pageable);
		return ResponseEntity.ok().body(list);
	}


	@GetMapping("/{id}")
	public ResponseEntity<ProductDTO> findById(@PathVariable Long id) {
		return ResponseEntity.ok().body(productServices.findById(id));
	}

	@PostMapping("/")
	public ResponseEntity<ProductDTO> insert(@Valid @RequestBody ProductDTO productDTO) {
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(productDTO.getId()).toUri();
			return ResponseEntity.ok().body(productServices.save(productDTO));
	}

	@PutMapping("/{id}")
	public ResponseEntity<ProductDTO> update(@PathVariable("id") Long id,@Valid @RequestBody ProductDTO productDTO) {
		return ResponseEntity.ok().body(productServices.update(productDTO));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
		productServices.delete(id);
		return ResponseEntity.noContent().build();
	}
}