package org.example.dao;

import org.example.dao.StudentDAO;
import org.example.model.Student;
import org.example.util.DatabaseUtil;
import org.junit.jupiter.api.*;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StudentDAOTest {

    private StudentDAO studentDAO;
    private Connection conn;

    @BeforeAll
    void setUp() {
        studentDAO = new StudentDAO(conn);

        // Clear data trước khi test
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM students");
        } catch (SQLException e) {
            fail("Không thể reset bảng students trước khi test");
        }
    }

    @Test
    @Order(1)
    void testAddStudent() {
        Student s = new Student("John Doe", 22, "john@example.com");
        boolean result = studentDAO.addStudent(s);
        assertTrue(result, "Thêm sinh viên thất bại");

        List<Student> students = studentDAO.getAllStudents();
        assertEquals(1, students.size());
        assertEquals("John Doe", students.get(0).getName());
    }

    @Test
    @Order(2)
    void testGetAllStudents() {
        List<Student> students = studentDAO.getAllStudents();
        assertFalse(students.isEmpty(), "Danh sách sinh viên rỗng");
    }

    @Test
    @Order(3)
    void testUpdateStudent() {
        List<Student> students = studentDAO.getAllStudents();
        assertFalse(students.isEmpty());

        Student s = students.get(0);
        s.setName("Jane Smith");
        s.setEmail("jane@example.com");

        boolean updated = studentDAO.updateStudent(s);
        assertTrue(updated, "Cập nhật sinh viên thất bại");

        Student updatedStudent = studentDAO.findById(s.getId());
        assertEquals("Jane Smith", updatedStudent.getName());
        assertEquals("jane@example.com", updatedStudent.getEmail());
    }

    @Test
    @Order(4)
    void testDeleteStudent() {
        List<Student> students = studentDAO.getAllStudents();
        assertFalse(students.isEmpty());

        int idToDelete = students.get(0).getId();
        boolean deleted = studentDAO.deleteStudent(idToDelete);
        assertTrue(deleted, "Xoá sinh viên thất bại");

        Student deletedStudent = studentDAO.findById(idToDelete);
        assertNull(deletedStudent, "Sinh viên vẫn còn sau khi xoá");
    }
}
