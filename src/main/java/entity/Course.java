package entity;

import javax.persistence.*;
import java.util.List;

@Entity(name = "courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "course_name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "course")
    private List<Group> groups;
}