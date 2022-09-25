package com.bootcamp.dscatalog.resources;


import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bootcamp.dscatalog.dto.CategoryDTO;
import com.bootcamp.dscatalog.services.CategoryServices;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/category")
public class CategoryResource {

	@Autowired
	private CategoryServices categoryServices;

	@GetMapping("/")
	public ResponseEntity<Page<CategoryDTO>> findAll(
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "linesPerPage", defaultValue = "12") Integer linesPerPage,
			@RequestParam(value = "direction", defaultValue = "ASC") String direction,
			@RequestParam(value = "orderBy", defaultValue = "name") String orderByName
			) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderByName);
		
		Page<CategoryDTO> list = categoryServices.findAllPaged(pageRequest);
		
		return ResponseEntity.ok().body(list);
	}

	@GetMapping("/{id}")
	public ResponseEntity<CategoryDTO> findById(@PathVariable Long id) {
		return ResponseEntity.ok().body(categoryServices.findById(id));
	}

	@PostMapping("/")
	public ResponseEntity<CategoryDTO> insert(@RequestBody CategoryDTO categoryDTO) {
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(categoryDTO.getId()).toUri();
		return ResponseEntity.ok().body(categoryServices.save(categoryDTO));
	}

	@PutMapping("/{id}")
	public ResponseEntity<CategoryDTO> update(@PathVariable("id") Long id,@RequestBody CategoryDTO categoryDTO) {
		categoryDTO.setId(id);
		return ResponseEntity.ok().body(categoryServices.update(categoryDTO));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
		categoryServices.delete(id);
		return ResponseEntity.noContent().build();
	}
}