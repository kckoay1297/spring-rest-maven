package posmy.interview.boot.enums.book;

public enum RecordCreationResultEnum {
	SUCCESS("success"),
	FAIL("fail");

	private String result;
	
	RecordCreationResultEnum(String result) {
		this.setResult(result);
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
}
