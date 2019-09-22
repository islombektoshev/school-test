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
public class SCenter {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;


    private String caption;

    @ManyToOne
    private User author;

    @OneToMany(mappedBy = "scenter")
    private final List<Subject> subjects = new ArrayList<>();

    @OneToMany(mappedBy = "scenter")
    private final List<Guruh> guruhs = new ArrayList<>();

    @OneToMany(mappedBy = "scenter")
    private final List<BlockTest> blockTests = new ArrayList<>();

    @OneToMany(mappedBy = "scenter")
    private final List<Student> students = new ArrayList<>();

}
