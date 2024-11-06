package com.funddfuture.fund_d_future.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.funddfuture.fund_d_future.user.Permission.*;


@Getter
@RequiredArgsConstructor
public enum Role {

  USER(
            Set.of(
                    USER_READ,
                    USER_UPDATE,
                    USER_CREATE,
                    USER_DELETE
            )
  ),
  ADMIN(
          Set.of(
                  ADMIN_READ,
                  ADMIN_UPDATE,
                  ADMIN_DELETE,
                  ADMIN_CREATE,
                  USER_READ,
                  USER_UPDATE,
                  USER_CREATE,
                  USER_DELETE,
                  FUNDER_READ,
                  FUNDER_UPDATE,
                  FUNDER_CREATE,
                  FUNDER_DELETE
          )
  ),
  FUNDER(
          Set.of(
                  FUNDER_READ,
                  FUNDER_UPDATE,
                  FUNDER_CREATE,
                  FUNDER_DELETE
          )
  )

  ;

  private final Set<Permission> permissions;

  public List<SimpleGrantedAuthority> getAuthorities() {
    var authorities = getPermissions()
            .stream()
            .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
            .collect(Collectors.toList());
    authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
    return authorities;
  }
}
