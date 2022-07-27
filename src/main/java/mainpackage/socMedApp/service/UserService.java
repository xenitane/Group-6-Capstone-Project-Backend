package mainpackage.socMedApp.service;

import mainpackage.socMedApp.model.*;
import mainpackage.socMedApp.repository.UserRepository;
import mainpackage.socMedApp.util.Generator;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;
	@Value("${pepper}")
	String pepper;

	public SignUpResponse register(User user) {
		SignUpResponse signUpResponse = new SignUpResponse();
		if (user == null || user.getEmail() == null || user.getUsername() == null || user.getName() == null || user.getPassword() == null) {
			signUpResponse.setStatus(false);
			signUpResponse.setMessage("Some fields are empty.");
			return signUpResponse;
		} else if (userRepository.existsByEmail(user.getEmail())) {
			signUpResponse.setStatus(false);
			signUpResponse.setMessage("User with this email already exists.");
			return signUpResponse;
		} else if (userRepository.existsByUsername(user.getUsername())) {
			signUpResponse.setStatus(false);
			signUpResponse.setMessage("User with this username already exists.");
			return signUpResponse;
		}
		String id = Generator.idGen();
		while (userRepository.existsById(id)) id = Generator.idGen();
		String salt = BCrypt.gensalt();
		String hashedPass = BCrypt.hashpw(user.getPassword() + pepper, salt);
		System.out.println(0 + user.getPassword() + pepper);
		user.setId(id);
		user.setPassword(hashedPass);
		user.setSalt(salt);
		user.setRole(UserRole.END_USER);
		userRepository.save(user);
		signUpResponse.setStatus(true);
		signUpResponse.setUserID(id);
		signUpResponse.setMessage("User Successfully registered");
		return signUpResponse;
	}

	public SignInResponse authenticate(SignInRequest signInRequest) {
		SignInResponse signInResponse = new SignInResponse();
		if (signInRequest == null || signInRequest.getCred() == null || signInRequest.getPassword() == null) {
			signInResponse.setStatus(false);
			signInResponse.setMessage("Some fields are empty.");
			return signInResponse;
		}
		User user = userRepository.findByUsername(signInRequest.getCred()).orElse(userRepository.findByEmail(signInRequest.getCred()).orElse(null));
		if (user == null) {
			signInResponse.setStatus(false);
			signInResponse.setMessage("User with this email/username does not exist.");
			return signInResponse;
		}
		if (BCrypt.hashpw(signInRequest.getPassword() + pepper, user.getSalt()).equals(user.getPassword())) {
			signInResponse.setStatus(true);
			signInResponse.setMessage("Signed in successfully.");
			signInResponse.setUserID(user.getId());
		} else {
			signInResponse.setStatus(false);
			signInResponse.setMessage("Incorrect Password.");
		}
		return signInResponse;
	}

	public ProfileResponse getUser(String username) {
		ProfileResponse profileResponse = new ProfileResponse();
		profileResponse.setProfile(userRepository.findByUsername(username).orElse(null));
		if (profileResponse.getProfile() == null) {
			profileResponse.setStatus(false);
			profileResponse.setMessage("No user with this username.");
		} else {
			profileResponse.setStatus(true);
			profileResponse.setMessage("Here is the profile of user " + username + ".");
		}
		return profileResponse;
	}
}
