package posmy.interview.boot.exception.book;

public class BookBorrowRecordNotFoundException extends Exception {

	private static final long serialVersionUID = 2370994366785620617L;

	public BookBorrowRecordNotFoundException(String errorMessage) {
		super(errorMessage);
	}
}
