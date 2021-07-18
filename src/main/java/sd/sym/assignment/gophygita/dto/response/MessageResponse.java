package sd.sym.assignment.gophygita.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {

	private boolean flag;
	private String message;
	private Object object;

	public MessageResponse(boolean flag, String message) {
		this.flag = flag;
		this.message = message;
	}
}
