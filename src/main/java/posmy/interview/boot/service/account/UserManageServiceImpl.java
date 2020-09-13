package posmy.interview.boot.service.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import posmy.interview.boot.entity.account.UserBean;
import posmy.interview.boot.repository.account.UserRepository;

@Service
@AllArgsConstructor
public class UserManageServiceImpl implements UserManageService {

	@Autowired
	UserRepository userRepository;
	
	@Override
	@Transactional(readOnly=true)
	public UserBean getUser(Integer userId) {
		return userRepository.getOne(userId);
	}
	
	@Override
	@Transactional
	public void deleteUser(Integer userId) {
		userRepository.deleteById(userId);
	}
}
