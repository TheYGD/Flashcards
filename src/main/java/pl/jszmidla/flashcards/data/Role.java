package pl.jszmidla.flashcards.data;

import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public enum Role {
    USER, ADMIN;

    public List<GrantedAuthority> getAuthorities() {
        GrantedAuthority authority = () -> "ROLE_" + name();
        return List.of( authority );
    }
}
