package uz.owl.schooltest.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "scenter", uniqueConstraints = @UniqueConstraint(columnNames = {"name","author_id"}))
public class SCenter {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    private String caption;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;


    @OneToMany(mappedBy = "scenter", cascade = {CascadeType.ALL, CascadeType.REMOVE})
    private final List<Subject> subjects = new ArrayList<>();

    @OneToMany(mappedBy = "scenter", cascade = {CascadeType.ALL, CascadeType.REMOVE})
    private final List<Guruh> guruhs = new ArrayList<>();

    @OneToMany(mappedBy = "scenter", cascade = {CascadeType.ALL, CascadeType.REMOVE})
    private final List<BlockTest> blockTests = new ArrayList<>();

    @OneToMany(mappedBy = "scenter", cascade = {CascadeType.ALL, CascadeType.REMOVE})
    private final List<Student> students = new ArrayList<>();

    @Override
    public String toString() {
        return "SCenter{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", caption='" + caption + '\'' +
                '}';
    }

    public boolean addSubject(Subject subject){
        return subjects.add(subject);
    }

    public boolean removeSubject(Subject subject){
        return subjects.remove(subject);
    }

    public boolean addGuruh(Guruh guruh){
        return guruhs.add(guruh);
    }

    public boolean removeGuruh(Guruh guruh){
        return guruhs.remove(guruh);
    }

    public boolean addStudent(Student student){
        return students.add(student);
    }

    public boolean removeStudent(Student student){
        return students.remove(student);
    }

    public boolean addBlockTest(BlockTest blockTest){
        return  blockTests.add(blockTest);
    }

    public boolean removeBlockTest(BlockTest blockTest){
        return blockTests.remove(blockTest);
    }
}
