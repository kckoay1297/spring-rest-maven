package posmy.interview.boot.enums.role;

public enum RoleEnum {
	MEMBER("MEMBER"),
	LIBRARIAN("LIBRARIAN");

	private String role;
	
	RoleEnum(String role) {
		this.setRole(role);
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
}
