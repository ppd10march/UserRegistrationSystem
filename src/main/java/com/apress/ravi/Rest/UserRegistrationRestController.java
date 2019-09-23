package com.apress.ravi.Rest;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apress.ravi.Exception.CustomErrorType;
import com.apress.ravi.dto.UsersDTO;
import com.apress.ravi.repository.UserJpaRepository;

@RestController
@RequestMapping("/api/user")
public class UserRegistrationRestController {

	public static final Logger logger = LoggerFactory.getLogger(UserRegistrationRestController.class);
	private UserJpaRepository userJpaRepository;
	
	@Autowired
	public void setUserJpaRepository(UserJpaRepository userJpaRepository) {
		this.userJpaRepository = userJpaRepository;
	}
	
	@GetMapping("/")
	public ResponseEntity<List<UsersDTO>> listAllUsers(){
		List<UsersDTO> users = userJpaRepository.findAll();
		if(users.isEmpty()) {
			return new ResponseEntity<List<UsersDTO>> (HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<UsersDTO>> (users, HttpStatus.OK);
	}
	
	
	
	@PostMapping(value="/",consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UsersDTO> createUser(@Valid @RequestBody final UsersDTO user){
		logger.info("Creating User: {}", user);
		if(userJpaRepository.findByName(user.getName())!=null) {
			logger.error("Unable to create. A user with name {} already exists.", user.getName());
			return new ResponseEntity<UsersDTO> (new CustomErrorType("User with name " + user.getName() + " already exists."), HttpStatus.CONFLICT);
		}
		userJpaRepository.save(user);
		return new ResponseEntity<UsersDTO>(user, HttpStatus.CREATED);
	}
	
	
	@GetMapping("/{id}")
	public ResponseEntity<UsersDTO> getUserById(@PathVariable("id") final Long id){
		UsersDTO user = userJpaRepository.findOne(id);
		if(user==null) {
			return new ResponseEntity<UsersDTO> (new CustomErrorType("User with id " + id + " not found"), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<UsersDTO> (user, HttpStatus.OK);
	}
	
	@PutMapping(value="/{id}",consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UsersDTO> updateUser(@PathVariable("id") final Long id, @RequestBody final UsersDTO user){
		UsersDTO currentUser = userJpaRepository.findOne(id);
		if(currentUser == null) {
			return new ResponseEntity<UsersDTO> (new CustomErrorType("Unable to Upate. User with id " + id + " not found"), HttpStatus.NOT_FOUND);
		}
		currentUser.setName(user.getName());
		currentUser.setAddress(user.getAddress());
		currentUser.setEmail(user.getEmail());
		userJpaRepository.saveAndFlush(currentUser);
		return new ResponseEntity<UsersDTO>(currentUser, HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<UsersDTO> deleteUser(@PathVariable("id") final Long id){
		UsersDTO user = userJpaRepository.findOne(id);
		if(user == null) {
			return new ResponseEntity<UsersDTO> (new CustomErrorType("Unable to Delete. User with id " + id + " not found"), HttpStatus.NOT_FOUND);
		}
		userJpaRepository.delete(id);
		
		return new ResponseEntity<UsersDTO> (new CustomErrorType("Deleted user with id " + id + "."),HttpStatus.NO_CONTENT);
	}
	
}
