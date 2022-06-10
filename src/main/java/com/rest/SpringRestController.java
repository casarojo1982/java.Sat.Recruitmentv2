package com.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.model.User;
import com.sevice.UserService;

@RestController
@RequestMapping(value = "/api/v1")
public class SpringRestController {
	@Autowired
	private UserService UserService;

	@RequestMapping(value = "/get-user-by-id/{id}", method = RequestMethod.GET)
	public ResponseEntity<User> getUserByid(@PathVariable("id") int id) {
		User User = UserService.findUserById(id);
		if (User == null) {
			return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<User>(User, HttpStatus.OK);
	}

	@RequestMapping(value = "/create-user", method = RequestMethod.POST)
	public ResponseEntity<Void> saveUser(@RequestBody User User, UriComponentsBuilder ucBuilder) {

		String aux = validateErrors(User.getName(), User.getEmail(), User.getAddress(), User.getPhone());

		if (aux != "") {
			return new ResponseEntity(aux, HttpStatus.BAD_REQUEST);
		}

		if (UserService.isUserAvailable(User)) {
			System.out.println("A User with name " + User.getName() + " already exist");
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}

		if (User.getUserType().equals("Normal")) {
			if (Double.valueOf(User.getMoney()) > 100) {
				Double percentage = Double.valueOf("0.12");
				// If new user is normal and has more than USD100
				Double gif = Double.valueOf(User.getMoney()) * percentage;
				User.setMoney(User.getMoney() + gif);
			}
			if (Double.valueOf(User.getMoney()) < 100) {
				if (Double.valueOf(User.getMoney()) > 10) {
					Double percentage = Double.valueOf("0.8");
					Double gif = Double.valueOf(User.getMoney()) * percentage;
					User.setMoney(User.getMoney() + gif);
				}
			}
		}
		if (User.getUserType().equals("SuperUser")) {
			if (Double.valueOf(User.getMoney()) > 100) {
				Double percentage = Double.valueOf("0.20");
				Double gif = Double.valueOf(User.getMoney()) * percentage;
				User.setMoney(User.getMoney() + gif);
			}
		}
		if (User.getUserType().equals("Premium")) {
			if (Double.valueOf(User.getMoney()) > 100) {
				Double gif = Double.valueOf(User.getMoney()) * 2;
				User.setMoney(User.getMoney() + gif);
			}
		}

		UserService.saveUser(User);
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/get-user-by-id/{id}").buildAndExpand(User.getId()).toUri());
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/update-user", method = RequestMethod.PUT)
	public ResponseEntity<Void> updateUser(@RequestBody User User) {
		String errors = "";

		String aux = validateErrors(User.getName(), User.getEmail(), User.getAddress(), User.getPhone());

		if (aux != "") {
			return new ResponseEntity(aux, HttpStatus.BAD_REQUEST);
		}

		User currentUser = UserService.findUserById(User.getId());
		if (currentUser == null) {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}

		if (User.getUserType().equals("Normal")) {
			if (Double.valueOf(User.getMoney()) > 100) {
				Double percentage = Double.valueOf("0.12");
				// If new user is normal and has more than USD100
				Double gif = Double.valueOf(User.getMoney()) * percentage;
				User.setMoney(User.getMoney() + gif);
			}
			if (Double.valueOf(User.getMoney()) < 100) {
				if (Double.valueOf(User.getMoney()) > 10) {
					Double percentage = Double.valueOf("0.8");
					Double gif = Double.valueOf(User.getMoney()) * percentage;
					User.setMoney(User.getMoney() + gif);
				}
			}
		}
		if (User.getUserType().equals("SuperUser")) {
			if (Double.valueOf(User.getMoney()) > 100) {
				Double percentage = Double.valueOf("0.20");
				Double gif = Double.valueOf(User.getMoney()) * percentage;
				User.setMoney(User.getMoney() + gif);
			}
		}
		if (User.getUserType().equals("Premium")) {
			if (Double.valueOf(User.getMoney()) > 100) {
				Double gif = Double.valueOf(User.getMoney()) * 2;
				User.setMoney(User.getMoney() + gif);
			}
		}

		UserService.updateUser(User);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@RequestMapping(value = "/delete-user/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> deleteUserByid(@PathVariable("id") int id) {
		User currentUser = UserService.findUserById(id);
		if (currentUser == null) {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}

		UserService.deleteUserById(id);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	public String validateErrors(String name, String email, String address, String phone) {

		String errors = "";

		if (name == null)
			// Validate if Name is null
			errors = "The name is required";
		if (email == null)
			// Validate if Email is null
			errors = errors + " The email is required";
		if (address == null)
			// Validate if Address is null
			errors = errors + " The address is required";
		if (phone == null)
			// Validate if Phone is null
			errors = errors + " The phone is required";

		return errors;
	}

}
