package mainpackage.socMedApp.service;

import mainpackage.socMedApp.model.*;
import mainpackage.socMedApp.repository.UserRepository;
import mainpackage.socMedApp.util.Generator;
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
		if (userRepository.existsByEmail(user.getEmail())) {
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
		Optional<User> optionalUser = userRepository.findByEmail(signInRequest.getCred());
		if (optionalUser.isEmpty()) optionalUser = userRepository.findByUsername(signInRequest.getCred());
		if (optionalUser.isPresent()) {
			String hashedPass = BCrypt.hashpw(signInRequest.getPassword() + pepper, optionalUser.get().getSalt());
			System.out.println(1 + signInRequest.getPassword() + pepper);
			System.out.println(hashedPass);
			System.out.println(optionalUser.get().getPassword());
			if (hashedPass.equals(optionalUser.get().getPassword())) {
				signInResponse.setStatus(true);
				signInResponse.setMessage("Successfully logged in.");
				signInResponse.setUserID(optionalUser.get().getId());
			} else {
				signInResponse.setStatus(false);
				signInResponse.setMessage("Incorrect password. Try Again.");
				signInResponse.setUserID(null);
			}
		} else {
			signInResponse.setStatus(false);
			signInResponse.setMessage("This user does not exist. Check the credentials or register as new user.");
		}
		return signInResponse;
	}

	public UserResponse getUser(String username) {
		return new UserResponse(userRepository.findByUsername(username).orElse(null));
	}
}
