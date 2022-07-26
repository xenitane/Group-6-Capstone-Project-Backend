package mainpackage.socMedApp.controller;

import mainpackage.socMedApp.model.*;
import mainpackage.socMedApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
	@Autowired
	UserService userService;

	@PostMapping(value = "/signup",consumes = "application/json",produces = "application/json")
	public ResponseEntity<SignUpResponse> signup(@RequestBody User user) {
		System.out.println(user.getPassword());
		SignUpResponse signUpResponse = userService.register(user);
		return new ResponseEntity<>(signUpResponse, signUpResponse.isStatus()    ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST);
	}

	@GetMapping(value="/signin",consumes = "application/json",produces = "application/json")
	public ResponseEntity<SignInResponse> signin(@RequestBody SignInRequest signInRequest){
		SignInResponse signInResponse=userService.authenticate(signInRequest);
		return new ResponseEntity<>(signInResponse,signInResponse.isStatus()?HttpStatus.ACCEPTED:HttpStatus.UNAUTHORIZED);
	}

	@GetMapping(value = "/user/{username}",produces = "application/json")
	public ResponseEntity<UserResponse> getUserInfo(@PathVariable("username") String username){
		UserResponse userResponse=userService.getUser(username);
		return new ResponseEntity<>(userResponse,userResponse.isStatus()?HttpStatus.OK:HttpStatus.NOT_FOUND);
	}
}
