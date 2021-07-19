package model;

import reflection.PropertyKey;

public class Student {
    @PropertyKey("firstname")
    private String firstname;

    @PropertyKey("lastname")
    private String lastname;

    @PropertyKey("faculty")
    private String faculty;

    @PropertyKey("course")
    private int course;


    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getFaculty() {
        return faculty;
    }

    public int getCourse() {
        return course;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public void setCourse(int course) {
        this.course = course;
    }

    @Override
    public String toString() {
        return "DataClass.Student{" + firstname + " " + lastname +
                ", course:" + course +
                ", faculty:" + faculty + '}';
    }
}