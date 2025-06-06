package com.hotel.Security.Config.Service;

import com.hotel.Security.Account.Enums.Role;
import com.hotel.Security.Account.Model.Account;
import com.hotel.Security.Account.Repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findAccountWithRelationsByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        Set<GrantedAuthority> authorities = new HashSet<>();
        Set<String> roles = new HashSet<>();

        if (account.getPerson().getEmployee() != null && account.getPerson().getEmployee().isActive()) {
            Role.EMPLOYEE.getPermissions().forEach(permission ->
                    authorities.add(new SimpleGrantedAuthority(permission.getPermission()))
            );
            roles.add(Role.EMPLOYEE.name());
            authorities.add((new SimpleGrantedAuthority(Role.EMPLOYEE.getAuthority())));
        }

        if (account.getPerson().getGuest() != null) {
            Role.GUEST.getPermissions().forEach(permission ->
                    authorities.add(new SimpleGrantedAuthority(permission.getPermission()))
            );
            roles.add(Role.GUEST.name());
            authorities.add(new SimpleGrantedAuthority(Role.GUEST.getAuthority()));
        }

        account.setAuthorities(authorities);
        account.setRoles(roles);

        return new User(
                account.getUsername(),
                account.getPassword(),
                account.isEnabled(),
                true,
                true,
                true,
                authorities
        );
    }

}