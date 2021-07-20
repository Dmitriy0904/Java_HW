package model;

import annotation.Mapped;

public class Student {
    @Mapped("firstname")
    private String firstname;

    @Mapped("lastname")
    private String lastname;

    @Mapped("faculty")
    private String faculty;

    @Mapped("course")
    private int course;

    @Mapped("group")
    private String group;

    @Mapped("scholarship")
    private boolean scholarship;

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getFaculty() {
        return faculty;
    }

    public Integer getCourse() {
        return course;
    }

    public String getGroup() {
        return group;
    }

    public Boolean getScholarship() {
        return scholarship;
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

    public void setCourse(Integer course) {
        this.course = course;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setScholarship(Boolean scholarship) {
        this.scholarship = scholarship;
    }

    @Override
    public String toString() {
        return "Student{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", faculty='" + faculty + '\'' +
                ", course=" + course +
                ", group='" + group + '\'' +
                ", scholarship=" + scholarship +
                '}';
    }
}