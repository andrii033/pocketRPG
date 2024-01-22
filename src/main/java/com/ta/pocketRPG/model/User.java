package com.ta.pocketRPG.model;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@Entity
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;
    private String password;
    private String email;

    @Transient
    private String repassword;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<GameCharacter> characters = new ArrayList<>();


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Set to true to allow logins even if the account is expired
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Set to true to allow logins even if the account is locked
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Set to true to allow logins even if credentials are expired
    }

    @Override
    public boolean isEnabled() {
        return true; // Set to true to enable the user account
    }
}
