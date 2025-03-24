package diploma.cloudapi.security;

import diploma.cloudapi.entity.UserEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@RequiredArgsConstructor
public class AppUserDetails implements UserDetails {

    private final UserEntity user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles().stream()
                .map(it -> new SimpleGrantedAuthority(it.name()))
                .toList();
    }

    @Override
    public String getPassword() {
        return user.getHashedPass();
    }

    @Override
    public String getUsername() {
        return user.getLogin();
    }
}
