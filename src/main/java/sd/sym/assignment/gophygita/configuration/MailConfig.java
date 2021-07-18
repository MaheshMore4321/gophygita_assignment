package sd.sym.assignment.gophygita.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Data
@Configuration
public class MailConfig {

	@Value("${mail.smtp.host}")
	private String mailHost;

	@Value("${mail.smtp.port}")
	private String mailPort;

	@Value("${mail.smtp.auth}")
	private String mailAuth;

	@Value("${mail.smtp.senderName}")
	private String mailSenderName;

	@Value("${mail.smtp.username}")
	private String mailUsername;

	@Value("${mail.smtp.password}")
	private String mailPassword;

	@Value("${mail.smtp.starttls.enable}")
	private boolean mailStartTlsEnable;
	
	public Properties getProperties() {
		Properties prop = System.getProperties();
		prop.put("mail.smtp.host", this.mailHost);
		prop.put("mail.smtp.port", this.mailPort);
		prop.put("mail.smtp.auth", this.mailAuth);
		prop.put("mail.smtp.starttls.enable", mailStartTlsEnable);
		return prop;
	}
}