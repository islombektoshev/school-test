package uz.owl.schooltest.entity;

import lombok.AllArgsConstructor;
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
public class BlockTest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @ColumnDefault("30")
    private int countOfQuestion;

    @ManyToOne
    private SCenter scenter;

    @ManyToMany(mappedBy = "blockTests")
    private final List<Student> students = new ArrayList<>();

    @ManyToMany(mappedBy = "blockTests")
    private final List<Guruh> guruhs = new ArrayList<>();

    @ManyToMany(mappedBy = "blockTests")
    private final List<Subject> subjects = new ArrayList<>();

    @Override
    public String toString() {
        return "BlockTest{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", countOfQuestion=" + countOfQuestion +
                '}';
    }
}
