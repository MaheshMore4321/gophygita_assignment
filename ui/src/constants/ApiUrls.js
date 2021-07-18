export const HEADER_DATA = (token) => {
    return { headers: {Accept: "application/json", Authorization: token} };
};

export const ROOT_URL = "/api/auth";

export const REGISTER_USER = ROOT_URL + "/register";
export const LOGIN_USER = ROOT_URL + "/login";
export const USER_IS_EXIST = ROOT_URL + "/userNameExist";
export const FETCH_CHECK_USER = ROOT_URL + "/checkUserName";
export const FETCH_PASSCODE_VALIDATE_USER = ROOT_URL + "/checkUsernamePasscode";
export const AUTH_CHECK_URL = ROOT_URL + "/authenticateRequest";

export const ROOT_DASH_URL = "/api/dashBoard";
export const USER_PANEL = ROOT_DASH_URL + "/user/";
export const ADMIN_PANEL = ROOT_DASH_URL + "/admin";
export const TOGGLE_ACTIVE_USER = ROOT_DASH_URL + "/toggleUserActive/";