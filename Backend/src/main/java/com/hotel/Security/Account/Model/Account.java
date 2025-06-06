package com.hotel.Security.Account.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hotel.Person.Model.Person;
import com.hotel.Security.Token.Model.Token;
import com.hotel.identity_document.Model.Id;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Table(name = "account")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account implements UserDetails {

    @EmbeddedId
    private Id accountId;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JsonIgnore
    private Person person;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private boolean enable;

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Token> tokens;

    @Column(name = "roles")
    private Set<String> roles;

    @Transient
    private Set<GrantedAuthority> authorities = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isEnabled() {
        return enable;
    }

    @Override
    public String getPassword() {
        return password;
    }

}