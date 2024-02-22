package boot.spring.backend.quotes.service.user;

import boot.spring.backend.quotes.model.UserInfo;
import boot.spring.backend.quotes.repository.UserInfoRepository;
import org.springframework.stereotype.Service;

@Service
public class UserInfoService {

  private final UserInfoRepository userInfoRepository;

  public UserInfoService(UserInfoRepository userInfoRepository) {
    this.userInfoRepository = userInfoRepository;
  }

  public UserInfo save(UserInfo userInfo) {
    return userInfoRepository.save(userInfo);
  }
}
