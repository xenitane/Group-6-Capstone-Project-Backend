package ola.training.group6.wallStreet.service;

import mainpackage.socMedApp.model.user.*;
import ola.training.group6.wallStreet.model.user.*;
import ola.training.group6.wallStreet.repository.UserRepository;
import ola.trainingGroup6.wallStreet.model.user.*;
import ola.training.group6.wallStreet.util.Generator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;
	@Value("${pepper}")
	String pepper;

	public Pair<SignUpResponse, HttpStatus> register(User user) {
		SignUpResponse signUpResponse = new SignUpResponse();
		if (user == null) {
			signUpResponse.setStatus(false);
			signUpResponse.setMessage("Empty request.");
			return Pair.of(signUpResponse, HttpStatus.BAD_REQUEST);
		}
		boolean emptyEmail = user.getEmail() == null || user.getEmail().trim().isEmpty();
		boolean emptyUsername = user.getUsername() == null || user.getUsername().trim().isEmpty();
		boolean emptyName = user.getName() == null || user.getName().trim().isEmpty();
		boolean emptyPassword = user.getPassword() == null || user.getPassword().trim().isEmpty();
		if (emptyEmail || emptyUsername || emptyName || emptyPassword) {
			signUpResponse.setStatus(false);
			signUpResponse.setMessage("Empty Parameter(s).");
			return Pair.of(signUpResponse, HttpStatus.BAD_REQUEST);
		} else if (userRepository.existsByEmail(user.getEmail())) {
			signUpResponse.setStatus(false);
			signUpResponse.setMessage("User with this email already exists.");
			return Pair.of(signUpResponse, HttpStatus.UNAUTHORIZED);
		} else if (userRepository.existsByUsername(user.getUsername())) {
			signUpResponse.setStatus(false);
			signUpResponse.setMessage("User with this username already exists.");
			return Pair.of(signUpResponse, HttpStatus.UNAUTHORIZED);
		}
		String id;
		do id = Generator.idGen(); while (userRepository.existsById(id));
		String salt = BCrypt.gensalt();
		String hashedPass = BCrypt.hashpw(user.getPassword() + pepper, salt);
		user.setId(id);
		user.setPassword(hashedPass);
		user.setSalt(salt);
		user.setRole(UserRole.END_USER);
		userRepository.save(user);
		signUpResponse.setStatus(true);
		signUpResponse.setUserId(id);
		signUpResponse.setMessage("User Successfully registered");
		return Pair.of(signUpResponse, HttpStatus.CREATED);
	}

	public Pair<SignInResponse, HttpStatus> authenticate(SignInRequest signInRequest) {
		SignInResponse signInResponse = new SignInResponse();
		if (signInRequest == null) {
			signInResponse.setStatus(false);
			signInResponse.setMessage("Empty Request.");
			return Pair.of(signInResponse, HttpStatus.BAD_REQUEST);
		}
		boolean emptyCred = signInRequest.getCred() == null || signInRequest.getCred().trim().isEmpty();
		boolean emptyPassword = signInRequest.getPassword() == null || signInRequest.getPassword().trim().isEmpty();
		if (emptyCred || emptyPassword) {
			signInResponse.setStatus(false);
			signInResponse.setMessage("Empty parameters.");
			return Pair.of(signInResponse, HttpStatus.BAD_REQUEST);
		}
		User user = userRepository.findByUsername(signInRequest.getCred()).orElse(userRepository.findByEmail(signInRequest.getCred()).orElse(null));
		if (user == null) {
			signInResponse.setStatus(false);
			signInResponse.setMessage("User with this email/username does not exist.");
			return Pair.of(signInResponse, HttpStatus.BAD_REQUEST);
		}
		if (BCrypt.hashpw(signInRequest.getPassword() + pepper, user.getSalt()).equals(user.getPassword())) {
			signInResponse.setStatus(true);
			signInResponse.setMessage("Signed in successfully.");
			signInResponse.setUserId(user.getId());
			return Pair.of(signInResponse, HttpStatus.ACCEPTED);
		} else {
			signInResponse.setStatus(false);
			signInResponse.setMessage("Incorrect Password.");
			return Pair.of(signInResponse, HttpStatus.UNAUTHORIZED);
		}
	}

	public Pair<ProfileResponse, HttpStatus> getUser(String username) {
		ProfileResponse profileResponse = new ProfileResponse();
		if (username == null || username.trim().isEmpty()) {
			profileResponse.setStatus(false);
			profileResponse.setMessage("Looking for a ghost?");
			return Pair.of(profileResponse, HttpStatus.BAD_REQUEST);
		}
		User user = userRepository.findByUsername(username).orElse(null);
		profileResponse.setProfile(user == null ? null : new Profile(user));
		if (profileResponse.getProfile() == null) {
			profileResponse.setStatus(false);
			profileResponse.setMessage("No user with this username.");
			return Pair.of(profileResponse, HttpStatus.NOT_FOUND);
		} else {
			profileResponse.setStatus(true);
			profileResponse.setMessage("Here is the profile of user " + username + ".");
			return Pair.of(profileResponse, HttpStatus.OK);
		}
	}

	public Pair<ProfileHeadResponse, HttpStatus> getUserHead(String username) {
		ProfileHeadResponse profileHeadResponse = new ProfileHeadResponse();
		if (username == null || username.trim().isEmpty()) {
			profileHeadResponse.setStatus(false);
			profileHeadResponse.setMessage("Looking for a ghost?");
			return Pair.of(profileHeadResponse, HttpStatus.BAD_REQUEST);
		}
		User user = userRepository.findByUsername(username).orElse(null);
		profileHeadResponse.setProfileHead(user == null ? null : new ProfileHead(user));
		if (profileHeadResponse.getProfileHead() == null) {
			profileHeadResponse.setStatus(false);
			profileHeadResponse.setMessage("No user with this username.");
			return Pair.of(profileHeadResponse, HttpStatus.NOT_FOUND);
		} else {
			profileHeadResponse.setStatus(true);
			profileHeadResponse.setMessage("Here is the profile of user " + username + ".");
			return Pair.of(profileHeadResponse, HttpStatus.OK);
		}
	}
}
