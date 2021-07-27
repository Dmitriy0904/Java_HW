package entity;

import javax.persistence.*;
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

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Lesson> getLesson() {
        return lesson;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLesson(List<Lesson> lesson) {
        this.lesson = lesson;
    }
}