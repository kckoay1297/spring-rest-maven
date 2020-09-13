package posmy.interview.boot.entity.account;

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
@Table(name = "USER")
@Getter
@Setter
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = false)
@Proxy(lazy = false)
public class UserBean {

	@Id
	@Column(name = "USER_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer userId;
	
	@Column(name = "ACCOUNT_NAME")
	private String accountName;
	
	@Column(name = "ROLE")
	private String role;
	
	@Column(name = "STATUS")
	private String status;
	
	@Column(name = "JOIN_DATE")
	private Date joinDate;
	
	public UserBean(Integer userId, String accountName, String role, String status, Date joinDate) {
		this.setUserId(userId);
		this.setAccountName(accountName);
		this.setRole(role);
		this.setStatus(status);
		this.setJoinDate(joinDate);
	}
}
