package posmy.interview.boot.service.book;


import posmy.interview.boot.entity.book.BookBean;
import posmy.interview.boot.entity.book.BookBorrowRecordBean;
import posmy.interview.boot.exception.book.BookBorrowRecordNotFoundException;

public interface BookBorrowService {

	Integer borrowBookUpdateStatus(BookBean borrowBook, Integer customerId);

	boolean isBookAvailable(BookBean borrowBook);

	boolean isBookBorrowRecordActive(Integer bookBorrowRecordId);

	BookBorrowRecordBean updateBookRecordOnReturn(BookBean borrowBook, Integer bookBorrowRecordId, Integer customerId)
			throws BookBorrowRecordNotFoundException;

	BookBorrowRecordBean createBookBorrowRecordBean(BookBorrowRecordBean bookBorrowRecord);

}
