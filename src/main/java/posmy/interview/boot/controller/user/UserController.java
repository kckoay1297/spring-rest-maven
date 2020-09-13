package posmy.interview.boot.controller.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import posmy.interview.boot.entity.account.UserBean;
import posmy.interview.boot.enums.role.RoleEnum;
import posmy.interview.boot.exception.user.RoleNotFoundException;
import posmy.interview.boot.exception.user.UserNotFoundException;
import posmy.interview.boot.service.account.UserManageService;

@RequestMapping("/users")
@RestController
public class UserController {

	@Autowired
	UserManageService userManageService;
	
	private Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@GetMapping("/read")
	public ResponseEntity<UserBean> readUser(@RequestParam(required=true) Integer userId){
		try {
			UserBean userBean = userManageService.getUser(userId);
			return new ResponseEntity<>(userBean, HttpStatus.OK);
		}catch(Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/delete")
	public ResponseEntity<Integer> deleteUser(@RequestParam(required=true) Integer userId, @RequestParam(required=true) Integer targetDeleteUserId){
		try {
			if(isAllowedToDeleteUser(userId, targetDeleteUserId)) {
				userManageService.deleteUser(userId);
				return new ResponseEntity<>(userId, HttpStatus.OK);
			}else {
				return new ResponseEntity<>(userId, HttpStatus.UNAUTHORIZED);
			}
		} catch(Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(userId, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	private boolean isAllowedToDeleteUser(Integer userId, Integer targetDeleteUserId) throws RoleNotFoundException, UserNotFoundException {
		UserBean user = userManageService.getUser(userId);
		if(user != null) {
			if(RoleEnum.MEMBER.getRole().equals(user.getRole())) {
				if(userId.equals(targetDeleteUserId)) {
					return true;
				}else {
					return false;
				}
			}else if(RoleEnum.LIBRARIAN.getRole().equals(user.getRole())) {
				return true;
			}else {
				throw new RoleNotFoundException(String.format("Role %s not found.", user.getRole()));
			}
		}else {
			throw new UserNotFoundException(String.format("User %s not found.", userId));
		}

	}
}
