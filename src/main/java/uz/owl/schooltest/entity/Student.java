package uz.owl.schooltest.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Column(nullable = false, unique = true)
//    private String username;

    private String firstname;
    private String lastname;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private SCenter scenter;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Guruh guruh;

    @ManyToMany(cascade = CascadeType.PERSIST)
    private final List<BlockTest> blockTests = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "student", cascade = CascadeType.ALL)
    private final List<Result> results = new ArrayList<>();

}
