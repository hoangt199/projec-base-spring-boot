package com.example.user.entity;

import com.example.common.model.BaseModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User extends BaseModel implements UserDetails {

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private RoleEntity role;

    @Column(name = "provider")
    @Enumerated(EnumType.STRING)
    private Provider provider;

    @Column(name = "provider_id")
    private String providerId;
    
    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "is_enabled")
    private boolean isEnabled;

    @Column(name = "is_account_non_expired")
    private boolean isAccountNonExpired;

    @Column(name = "is_account_non_locked")
    private boolean isAccountNonLocked;

    @Column(name = "is_credentials_non_expired")
    private boolean isCredentialsNonExpired;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserModuleRole> moduleRoles = new HashSet<>();


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new java.util.ArrayList<>();
        
        // Add role-based authority
        if (role != null) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
            
            // Add permission-based authorities
            if (role.getPermissions() != null) {
                role.getPermissions().forEach(permission -> 
                    authorities.add(new SimpleGrantedAuthority(permission.getName())
                ));
            }
        }
        
        // Add module-based authorities
        if (moduleRoles != null) {
            moduleRoles.forEach(moduleRole -> {
                // Add module-role authority
                authorities.add(new SimpleGrantedAuthority(
                    "MODULE_" + moduleRole.getModule().getModuleKey() + "_ROLE_" + moduleRole.getRole().getName()
                ));
                
                // Add module-permission authorities
                if (moduleRole.getRole().getPermissions() != null) {
                    moduleRole.getRole().getPermissions().forEach(permission -> 
                        authorities.add(new SimpleGrantedAuthority(
                            "MODULE_" + moduleRole.getModule().getModuleKey() + "_" + permission.getName()
                        )
                    ));
                }
            });
        }
        
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
    
    // Business logic methods have been moved to UserService to follow SOLID principles
}