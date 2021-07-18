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
	private List<String> roles;
	private boolean flag;
	private String message;
}