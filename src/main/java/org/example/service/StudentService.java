package org.example.service;

import org.example.entity.Student;
import org.example.repository.StudentRepository;

import java.util.List;

public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService() {
        this.studentRepository = new StudentRepository();
    }

    public void close() {
        studentRepository.close();
    }

    public List<Student> getAll() {
        try {
            return studentRepository.getAllStudents();
        } catch (Exception e) {
            System.err.println("❌ Error fetching students: " + e.getMessage());
            return List.of();
        }
    }

    public void create(Student student) {
        try {
            studentRepository.addStudent(student);
            System.out.println("✅ Student added successfully.");
        } catch (Exception e) {
            System.err.println("❌ Error adding student: " + e.getMessage());
        }
    }

    public void update(Student student) {
        try {
            Student existing = studentRepository.findById(student.getId());
            if (existing != null) {
                studentRepository.updateStudent(student);
                System.out.println("✅ Student updated successfully.");
            } else {
                System.out.println("⚠️ Student not found.");
            }
        } catch (Exception e) {
            System.err.println("❌ Error updating student: " + e.getMessage());
        }
    }

    public void delete(int id) {
        try {
            studentRepository.deleteStudent(id);
            System.out.println("✅ Student deleted successfully.");
        } catch (Exception e) {
            System.err.println("❌ Error deleting student: " + e.getMessage());
        }
    }

    public Student findById(int id) {
        try {
            return studentRepository.findById(id);
        } catch (Exception e) {
            System.err.println("❌ Error finding student: " + e.getMessage());
            return null;
        }
    }
}
