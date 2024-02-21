package boot.spring.backend.quotes.model.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static boot.spring.backend.quotes.model.security.Permission.ADMIN_CREATE;
import static boot.spring.backend.quotes.model.security.Permission.ADMIN_DELETE;
import static boot.spring.backend.quotes.model.security.Permission.ADMIN_READ;
import static boot.spring.backend.quotes.model.security.Permission.ADMIN_UPDATE;
import static boot.spring.backend.quotes.model.security.Permission.MANAGER_CREATE;
import static boot.spring.backend.quotes.model.security.Permission.MANAGER_DELETE;
import static boot.spring.backend.quotes.model.security.Permission.MANAGER_READ;
import static boot.spring.backend.quotes.model.security.Permission.MANAGER_UPDATE;

public enum Role {
  USER(Collections.emptySet()),
  ADMIN(Set.of(
      ADMIN_READ,
      ADMIN_UPDATE,
      ADMIN_CREATE,
      ADMIN_DELETE,
      MANAGER_READ,
      MANAGER_UPDATE,
      MANAGER_CREATE,
      MANAGER_DELETE
  )),
  MANAGER(Set.of(
      MANAGER_READ,
      MANAGER_UPDATE,
      MANAGER_CREATE,
      MANAGER_DELETE
  ))

  ;

  private final Set<Permission> permissions;

  Role(Set<Permission> permissions) {
    this.permissions = permissions;
  }

  public Set<Permission> getPermissions() {
    return this.permissions;
  }

  public List<SimpleGrantedAuthority> getAuthorities() {
    List<SimpleGrantedAuthority> authorities = getPermissions()
        .stream()
        .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
        .collect(Collectors.toList());
    authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
    return authorities;
  }
}
