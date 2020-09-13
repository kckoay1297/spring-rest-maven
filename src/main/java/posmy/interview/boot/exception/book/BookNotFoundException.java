package posmy.interview.boot.exception.book;

public class BookNotFoundException extends Exception {

	private static final long serialVersionUID = -5244560031750196244L;

	public BookNotFoundException(String errorMessage) {
		super(errorMessage);
	}
}
