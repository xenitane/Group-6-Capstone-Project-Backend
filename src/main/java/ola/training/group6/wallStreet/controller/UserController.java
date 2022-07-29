package ola.training.group6.wallStreet.controller;

import ola.training.group6.wallStreet.model.user.*;
import ola.training.group6.wallStreet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class UserController {
	@Autowired
	UserService userService;

	@PostMapping(value = "/signup", consumes = "application/json", produces = "application/json")
	public ResponseEntity<SignUpResponse> signup(@RequestBody User user) {
		Pair<SignUpResponse, HttpStatus> signUpResponse = userService.register(user);
		return new ResponseEntity<>(signUpResponse.getFirst(), signUpResponse.getSecond());
	}

	@GetMapping(value = "/signin", produces = "application/json")
	public ResponseEntity<SignInResponse> signin(@RequestHeader("cred") String cred, @RequestHeader("password") String password) {
		SignInRequest signInRequest = new SignInRequest();
		signInRequest.setCred(cred);
		signInRequest.setPassword(password);
		Pair<SignInResponse, HttpStatus> signInResponse = userService.authenticate(signInRequest);
		return new ResponseEntity<>(signInResponse.getFirst(), signInResponse.getSecond());
	}

	@GetMapping(value = "/user/{username}", produces = "application/json")
	public ResponseEntity<ProfileResponse> getUserInfo(@PathVariable("username") String username) {
		Pair<ProfileResponse, HttpStatus> profileResponse = userService.getUser(username);
		return new ResponseEntity<>(profileResponse.getFirst(), profileResponse.getSecond());
	}

	@GetMapping(value = "/userhead/{username}", produces = "application/json")
	public ResponseEntity<ProfileHeadResponse> getUserHeader(@PathVariable("username") String username) {
		Pair<ProfileHeadResponse, HttpStatus> profileHeadResponse = userService.getUserHead(username);
		return new ResponseEntity<>(profileHeadResponse.getFirst(), profileHeadResponse.getSecond());
	}
}
