package posmy.interview.boot.service.book;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import lombok.AllArgsConstructor;
import posmy.interview.boot.entity.book.BookBean;
import posmy.interview.boot.repository.book.BookRepository;

@Service
@AllArgsConstructor
public class BookManageServiceImpl implements BookManageService {

	@Autowired
	BookRepository bookRepository;
	
	@Override
	@Transactional
	public BookBean createBook(BookBean bookBean) {
		return bookRepository.save(bookBean);
	}
	
	@Override
	@Transactional
	public void updateBook(Integer id, String bookName, String type, String description) {
		BookBean currentBook = readBook(id);
		if(currentBook != null) {
			if(!StringUtils.isEmpty(bookName)) {
				currentBook.setBookName(bookName);
			}
			
			if(!StringUtils.isEmpty(type)) {
				currentBook.setType(type);
			}
			
			if(!StringUtils.isEmpty(description)) {
				currentBook.setDescription(description);
			}
		}
	}
	
	@Override
	@Transactional(readOnly = true)
	public BookBean readBook(Integer id) {
		return bookRepository.getOne(id);
	}
	
	@Override
	@Transactional
	public BookBean deleteBook(Integer id) {
		BookBean deleteBookBean = bookRepository.getOne(id);
		bookRepository.delete(deleteBookBean);
		return deleteBookBean;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<BookBean> findAllBook() {
		return bookRepository.findAll();
	}
}
