package uz.owl.schooltest.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @OneToMany(mappedBy = "guruh", cascade = CascadeType.MERGE)
    private final List<Student> students = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.MERGE)
    private final List<BlockTest> blockTests = new ArrayList<>();

    @Override
    public String toString() {
        return "Guruh{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Guruh guruh = (Guruh) o;
        return Objects.equals(id, guruh.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    //    ======================================================
    public void addStudent(Student student){
        students.add(student);
    }

    public void  removeStudent(Student student){
        students.remove(student);
    }

    public BlockTest get(int index) {
        return blockTests.get(index);
    }

    public boolean addBlockTest(BlockTest blockTest) {
        return blockTests.add(blockTest);
    }

    public boolean removeBlockTest(Object o) {
        return blockTests.remove(o);
    }
}
