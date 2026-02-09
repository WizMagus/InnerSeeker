package exception;

public class AuthException extends RuntimeException{
	private final AuthError error;
	
	public AuthException(AuthError error) {
		super(error.name());
		this.error = error;
	}
	
	public AuthException(AuthError error, String message) {
		super(message);
		this.error = error;
	}
	
	public enum AuthError {
		USERNAME_REQUIRED,
		PASSWORD_REQUIRED,
		USERNAME_TAKEN,
		INVALID_CREDENTIALS
	}
	
	public AuthError getError() {
		return error;
	}
	
	

}
