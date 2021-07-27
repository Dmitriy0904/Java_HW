package controller;

import entity.Lesson;
import entity.Student;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Controller {
    private final BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));


    public Long readStudentId(){
        try {
            System.out.println("=======================================================================================");
            System.out.println("Enter student id, which nearest lesson do you want to get");
            Long studentId = Long.parseLong(bf.readLine());
            return studentId;

        } catch (IOException exception){
            throw new RuntimeException(exception);
        }
    }


    public void printResult(Student student, Lesson lesson){
        if(student == null){
            throw new RuntimeException("Student is null");
        }
        if(lesson == null){
            throw new RuntimeException("Lesson is empty");
        }
        System.out.println("\nNearest lesson for student: " + student.getFullName() + "\n");
        System.out.println("\nId:" + lesson.getId() + " Topic:" + lesson.getTopic().getName() + " Date:" + lesson.getDate() + "\n");
    }
}