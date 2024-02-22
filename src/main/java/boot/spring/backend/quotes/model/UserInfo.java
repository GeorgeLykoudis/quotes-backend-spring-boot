package boot.spring.backend.quotes.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "user_info")
public class UserInfo extends Audit {

  @Column(name = "first_name")
  private String firstName;
  @Column(name = "last_name")
  private String lastName;
  @Column(name = "birth_date")
  private LocalDate birthDate;
  @OneToOne(mappedBy = "userInfo", orphanRemoval = true)
  private User user;

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public LocalDate getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(LocalDate birthDate) {
    this.birthDate = birthDate;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public static UserInfoBuilder builder() {
    return new UserInfoBuilder();
  }

  public static class UserInfoBuilder {
    private final UserInfo instance = new UserInfo();

    public UserInfoBuilder id(Long id) {
      this.instance.setId(id);
      return this;
    }

    public UserInfoBuilder firstName(String firstName) {
      this.instance.firstName = firstName;
      return this;
    }

    public UserInfoBuilder lastName(String lastName) {
      this.instance.lastName = lastName;
      return this;
    }

    public UserInfoBuilder birthDate(LocalDate birthDate) {
      this.instance.birthDate = birthDate;
      return this;
    }

    public UserInfoBuilder createdAt(Instant createdAt) {
      this.instance.setCreatedAt(createdAt);
      return this;
    }

    public UserInfoBuilder lastModifiedAt(Instant lastModifiedAt) {
      this.instance.setLastModifiedAt(lastModifiedAt);
      return this;
    }

    public UserInfo build() {
      return this.instance;
    }
  }
}
