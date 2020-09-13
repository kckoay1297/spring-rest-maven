package posmy.interview.boot.repository.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import posmy.interview.boot.entity.account.UserBean;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<UserBean, Integer> {

}
