package entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "topics")
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "topic_name", nullable = false)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "id")
    private List<Lesson> lesson;
}