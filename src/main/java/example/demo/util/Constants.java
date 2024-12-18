package example.demo.util;

public class Constants {
    public static final String USER_NAME_REGEXP = "^[a-zA-Z0-9_.-]{3,20}$";
    public static final String USER_NAME_ERROR_MESSAGE =
            "Username must be 3 to 20 characters long and can only contain letters, digits, underscores, hyphens, and dots";

    public static final String PASSWORD_REGEXP =
            "^(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{6,64}$";
    public static final String PASSWORD_ERROR_MESSAGE =
            "Password must be 6-64 characters long, contain at least one uppercase letter, one lowercase letter, " +
                    "one digit, and one special character";

    public static final String ACCESS_TOKEN = "accessToken";
}
