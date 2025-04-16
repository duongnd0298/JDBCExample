package org.example.repository; // Package của lớp test

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.example.entity.Student;
import org.junit.jupiter.api.*; // Giữ các import của JUnit

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StudentRepositoryTest { // Đổi tên lớp test

    private EntityManagerFactory emf;
    private EntityManager em;
    // private StudentDAO studentDAO; // Xóa dòng này
    private StudentRepository studentRepository; // Thêm dòng này

    @BeforeAll
    void setUpAll() {
        try {
            emf = Persistence.createEntityManagerFactory("student-jpa-unit");
            em = emf.createEntityManager();

            // Khởi tạo StudentRepository thay vì StudentDAO
            // this.studentDAO = new StudentDAO(em); // Xóa hoặc comment dòng này
            this.studentRepository = new StudentRepository(em); // Thêm dòng này

            System.out.println("--- Đang dọn dẹp bảng students trước khi test Repository ---");
            em.getTransaction().begin();
            int deletedCount = em.createQuery("DELETE FROM Student").executeUpdate();
            em.getTransaction().commit();
            System.out.println("Đã xóa " + deletedCount + " bản ghi students.");
            System.out.println("--- Thiết lập Repository Test hoàn tất, bắt đầu test ---");

        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) em.getTransaction().rollback();
            if (em != null && em.isOpen()) em.close();
            if (emf != null && emf.isOpen()) emf.close();
            e.printStackTrace();
            fail("Lỗi nghiêm trọng khi thiết lập môi trường Repository test: " + e.getMessage());
        }
    }

    @AfterAll
    void tearDownAll() {
        System.out.println("--- Đang dọn dẹp Repository Test sau khi test ---");
        if (em != null && em.isOpen()) em.close();
        if (emf != null && emf.isOpen()) emf.close();
        System.out.println("--- Dọn dẹp Repository Test hoàn tất ---");
    }

    @Test
    @Order(1)
    void testAddStudent() {
        System.out.println("--- Chạy Repository testAddStudent ---");
        Student s = new Student("Repo User", 25, "repo@example.com");

        // Gọi phương thức repository (trả về void)
        // boolean result = studentDAO.addStudent(s); // Thay thế dòng này
        assertDoesNotThrow(() -> studentRepository.addStudent(s), "addStudent không nên ném lỗi"); // Gọi phương thức add của repository

        // Kiểm tra ID được gán
        assertNotNull(s.getId(), "ID của sinh viên phải được gán sau khi persist");

        // Kiểm tra lại bằng cách đọc từ DB
        Student foundStudent = studentRepository.findById(s.getId());
        assertNotNull(foundStudent, "Phải tìm thấy sinh viên vừa thêm bằng ID");
        assertEquals("Repo User", foundStudent.getName());
        System.out.println("Đã thêm và xác nhận sinh viên qua Repository: " + foundStudent.getName() + " với ID: " + foundStudent.getId());
    }

    @Test
    @Order(2)
    void testQueryStudents() { // Đổi tên test method nếu cần
        System.out.println("--- Chạy Repository testQueryStudents ---");
        // Thêm sinh viên thứ 2 (nếu testAddStudent chưa thêm đủ)
        Student s2 = new Student("Repo User 2", 26, "repo2@example.com");
        assertDoesNotThrow(() -> studentRepository.addStudent(s2), "addStudent 2 không nên ném lỗi");
        assertNotNull(s2.getId(), "ID của sinh viên thứ 2 phải được gán");

        // Gọi phương thức repository
        // List<Student> students = studentDAO.getAllStudents(); // Thay thế
        List<Student> students = studentRepository.queryStudents(); // Hoặc getAllStudents()

        assertNotNull(students, "Danh sách sinh viên không được null");
        assertFalse(students.isEmpty(), "Danh sách sinh viên không được rỗng");
        // Giả sử test 1 và đầu test 2 đã thêm thành công -> có 2 sv
        assertEquals(2, students.size(), "Danh sách phải chứa đúng 2 sinh viên");
        System.out.println("Đã lấy danh sách " + students.size() + " sinh viên qua Repository.");
    }

    @Test
    @Order(3)
    void testUpdateStudent() {
        System.out.println("--- Chạy Repository testUpdateStudent ---");
        // List<Student> students = studentDAO.getAllStudents(); // Thay thế
        List<Student> students = studentRepository.queryStudents();
        assertFalse(students.isEmpty(), "Phải có sinh viên để cập nhật");

        Student studentToUpdate = students.get(0); // Lấy sv đầu tiên
        int studentId = studentToUpdate.getId();
        System.out.println("Đang chuẩn bị cập nhật sinh viên qua Repository có ID: " + studentId);

        studentToUpdate.setName("Repo User Updated");
        studentToUpdate.setEmail("repo.updated@example.com");

        // Gọi phương thức repository (trả về void)
        // boolean result = studentDAO.updateStudent(studentToUpdate); // Thay thế
        assertDoesNotThrow(() -> studentRepository.updateStudent(studentToUpdate), "updateStudent không nên ném lỗi");

        // Kiểm tra lại bằng cách đọc từ DB
        Student updatedStudent = studentRepository.findById(studentId);
        assertNotNull(updatedStudent, "Phải tìm thấy sinh viên sau khi cập nhật");
        assertEquals("Repo User Updated", updatedStudent.getName(), "Tên phải được cập nhật");
        assertEquals("repo.updated@example.com", updatedStudent.getEmail(), "Email phải được cập nhật");
        System.out.println("Đã cập nhật và xác nhận sinh viên qua Repository ID: " + studentId);
    }

    @Test
    @Order(4)
    void testDeleteStudent() {
        System.out.println("--- Chạy Repository testDeleteStudent ---");
        // List<Student> students = studentDAO.getAllStudents(); // Thay thế
        List<Student> students = studentRepository.queryStudents();
        assertFalse(students.isEmpty(), "Phải có sinh viên để xóa");
        int initialSize = students.size();

        int idToDelete = students.get(initialSize - 1).getId(); // Lấy ID sv cuối
        System.out.println("Đang chuẩn bị xóa sinh viên qua Repository có ID: " + idToDelete);

        // Gọi phương thức repository (trả về void)
        // boolean result = studentDAO.deleteStudent(idToDelete); // Thay thế
        assertDoesNotThrow(() -> studentRepository.deleteStudent(idToDelete), "deleteStudent không nên ném lỗi");

        // Kiểm tra lại bằng cách tìm theo ID (phải trả về null)
        Student deletedStudent = studentRepository.findById(idToDelete);
        assertNull(deletedStudent, "Sinh viên phải không tìm thấy (null) sau khi xóa");

        // Kiểm tra lại số lượng
        // List<Student> remainingStudents = studentDAO.getAllStudents(); // Thay thế
        List<Student> remainingStudents = studentRepository.queryStudents();
        assertEquals(initialSize - 1, remainingStudents.size(), "Số lượng sinh viên phải giảm đi 1 sau khi xóa");
        System.out.println("Đã xóa sinh viên qua Repository ID: " + idToDelete + ". Số sinh viên còn lại: " + remainingStudents.size());
    }
}