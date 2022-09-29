package com.bootcamp.dscatalog.resources;


import com.bootcamp.dscatalog.dto.CategoryDTO;
import com.bootcamp.dscatalog.dto.ProductDTO;
import com.bootcamp.dscatalog.entities.Product;
import com.bootcamp.dscatalog.services.CategoryServices;
import com.bootcamp.dscatalog.services.ProductServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/products")
public class ProductResource {

	@Autowired
	private ProductServices productServices;

	@GetMapping("/")
	public ResponseEntity<Page<ProductDTO>> findAll(
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "linesPerPage", defaultValue = "12") Integer linesPerPage,
			@RequestParam(value = "direction", defaultValue = "ASC") String direction,
			@RequestParam(value = "orderBy", defaultValue = "name") String orderByName
			) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderByName);
		
		Page<ProductDTO> list = productServices.findAllPaged(pageRequest);
		
		return ResponseEntity.ok().body(list);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ProductDTO> findById(@PathVariable Long id) {
		return ResponseEntity.ok().body(productServices.findById(id));
	}

	@PostMapping("/")
	public ResponseEntity<ProductDTO> insert(@RequestBody ProductDTO productDTO) {
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(productDTO.getId()).toUri();
		return ResponseEntity.ok().body(productServices.save(productDTO));
	}

	@PutMapping("/{id}")
	public ResponseEntity<ProductDTO> update(@PathVariable("id") Long id,@RequestBody ProductDTO productDTO) {
		productDTO.setId(id);
		return ResponseEntity.ok().body(productServices.update(productDTO));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
		productServices.delete(id);
		return ResponseEntity.noContent().build();
	}
}