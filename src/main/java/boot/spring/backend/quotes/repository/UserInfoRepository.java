package boot.spring.backend.quotes.repository;

import boot.spring.backend.quotes.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
}
