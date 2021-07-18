package sd.sym.assignment.gophygita.constant;

public class ApplicationConstant {

    private ApplicationConstant(){}

    public static final String APPLICATION_PROPERTIES = "application.properties";

    public static final String DATE_TIME_FORMAT = "yyyy/MM/dd HH:mm:ss";

    public static final String AUTH_REQUEST_MAPPING_URL = "/api/auth";
    public static final String AUTH_LOGIN_URL = "login";
    public static final String AUTH_REGISTRATION_URL = "register";
    public static final String AUTH_VERIFICATION_URL = "mailVerificationUser/{userId}/{emailPasscode}";
    public static final String AUTH_EXIST_USERNAME_URL = "userNameExist";
    public static final String AUTH_CHECK_USERNAME_URL = "checkUserName";
    public static final String AUTH_CHECK_PASSCODE_URL = "checkPasscode";
    public static final String AUTH_CHECK_USERNAME_PASSCODE_URL = "checkUsernamePasscode";
    public static final String AUTH_CHECK_URL = "authenticateRequest";

    public static final String DASHBOARD_URL = "/api/dashBoard";
    public static final String GET_DATA_USER_URL = "user/{userId}";
    public static final String GET_DATA_ADMIN_URL = "admin";
    public static final String TOGGLE_USER_ACTIVE = "toggleUserActive/{userId}";

    public static final String LOGIN_PURPOSE = "LOGIN";
    public static final String FETCH_PURPOSE = "FETCH";
}
