package uz.owl.schooltest.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Guruh {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    private SCenter scenter;

    @OneToMany(mappedBy = "guruh")
    private final List<Student> students = new ArrayList<>();

    @ManyToMany
    private final List<BlockTest> blockTests = new ArrayList<>();

    @Override
    public String toString() {
        return "Guruh{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
