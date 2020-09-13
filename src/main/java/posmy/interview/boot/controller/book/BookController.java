package posmy.interview.boot.controller.book;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import posmy.interview.boot.entity.book.BookBean;
import posmy.interview.boot.entity.book.BookBorrowRecordBean;
import posmy.interview.boot.enums.book.RecordCreationResultEnum;
import posmy.interview.boot.service.book.BookBorrowService;
import posmy.interview.boot.service.book.BookManageService;

@RequestMapping("/book")
@RestController
public class BookController {

	@Autowired
	BookManageService bookManageService;
	
	@Autowired
	BookBorrowService bookBorrowService;
	
	private Logger logger = LoggerFactory.getLogger(BookController.class);
	
	@GetMapping("/find-all")
	public ResponseEntity<List<BookBean>> findAllBooks(){
		try {
			List<BookBean> bookList = bookManageService.findAllBook();
			return new ResponseEntity<>(bookList, HttpStatus.OK);
		}catch(Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/read/{bookId}")
	public ResponseEntity<BookBean> readBook(@PathVariable Integer bookId){
		try {
			BookBean book = bookManageService.readBook(bookId);
			return new ResponseEntity<>(book, HttpStatus.OK);
		}catch(Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/create")
	public ResponseEntity<BookBean> createBook(@RequestBody BookBean bookCreation){
		try {
			bookCreation.setCreatedDate(new Date());
			bookCreation = bookManageService.createBook(bookCreation);
			return new ResponseEntity<>(bookCreation, HttpStatus.OK);
		}catch(Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(bookCreation, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/update")
	public ResponseEntity<Integer> updateBookDetails(@RequestParam(required=true) Integer bookId, @RequestParam(required=false) String bookName, 
			@RequestParam(required=false) String type, @RequestParam(required=false) String description){
		try {
			bookManageService.updateBook(bookId, bookName, type, description);
			return new ResponseEntity<>(bookId, HttpStatus.OK);
		}catch(Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(bookId, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/delete")
	public ResponseEntity<BookBean> deleteBook(@RequestParam(required=true) Integer bookId){
		try {
			BookBean deletedBookBean = bookManageService.deleteBook(bookId);
			return new ResponseEntity<>(deletedBookBean, HttpStatus.OK);
		}catch(Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	@PutMapping("/borrow")
	public ResponseEntity<Map<String, String>> borrowBook(@RequestParam(required=true) Integer customerId, @RequestParam(required=true) Integer bookId){
		try {
			BookBean borrowBook = bookManageService.readBook(bookId);
			Map<String, String> resultMap = new HashMap<>();
			if(borrowBook != null) {
				if(bookBorrowService.isBookAvailable(borrowBook)) {
					Integer borrowRecordId = bookBorrowService.borrowBookUpdateStatus(borrowBook, customerId);
					resultMap.put(RecordCreationResultEnum.SUCCESS.getResult(), String.format("Book borrow record %s created.", borrowRecordId));
				}else {
					resultMap.put(RecordCreationResultEnum.FAIL.getResult(), String.format("Book %s is already borrowed.", bookId));
				}
			}else {
				resultMap.put(RecordCreationResultEnum.FAIL.getResult(), String.format("Book %s not found.", bookId));
			}
			return new ResponseEntity<>(resultMap, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(getBorrowErrorMessage(e.getMessage()) , HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/return")
	public ResponseEntity<Map<String, String>> returnBook(@RequestParam(required=true) Integer bookBorrowRecordId, 
			@RequestParam(required=true) Integer customerId, @RequestParam(required=true) Integer bookId){
		try {
			BookBean borrowBook = bookManageService.readBook(bookId);
			Map<String, String> resultMap = new HashMap<>();
			if(borrowBook != null) {
				if(!bookBorrowService.isBookAvailable(borrowBook)) {
					if(bookBorrowService.isBookBorrowRecordActive(bookBorrowRecordId)) {
						BookBorrowRecordBean bookBorrowRecord = bookBorrowService.updateBookRecordOnReturn(borrowBook, bookBorrowRecordId, customerId);
						if(bookBorrowRecord != null) {
							resultMap.put(RecordCreationResultEnum.SUCCESS.getResult(), String.format("Book %s is returned, record %s is updated.", bookBorrowRecord.getBookId(), bookBorrowRecord.getBookBorrowId()));
						}else {
							resultMap.put(RecordCreationResultEnum.FAIL.getResult(), String.format("Invalid customer id %s. ", customerId));
						}
					}else {
						resultMap.put(RecordCreationResultEnum.FAIL.getResult(), String.format("Book borrow record %s not found.", bookBorrowRecordId));
					}
				}else{
					resultMap.put(RecordCreationResultEnum.FAIL.getResult(), String.format("Book %s is already returned.", bookBorrowRecordId));
				}
			}else {
				resultMap.put(RecordCreationResultEnum.FAIL.getResult(), String.format("Book %s not found.", bookId));
			}
			return new ResponseEntity<>(resultMap, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(getBorrowErrorMessage(e.getMessage()) , HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	private Map<String, String> getBorrowErrorMessage(String errorMessage){
		Map<String, String> resultMap = new HashMap<>();
		resultMap.put(RecordCreationResultEnum.FAIL.getResult(), errorMessage);
		return resultMap;
	}
}
