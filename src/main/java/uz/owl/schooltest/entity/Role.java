package uz.owl.schooltest.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String rolename;

    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    private final List<User> users = new ArrayList<>();

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", rolename='" + rolename + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return Objects.equals(id, role.id) &&
                rolename.equals(role.rolename);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, rolename);
    }
}
