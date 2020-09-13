package posmy.interview.boot.enums.book;

public enum BookBorrowStatusEnum {

	BORROWED("BORROWED"),
	AVAILABLE("AVAILABLE"),
	RETURNED("RETURNED");

	private String status;
	
	BookBorrowStatusEnum(String status) {
		this.setStatus(status);
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
