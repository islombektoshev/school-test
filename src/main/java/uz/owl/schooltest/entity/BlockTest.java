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
public class BlockTest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ColumnDefault("30")
    private Integer countOfQuestion;

    @ManyToOne
    private SCenter scenter;

    @ManyToMany(mappedBy = "blockTests", cascade = CascadeType.MERGE)
    private final List<Student> students = new ArrayList<>();

    @ManyToMany(mappedBy = "blockTests", cascade = CascadeType.MERGE)
    private final List<Guruh> guruhs = new ArrayList<>();

    @ManyToMany(mappedBy = "blockTests", cascade = CascadeType.MERGE)
    private final List<Subject> subjects = new ArrayList<>();

    @Override
    public String toString() {
        return "BlockTest{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", countOfQuestion=" + countOfQuestion +
                '}';
    }

    public boolean addStudent(Student student) {
        return students.add(student);
    }

    public boolean removeStudent(Object o) {
        return students.remove(o);
    }

    public boolean addGuruh(Guruh guruh) {
        return guruhs.add(guruh);
    }

    public boolean removeGuruh(Object o) {
        return guruhs.remove(o);
    }

    public boolean addSubject(Subject subject) {
        return subjects.add(subject);
    }
    public boolean removeSubject(Object o) {
        return subjects.remove(o);
    }
}
