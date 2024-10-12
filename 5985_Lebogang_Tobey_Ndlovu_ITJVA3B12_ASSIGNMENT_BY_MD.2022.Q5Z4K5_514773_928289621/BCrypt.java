package  Mzanzi.Bank.System; //Password encoding using jBcrypt library in spring boot.

public class BCrypt {

	private Object plaintextPassword;
	String hashedPassword = BCrypt.hashpw(plaintextPassword, BCrypt.gensalt());
	public static Object gensalt() {
		// TODO Auto-generated method stub
		return null;
	}
	public static String hashpw(Object plaintextPassword2, Object gensalt) {
		// TODO Auto-generated method stub
		return null;
	}
	public static boolean checkpw1(String password, String password2) {
		// TODO Auto-generated method stub
		return false;
	}
	public static boolean checkpw(String password, String password2) {
		// TODO Auto-generated method stub
		return false;
	}

public boolean checkPassword(String plaintextPassword, String hashedPassword) {
    return BCrypt.checkpw(plaintextPassword, hashedPassword);
}

public String hashPassword(String plaintextPassword) {
    return BCrypt.hashpw(plaintextPassword, BCrypt.gensalt());
}}