package posmy.interview.boot.service.book;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import posmy.interview.boot.entity.book.BookBean;
import posmy.interview.boot.entity.book.BookBorrowRecordBean;
import posmy.interview.boot.enums.book.BookBorrowStatusEnum;
import posmy.interview.boot.exception.book.BookBorrowRecordNotFoundException;
import posmy.interview.boot.repository.book.BookBorrowRecordRepository;
import posmy.interview.boot.repository.book.BookRepository;

@Service
@AllArgsConstructor
public class BookBorrowServiceImpl implements BookBorrowService {

	@Autowired
	BookRepository bookRepository;
	
	@Autowired
	BookBorrowRecordRepository bookBorrowRecordRepository;
	
	@Override
	@Transactional(readOnly=true)
	public boolean isBookAvailable(BookBean borrowBook) {
		if(BookBorrowStatusEnum.AVAILABLE.getStatus().equals(borrowBook.getStatus())) {
			return true;
		}else if(BookBorrowStatusEnum.BORROWED.getStatus().equals(borrowBook.getStatus())) {
			return false;
		}else {
			return false;
		}
	}
	
	@Override
	@Transactional
	public Integer borrowBookUpdateStatus(BookBean borrowBook, Integer customerId) {
		borrowBook.setStatus(BookBorrowStatusEnum.BORROWED.getStatus());
		bookRepository.save(borrowBook);
		
		BookBorrowRecordBean bookBorrowRecord = createBookBorrowRecord(borrowBook, customerId);
		return bookBorrowRecord.getBookId();
	}
	
	private BookBorrowRecordBean createBookBorrowRecord(BookBean borrowBook, Integer customerId) {
		BookBorrowRecordBean bookBorrowRecord = new BookBorrowRecordBean();
		bookBorrowRecord.setBookId(borrowBook.getBookId());
		bookBorrowRecord.setBorrowDate(new Date());
		bookBorrowRecord.setCustomerId(customerId);
		bookBorrowRecord.setStatus(BookBorrowStatusEnum.BORROWED.getStatus());
		bookBorrowRecordRepository.save(bookBorrowRecord);
		
		return bookBorrowRecord;
	}
	
	@Override
	@Transactional(readOnly=true)
	public boolean isBookBorrowRecordActive(Integer bookBorrowRecordId) {
		BookBorrowRecordBean bookBorrowRecord = bookBorrowRecordRepository.getOne(bookBorrowRecordId);
		if(bookBorrowRecord == null) {
			return false;
		}
		
		if(BookBorrowStatusEnum.BORROWED.getStatus().equals(bookBorrowRecord.getStatus())) {
			return true;
		}else {
			return false;
		}
	}
	
	@Override
	@Transactional
	public BookBorrowRecordBean updateBookRecordOnReturn(BookBean borrowBook, Integer bookBorrowRecordId, Integer customerId) throws BookBorrowRecordNotFoundException {
		BookBorrowRecordBean bookBorrowRecord = bookBorrowRecordRepository.getOne(bookBorrowRecordId);
		if(bookBorrowRecord != null) {
			if(customerId.equals(bookBorrowRecord.getCustomerId())) {
				bookBorrowRecord.setStatus(BookBorrowStatusEnum.AVAILABLE.getStatus());
				bookBorrowRecord.setReturnDate(new Date());
				return bookBorrowRecordRepository.save(bookBorrowRecord);
			}else {
				return null;
			}

		}else {
			throw new BookBorrowRecordNotFoundException(String.format("Book borrow record %s not found.", bookBorrowRecordId));
		}
	}
	
	@Override
	@Transactional
	public BookBorrowRecordBean createBookBorrowRecordBean(BookBorrowRecordBean bookBorrowRecord) {
		return bookBorrowRecordRepository.save(bookBorrowRecord);
	}
}
