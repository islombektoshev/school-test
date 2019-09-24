package uz.owl.schooltest.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "subject", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "scenter_id"}))
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Override
    public String toString() {
        return "Subject{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Column(name = "primary_subject", nullable = false)
    @ColumnDefault("true")
    private boolean primarySubject;

    @ManyToOne
    @JoinColumn(name = "scenter_id")
    private SCenter scenter;

    @ManyToMany
    private final List<BlockTest> blockTests = new ArrayList<>();

    @ManyToMany
    private final List<Student> students = new ArrayList<>();

}
