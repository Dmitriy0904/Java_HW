package entity;

import javax.persistence.*;

@Entity
@Table(name = "marks")
public class Mark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    private Integer mark;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @OneToOne
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

}