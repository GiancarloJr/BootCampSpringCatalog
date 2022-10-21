package com.bootcamp.dscatalog.resources;


import com.bootcamp.dscatalog.dto.ProductDTO;
import com.bootcamp.dscatalog.dto.UserDTO;
import com.bootcamp.dscatalog.dto.UserInsertDTO;
import com.bootcamp.dscatalog.dto.UserUpdateDTO;
import com.bootcamp.dscatalog.services.ProductServices;
import com.bootcamp.dscatalog.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/users")
public class UserResource {

	@Autowired
	private UserServices userServices;

	@GetMapping("/")
	public ResponseEntity<Page<UserDTO>> findAll(Pageable pageable) {
		// PARAMETROS:  page, size, sort.
		Page<UserDTO> list = userServices.findAllPaged(pageable);
		
		return ResponseEntity.ok().body(list);
	}

	@GetMapping("/{id}")
	public ResponseEntity<UserDTO> findById(@PathVariable Long id) {
		return ResponseEntity.ok().body(userServices.findById(id));
	}

	@PostMapping("/")
	public ResponseEntity<UserDTO> insert(@Valid @RequestBody UserInsertDTO userDTO) {
		UserDTO newDto = userServices.save(userDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(userDTO.getId()).toUri();
		return ResponseEntity.created(uri).body(newDto);
	}

	@PutMapping("/{id}")
	public ResponseEntity<UserDTO> update(@PathVariable("id") Long id,@Valid @RequestBody UserUpdateDTO userDTO) {
		userDTO.setId(id);
		return ResponseEntity.ok().body(userServices.update(userDTO));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
		userServices.delete(id);
		return ResponseEntity.noContent().build();
	}
}