package gr.jmone.movierama.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gr.jmone.movierama.model.User;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {

  private Long id;
  private String name;
  private String username;
  private String externalId;
  private String email;
  @JsonIgnore private String password;

  private Collection<? extends GrantedAuthority> authorities;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  public static UserDetailsImpl build(User user) {
    List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.getRole()));

    return new UserDetailsImpl(
        user.getId(),
        user.getName(),
        user.getUsername(),
        user.getExternalId().toString(),
        user.getEmail(),
        user.getPassword(),
        authorities);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    UserDetailsImpl user = (UserDetailsImpl) o;
    return Objects.equals(id, user.id);
  }
}
