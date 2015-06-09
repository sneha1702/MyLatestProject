package frontendUtility;

public class Validate {
	private static String emailRegex = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

	public static String validateEmail(String email) {
		String error = "";
		if (email.isEmpty()) {
			error = "This field is required";
		} else if (email.length() > 32) {
			error = "This field cannot exceed the length of 32 characters.";
		} else if (!email.matches(emailRegex)) {
			error = "Invalid Email entered.";
		}
		return error;
	}

	public static String validatePassword(String password) {
		String error = "";
		if (password.isEmpty()) {
			error = "This field is required";
		} else if (password.length() > 24) {
			error = "This field cannot exceed the length of 24 characters.";
		} 
		return error;
	}
	
	public static String validateMatchPassword(String password, String rPassword){
		String error = "";
		if(!password.equals(rPassword)){
			error = "Password retyped incorrectly.";
		}
		return error;
	}
}
