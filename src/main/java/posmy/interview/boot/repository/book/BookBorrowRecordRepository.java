package posmy.interview.boot.repository.book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import posmy.interview.boot.entity.book.BookBorrowRecordBean;

@Repository
@Transactional
public interface BookBorrowRecordRepository extends JpaRepository<BookBorrowRecordBean, Integer>{

}
