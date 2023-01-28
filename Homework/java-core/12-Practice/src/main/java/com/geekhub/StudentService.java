package com.geekhub;

import java.io.*;
import java.util.List;
import java.util.logging.Logger;

public class StudentService {

    private final StudentRepository repository = new StudentRepository();
    private final File studentsFile = new File("Homework/java-core/12-Practice/src/main/resources/students.txt");
    private final Logger logger = Logger.getGlobal();

    public StudentService() {
        if (studentsFile.exists()) {
            setStudentsFromFile();
        }
    }

    public void addStudent(Student student) {
        repository.addStudent(student);

        saveChangesToFile();
    }

    public void deleteStudent(int id) {
        repository.deleteStudent(id);

        saveChangesToFile();
    }

    public List<Student> getStudents() {
        return repository.getStudents();
    }

    private void setStudentsFromFile() {
        try (FileReader fr = new FileReader(studentsFile);
             BufferedReader reader = new BufferedReader(fr)) {
            String line = reader.readLine();

            while (line != null) {
                repository.addStudent(StudentConsoleParser.fromStringFormat(line));
                line = reader.readLine();
            }
        } catch (IOException ex) {
            logger.warning(ex.getMessage());
        }
    }

    private void saveChangesToFile() {
        try (FileOutputStream fos = new FileOutputStream(studentsFile, false);
             PrintStream printStream = new PrintStream(fos)) {

            for (Student student : repository.getStudents()) {
                printStream.println(student.toString());
            }
        } catch (IOException ex) {
            logger.warning(ex.getMessage());
        }
    }
}
