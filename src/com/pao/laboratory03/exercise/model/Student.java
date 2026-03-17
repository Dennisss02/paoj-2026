package com.pao.laboratory03.exercise.model;

import com.pao.laboratory03.exercise.exception.InvalidStudentException;
import com.pao.laboratory03.exercise.exception.InvalidGradeException;

import java.util.*;

public class Student {
    private String name;
    private int age;
    private Map<Subject, Double> grades;
    public Student(String name, int age) {
        if(age < 18 || age > 60) {
            throw new InvalidStudentException("Varsta " + age + " este invalida (18-60)");
        }
        this.name = name;
        this.age = age;
        grades = new HashMap<Subject, Double>();
    }
    public String getName() { return name; }
    public int getAge() { return age; }
    public Map<Subject, Double> getGrades() { return grades; }
    public void addGrade(Subject subject, double grade) {
        if(grade < 1 || grade > 10) {
            throw new InvalidGradeException("Nota " + grade + " nu este valida (1-10)");
        }
        grades.put(subject, grade);
    }
    public double getAverage() {
        int n = grades.size();
        if(n == 0) {
            return 0;
        }
        double sum = 0;
        for(double grade: grades.values()) {
            sum += grade;
        }
        return sum / n;
    }

    @Override
    public String toString() {
        return "Student{name=" + name + ", age=" + age + ", avg=" + getAverage() + "}";
    }
}
