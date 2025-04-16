package org.example.service;

import org.example.entity.Student;
// import org.example.dao.StudentDAO; // Xóa import này
import org.example.repository.StudentRepository; // Thêm import này

import java.util.List;

public class StudentService {

    // private final StudentDAO studentDAO; // Thay thế dòng này
    private final StudentRepository studentRepository; // Bằng dòng này

    // Cập nhật constructor
    public StudentService(StudentRepository studentRepository) { // Thay StudentDAO
        this.studentRepository = studentRepository; // Thay studentDAO
    }

    public List<Student> findAllStudents() {
        // Thay đổi lời gọi phương thức nếu tên khác nhau
        // return studentDAO.getAllStudents();
        return studentRepository.queryStudents(); // Hoặc getAllStudents() nếu bạn giữ tên đó
    }

    public void createStudent(Student student) {
        // Thay đổi lời gọi phương thức
        // studentDAO.addStudent(student);
        studentRepository.addStudent(student);
    }

    public void modifyStudent(Student student) {
        // Thay đổi lời gọi phương thức
        // studentDAO.updateStudent(student);
        studentRepository.updateStudent(student);
    }

    public void removeStudent(int id) {
        // Thay đổi lời gọi phương thức
        // studentDAO.deleteStudent(id);
        studentRepository.deleteStudent(id);
    }

    public Student getStudentById(int id) {
        // Thay đổi lời gọi phương thức
        // return studentDAO.findById(id);
        return studentRepository.findById(id);
    }

    // Các phương thức service khác nếu có...
}