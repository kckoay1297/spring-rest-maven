package posmy.interview.boot.entity.book;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@NoArgsConstructor
@Table(name = "BOOK_BORROW_RECORD")
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = false)
@Proxy(lazy = false)
public class BookBorrowRecordBean {

	@Id
	@Column(name = "BOOK_BORROW_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer bookBorrowId;
	
	@Column(name = "BOOK_ID")
	private Integer bookId;
	
	@Column(name = "STATUS")
	private String status;
	
	@Column(name = "BORROW_DATE")
	private Date borrowDate;
	
	@Column(name = "RETURN_DATE")
	private Date returnDate;
	
	@Column(name = "CUSTOMER_ID")
	private Integer customerId;

	public BookBorrowRecordBean(Integer bookBorrowId, Integer bookId, String status, Date borrowDate, Date returnDate, Integer customerId) {
		this.setBookBorrowId(bookBorrowId);
		this.setBookId(bookId);
		this.setStatus(status);
		this.setBorrowDate(borrowDate);
		this.setReturnDate(returnDate);
		this.setCustomerId(customerId);
	}
	
}
