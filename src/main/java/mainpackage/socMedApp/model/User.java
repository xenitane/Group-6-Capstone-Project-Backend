package mainpackage.socMedApp.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("users")
@Getter
@Setter
public class User extends Profile {
	@Id
	private String id;
	private String email;
	private String password;
	private String salt;
}
