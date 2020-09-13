package posmy.interview.boot.exception.user;

public class UserNotFoundException extends Exception {

	private static final long serialVersionUID = 24655634336253583L;

	public UserNotFoundException(String errorMessage) {
		super(errorMessage);
	}
}
