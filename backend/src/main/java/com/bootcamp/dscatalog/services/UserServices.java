package com.bootcamp.dscatalog.services;

import com.bootcamp.dscatalog.dto.*;
import com.bootcamp.dscatalog.entities.Category;
import com.bootcamp.dscatalog.entities.Role;
import com.bootcamp.dscatalog.entities.User;
import com.bootcamp.dscatalog.repository.CategoryRepository;
import com.bootcamp.dscatalog.repository.RoleRepository;
import com.bootcamp.dscatalog.repository.UserRepository;
import com.bootcamp.dscatalog.services.exceptions.DataBaseException;
import com.bootcamp.dscatalog.services.exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;


@Service
public class UserServices implements UserDetailsService {

	private static Logger logger = LoggerFactory.getLogger(UserServices.class);

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Transactional(readOnly = true)
	public Page<UserDTO> findAllPaged(Pageable pageable){
		//USANDO STREAM E MAP PARA DTO
		Page<User>  list = userRepository.findAll(pageable);
		return list.map(cat -> new UserDTO(cat));
	
		//USANDO FOREACH PARA DTO
		//List<UserDTO>  dto = new ArrayList<>();
//		for (User User : UserRepository.findAll()) {
//			dto.add(new UserDTO(User));
//		}
//		return dto;
	}
	@Transactional(readOnly = true)
	public UserDTO findById(Long id){
		Optional<User> obj = userRepository.findById(id);
		User entity = obj.orElseThrow(()-> new ResourceNotFoundException("Objeto nao encontrado"));
		return new UserDTO(entity);
	}
	@Transactional
	public UserDTO save(UserInsertDTO userDTO){
		User entity = new User();
		copyDtoToEntity(userDTO, entity);
		entity.setPassword(passwordEncoder.encode(userDTO.getPassword()));
		return new UserDTO(userRepository.save(entity));
	}

	private void copyDtoToEntity(UserDTO userDTO,User entity){
		entity.setFirstName(userDTO.getFirstName());
		entity.setLastName(userDTO.getLastName());
		entity.setEmail(userDTO.getEmail());

		entity.getRoles().clear();
		for(RoleDTO roleDTO: userDTO.getRoles()){
			Role role = roleRepository.getReferenceById(roleDTO.getId());
			entity.getRoles().add(role);
		}
	}

	@Transactional
	public UserDTO update(UserUpdateDTO UserDTO){
		try {
			Optional<User> obj = userRepository.findById(UserDTO.getId());
			copyDtoToEntity(UserDTO, obj.get());
			return new UserDTO(userRepository.save(obj.get()));
		} catch (NoSuchElementException e){
			throw new ResourceNotFoundException("Entity not found");
		}
	}

	public void delete(Long id){
		try {
			userRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e){
			throw new ResourceNotFoundException("Entity not found");
		} catch (DataIntegrityViolationException e) {
			throw new DataBaseException("Integraty violation");
		}
	}

	@Override
	public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(userEmail);
		if(user == null){
			logger.error("User not found"+ userEmail);
			throw new UsernameNotFoundException("Email not found");
		}else
			logger.info("User found: "+userEmail);
			return user;
	}
}
