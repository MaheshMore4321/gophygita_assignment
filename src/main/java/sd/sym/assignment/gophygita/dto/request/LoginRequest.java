package sd.sym.assignment.gophygita.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

	@NotBlank
	private String username;

	@NotBlank
	private String password;

	public void setUsername(String username) {
		this.username = username.toLowerCase();
	}

	public String getUsername() {
		return this.username.toLowerCase();
	}
}