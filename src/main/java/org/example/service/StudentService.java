package org.example.service;

import org.example.dao.StudentDAO;
import org.example.model.Student;

import java.sql.Connection;
import java.util.List;

public class StudentService {
    private final StudentDAO studentDAO;

    public StudentService(Connection conn) {
        this.studentDAO = new StudentDAO(conn);
    }

    // Lấy danh sách tất cả sinh viên
    public List<Student> getAll() {
        try {
            return studentDAO.getAllStudents();
        } catch (Exception e) {
            System.err.println("❌ Error fetching students: " + e.getMessage());
            return List.of(); // Trả về list rỗng nếu lỗi
        }
    }

    // Thêm sinh viên mới
    public void create(Student student) {
        try {
            studentDAO.addStudent(student);
            System.out.println("✅ Student added successfully.");
        } catch (Exception e) {
            System.err.println("❌ Error adding student: " + e.getMessage());
        }
    }

    // Cập nhật sinh viên
    public void update(Student student) {
        try {
            boolean result = studentDAO.updateStudent(student);
            if (result) {
                System.out.println("✅ Student updated successfully.");
            } else {
                System.out.println("⚠️ Student not found.");
            }
        } catch (Exception e) {
            System.err.println("❌ Error updating student: " + e.getMessage());
        }
    }

    // Xóa sinh viên theo ID
    public void delete(int id) {
        try {
            boolean result = studentDAO.deleteStudent(id);
            if (result) {
                System.out.println("✅ Student deleted successfully.");
            } else {
                System.out.println("⚠️ Student not found.");
            }
        } catch (Exception e) {
            System.err.println("❌ Error deleting student: " + e.getMessage());
        }
    }

    // Tìm sinh viên theo ID
    public Student findById(int id) {
        try {
            return studentDAO.findById(id);
        } catch (Exception e) {
            System.err.println("❌ Error finding student: " + e.getMessage());
            return null;
        }
    }
}
