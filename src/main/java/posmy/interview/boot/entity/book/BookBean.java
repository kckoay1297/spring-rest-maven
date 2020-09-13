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
@Table(name = "BOOK")
@Getter
@Setter
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = false)
@Proxy(lazy = false)
public class BookBean {
	
	@Id
	@Column(name = "BOOK_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer bookId;
	
	@Column(name = "BOOK_NAME")
	private String bookName;
	
	@Column(name = "TYPE")
	private String type;
	
	@Column(name = "STATUS")
	private String status;
	
	@Column(name = "DESCRIPTION")
	private String description;
	
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	public BookBean(Integer bookId, String bookName, String type, String status, String description, Date createdDate) {
		this.setBookId(bookId);
		this.setBookName(bookName);
		this.setType(type);
		this.setStatus(status);
		this.setDescription(description);
		this.createdDate = createdDate;
	}
}
