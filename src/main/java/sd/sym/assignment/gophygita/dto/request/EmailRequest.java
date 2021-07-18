package sd.sym.assignment.gophygita.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailRequest {

	private String to;
	private String subject;
	private String body;

	public void parseTo(String name, String userName, String passCode, String link) {
		this.subject = parseTo(this.subject, name, userName, passCode, link);
		this.body = parseTo(this.body, name, userName, passCode, link);
	}

	private String parseTo(String input, String name, String username, String passcode, String link) {
		input = input.replace("$NAME", name);
		input = input.replace("$USERNAME", username);
		input = input.replace("$PASSCODE", passcode);
		input = input.replace("$LINK", link);
		return input;
	}
}