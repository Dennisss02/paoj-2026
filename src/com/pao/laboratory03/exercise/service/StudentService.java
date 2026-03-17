package com.pao.laboratory03.exercise.service;

import com.pao.laboratory03.exercise.exception.StudentNotFoundException;
import com.pao.laboratory03.exercise.model.Student;
import com.pao.laboratory03.exercise.model.Subject;

import java.util.*;

public class StudentService {
    private List<Student> students;

    private static StudentService Instance = null;
    private StudentService() {
        students = new ArrayList<>();
    };
    public static StudentService getInstance() {
        if(Instance == null) {
            Instance = new StudentService();
        }
        return Instance;
    }

    public Student findByName(String name) {
        for(Student s: students) {
            if(Objects.equals(s.getName(), name)) {
                return s;
            }
        }
        throw new StudentNotFoundException("Studentul " + name + " nu a fost gasit");
    }
    public void addStudent(String name, int age) {
        try {
            findByName(name);
        }
        catch(StudentNotFoundException e) {
            students.add(new Student(name, age));
            return;
        }
        throw new RuntimeException("Studentul " + name + " exista deja");
    }
    public void addGrade(String studentName, Subject subject, double grade) {
        findByName(studentName).addGrade(subject, grade);
    }
    public void printAllStudents() {
        System.out.println(students);
    }
    public void printTopStudents() {
        students.sort(Comparator.comparingDouble(Student::getAverage).reversed());
        System.out.println(students);
    }
    public Map<Subject, Double> getAveragePerSubject() {
        Map<Subject, Double> sumSubject = new HashMap<>();
        Map<Subject, Integer> cntSubject = new HashMap<>();
        for(Student s: students) {
            for(Map.Entry<Subject, Double> entry: s.getGrades().entrySet()) {
                Subject subject = entry.getKey();
                Double grade = entry.getValue();
                sumSubject.put(subject, sumSubject.getOrDefault(subject, 0.0) + grade);
                cntSubject.put(subject, cntSubject.getOrDefault(subject, 0) + 1);
            }
        }
        Map<Subject, Double> averages = new HashMap<>();
        for(Subject sub: sumSubject.keySet()) {
            averages.put(sub, sumSubject.get(sub) / cntSubject.get(sub));
        }
        return averages;
    }
}
