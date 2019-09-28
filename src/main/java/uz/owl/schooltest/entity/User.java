package uz.owl.schooltest.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Pattern(regexp = "[\\w_.!@#$%^&*+-~]{3,255}")
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Pattern(regexp = "[\\w_.!@#$%^&*+-~]{3,255}")
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "enable", nullable = false)
    @ColumnDefault("true")
    private boolean enable;


    @ManyToMany(mappedBy = "users", fetch = FetchType.EAGER)
    private final List<Role> roles = new ArrayList<>();

    @OneToMany(mappedBy = "author", cascade = {CascadeType.MERGE, CascadeType.REMOVE, CascadeType.PERSIST})
    private final List<SCenter> sCenters = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(r -> new SimpleGrantedAuthority(r.getRolename())).collect(Collectors.toList());
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
        return enable;
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", enable=" + enable +
                '}';
    }
}
