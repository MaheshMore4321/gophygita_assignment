package sd.sym.assignment.gophygita.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

	private Long id;
	private String token;
	private String type = "Bearer";
	private String username;
	private String role;
	private boolean flag;
	private String message;

	public void setUsername(String username) {
		this.username = username.toLowerCase();
	}

	public String getUsername() {
		return this.username.toLowerCase();
	}
}