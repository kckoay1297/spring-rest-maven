package posmy.interview.boot.service.account;

import posmy.interview.boot.entity.account.UserBean;

public interface UserManageService {

	UserBean getUser(Integer userId);

	void deleteUser(Integer userId);

}
