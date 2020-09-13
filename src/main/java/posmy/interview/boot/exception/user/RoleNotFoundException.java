package posmy.interview.boot.exception.user;

public class RoleNotFoundException extends Exception {

	private static final long serialVersionUID = 3274608311513986969L;
	
	public RoleNotFoundException(String errorMessage) {
		super(errorMessage);
	}

}
