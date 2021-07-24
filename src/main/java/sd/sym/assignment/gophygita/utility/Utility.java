package sd.sym.assignment.gophygita.utility;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;

import static sd.sym.assignment.gophygita.constant.ApplicationConstant.DATE_TIME_FORMAT;

public class Utility {

	private Utility() {}

	public static int getIntProp(String value) {
		try {
			return Integer.parseInt(nullCheck(value));
		} catch (Exception e) {
			return -1;
		}
	}

	public static String nullCheck(String str) {
		return null == str ? "" : str;
	}

	public static String getNowDateString() {
		return DateTimeFormatter.ofPattern(DATE_TIME_FORMAT).format(LocalDateTime.now());
	}

	public static Date getNowDate() {
		return new Date(getNowDateString());
	}

	public static Date getNowDate(Date updatedOn) {
		return (null == updatedOn) ? getNowDate() : updatedOn;
	}

	public static String getUniqueCode() {
		return UUID.randomUUID().toString();
	}

	public static String getUniqueEmailVerificationCode() {
		return getUniqueCode().replace("-","");
	}

	public static boolean isValidEmailId(String emailId) {
		if(null != emailId && !emailId.trim().isEmpty()) {
			return emailId.trim().matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$");
		}
		return false;
	}
}
