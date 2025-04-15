package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.example.Student;


import static org.example.DatabaseUtil.*;

public class Controller{

    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String query = "SELECT * FROM students";

        String JDBC_URL = "jdbc:mysql://localhost:3306/student_db";
        String JDBC_USER = "root";
        String JDBC_PASSWORD = "123456";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Student s = new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("email")
                );
                students.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }
    public void addStudent(Student s) {
        String query = "INSERT INTO students (name, age, email) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)){
            pstmt.setString(1, s.getName());
            pstmt.setInt(2, s.getAge());
            pstmt.setString(3, s.getEmail());

            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Da them " + rowsAffected + " sinh vien.");

        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public void updateStudent(Student s) {
        String query = "UPDATE students SET name = ?, age = ?, email = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)){
            pstmt.setString(1, s.getName());
            pstmt.setInt(2, s.getAge());
            pstmt.setString(3, s.getEmail());
            pstmt.setInt(4, s.getId());
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Da cap nhat " + rowsAffected + " sinh vien.");

        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public boolean deleteStudent(int id) {
        String query = "DELETE FROM students WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)){
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Đã xóa sinh viên có ID: " + id);
                return true; // Indicate successful deletion
            } else {
                System.out.println("Không tìm thấy sinh viên có ID: " + id);
                return false; // Indicate no student found with that ID
            }

        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }
    public Student findById(int id) {
        String query = "SELECT * FROM students WHERE id = ?";
        Student student = null;

        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                student = new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("email")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return student;
    }

    }
