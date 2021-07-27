package logic;

import entity.Lesson;
import entity.Student;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class NearestLessonFinder {

    public Lesson findNearestLesson(Student student){
        List<Lesson> lessons = student.getGroup().getLessons();
        if(lessons.isEmpty()){
            throw new RuntimeException("Can not find nearest lesson to student id:" + student.getId());
        }

        lessons.stream()
                .sorted(Comparator.comparing(lesson -> lesson.getDate()))
                .collect(Collectors.toList());

        return lessons.get(0);
    }
}