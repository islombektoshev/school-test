package uz.owl.schooltest.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @Column(name = "primary_subject", nullable = false)
    @ColumnDefault("true")
    private boolean primarySubject;

    @ManyToOne
    @JoinColumn(name = "scenter_id")
    private SCenter scenter;

    @ManyToMany(cascade = CascadeType.MERGE)
    private final List<BlockTest> blockTests = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.MERGE)
    private final List<Student> students = new ArrayList<>();

    @Override
    public String toString() {
        return "Subject{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subject subject = (Subject) o;
        return id.equals(subject.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void addStudent(Student student){
        students.add(student);
    }

    public void removeStudent(Student student){
        students.remove(student);
    }

    public void addBlockTest(BlockTest blockTest){
        blockTests.add(blockTest);
    }

    public void removeBlockTest(BlockTest blockTest){
        blockTests.remove(blockTest);
    }
}
