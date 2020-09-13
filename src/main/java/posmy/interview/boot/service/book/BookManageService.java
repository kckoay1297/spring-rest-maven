package posmy.interview.boot.service.book;

import java.util.List;

import posmy.interview.boot.entity.book.BookBean;

public interface BookManageService {

	BookBean createBook(BookBean bookBean);

	BookBean readBook(Integer id);

	void updateBook(Integer id, String bookName, String type, String description);

	BookBean deleteBook(Integer id);

	List<BookBean> findAllBook();

}
