package boot.spring.backend.quotes.model;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class Audit extends BaseEntity {
  @CreatedDate
  @Column(name = "created_at")
  private Instant createdAt;
  @LastModifiedDate
  @Column(name = "last_modified_at")
  private Instant lastModifiedAt;

  public Audit() { super(); }

  public Audit(Instant createdAt, Instant lastModifiedAt) {
    super();
    this.createdAt = createdAt;
    this.lastModifiedAt = lastModifiedAt;
  }

  public Audit(Long id, Instant createdAt, Instant lastModifiedAt) {
    super(id);
    this.createdAt = createdAt;
    this.lastModifiedAt = lastModifiedAt;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }

  public Instant getLastModifiedAt() {
    return lastModifiedAt;
  }

  public void setLastModifiedAt(Instant lastModifiedAt) {
    this.lastModifiedAt = lastModifiedAt;
  }
}
