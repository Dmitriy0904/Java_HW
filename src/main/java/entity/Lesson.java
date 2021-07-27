package entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "lessons")
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id")
    private Topic topic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @Column(name = "date", nullable = false)
    private Date date;

    public Long getId() {
        return id;
    }

    public Group getGroup() {
        return group;
    }

    public Topic getTopic() {
        return topic;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public Date getDate() {
        return date;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}