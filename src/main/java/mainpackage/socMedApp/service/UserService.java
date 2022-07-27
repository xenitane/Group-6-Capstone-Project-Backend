package mainpackage.socMedApp.service;

import mainpackage.socMedApp.model.user.*;
import mainpackage.socMedApp.repository.UserRepository;
import mainpackage.socMedApp.util.Generator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

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

	public ProfileResponse getUser(String userId) {
		ProfileResponse profileResponse = new ProfileResponse();
		User user = userRepository.findById(userId).orElse(null);
		profileResponse.setProfile(user == null ? null : new Profile(user));
		if (profileResponse.getProfile() == null) {
			profileResponse.setStatus(false);
			profileResponse.setMessage("No user with this username.");
		} else {
			profileResponse.setStatus(true);
			profileResponse.setMessage("Here is the profile of user " + userId + ".");
		}
		return profileResponse;
	}

	public ProfileHeadResponse getUserHead(String userid) {
		ProfileHeadResponse profileHeadResponse = new ProfileHeadResponse();
		User user = userRepository.findById(userid).orElse(null);
		profileHeadResponse.setProfileHead(user == null ? null : new ProfileHead(user));
		if (profileHeadResponse.getProfileHead() == null) {
			profileHeadResponse.setStatus(false);
			profileHeadResponse.setMessage("No user with this username.");
		} else {
			profileHeadResponse.setStatus(true);
			profileHeadResponse.setMessage("Here is the profile header of user " + userid + ".");
		}
		return profileHeadResponse;
	}
}
