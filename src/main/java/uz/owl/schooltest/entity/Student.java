package uz.owl.schooltest.entity;

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
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstname;
    private String lastname;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private SCenter scenter;

    @ManyToOne(cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
    private Guruh guruh;

    @ManyToMany(mappedBy = "students", cascade = CascadeType.MERGE)
    private final List<Subject> subjects = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.MERGE)
    private final List<BlockTest> blockTests = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "student", cascade = CascadeType.ALL)
    private final List<Result> results = new ArrayList<>();

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                '}';
    }

    public void addSubject(Subject element) {
        subjects.add(element);
    }

    public boolean removeSubject(Object index) {
        return subjects.remove(index);
    }

    public boolean addBlockTest(BlockTest blockTest) {
        return blockTests.add(blockTest);
    }

    public boolean removeBlockTest(Object o) {
        return blockTests.remove(o);
    }

    public void addResult(Result element) {
        results.add(element);
    }

    public Result removeResult(int index) {
        return results.remove(index);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(id, student.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
