package com.geekhub;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class StudentRepository {

    private final List<Student> students = new ArrayList<>();

    public void addStudent(Student student) {
        students.add(student);
        setMaxStudentsId(students);
    }

    private void setMaxStudentsId(List<Student> students) {
        int maxId = students.stream()
            .map(Student::getId)
            .max(Comparator.comparingInt(id -> id))
            .orElse(1);

        Student.setMaxId(maxId);
    }

    public void deleteStudent(int id) {
        students.removeIf(student ->
            student.getId() == id
        );
    }

    public List<Student> getStudents() {
        return students;
    }
}
