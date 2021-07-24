package sd.sym.assignment.gophygita.service;

import com.sun.mail.smtp.SMTPTransport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sd.sym.assignment.gophygita.configuration.MailConfig;
import sd.sym.assignment.gophygita.dto.request.EmailRequest;
import sd.sym.assignment.gophygita.utility.Utility;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.UnsupportedEncodingException;

@Slf4j
@Service
public class SendMailProcess extends MailConfig {

	private static final String SENDING_PROTOCOL = "smtp";
	 
	private Session getSession() {
		return Session.getDefaultInstance(getProperties());
	}

	public boolean sendMail(EmailRequest emailRequest) {
		try {
			Session session = getSession();
			Message message = new MimeMessage(session);

			if (null != getMailSenderName()) {
				message.setFrom(new InternetAddress(getMailUsername(), getMailSenderName()));
			} else {
				message.setFrom(new InternetAddress(getMailUsername()));
			}

			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailRequest.getTo()));
 			message.setSubject(emailRequest.getSubject());
			message.setSentDate(Utility.getNowDate());

			Multipart multipart = new MimeMultipart();
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(emailRequest.getBody(), "text/html");
			multipart.addBodyPart(messageBodyPart);

			message.setContent(multipart);
			 
			SMTPTransport sMTPTransport = (SMTPTransport) session.getTransport(SENDING_PROTOCOL);
			sMTPTransport.connect(getMailHost(), getMailUsername(), getMailPassword());
			sMTPTransport.sendMessage(message, message.getAllRecipients());

			log.info("Mail Send Response :: " + sMTPTransport.getLastServerResponse());
			return true;
		} catch (MessagingException | UnsupportedEncodingException e) {
			log.error("Exception while mail send", e);
			return false;
		}
	}
}