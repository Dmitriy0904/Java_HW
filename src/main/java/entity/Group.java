package entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "course_groups")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "group_name", nullable = false, unique = true)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "group")
    private List<Student> students;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @OneToMany(mappedBy = "id")
    private List<Lesson> lessons;
}