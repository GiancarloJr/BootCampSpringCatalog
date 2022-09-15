package com.bootcamp.dscatalog.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bootcamp.dscatalog.entities.Category;
import com.bootcamp.dscatalog.services.CategoryServices;

@RestController
@RequestMapping("/category")
public class CategoryResource {

	@Autowired
	private CategoryServices categoryServices;

	@GetMapping("/")
	public ResponseEntity<List<Category>> findAll() {
		return ResponseEntity.ok().body(categoryServices.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Optional<Category>> findById(@PathVariable Long id) {
		return ResponseEntity.ok().body(categoryServices.findById(id));
	}

}