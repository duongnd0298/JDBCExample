package org.example.repository; // Đảm bảo đúng package

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.example.entity.Student; // Đảm bảo import đúng entity

import java.util.List;

public class StudentRepository {
    private final EntityManager entityManager;

    public StudentRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    // Lấy danh sách tất cả sinh viên (tương tự getAllStudents)
    public List<Student> queryStudents() { // Đổi tên từ getAllStudents nếu muốn
        // Hoặc giữ tên getAllStudents cho nhất quán
        TypedQuery<Student> query = entityManager.createQuery("SELECT s FROM Student s", Student.class);
        return query.getResultList();
    }

    // Thêm sinh viên (Giả sử trả về void cho nhất quán)
    public void addStudent(Student s) { // Trả về void
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(s);
            entityManager.getTransaction().commit();
            System.out.println("✅ [Repository] Đã thêm sinh viên.");
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            e.printStackTrace();
            // Ném lại exception hoặc xử lý khác nếu cần
            throw new RuntimeException("Lỗi khi thêm sinh viên qua Repository", e);
        }
    }

    // Cập nhật sinh viên (Trả về void như trong hình)
    public void updateStudent(Student s) { // Trả về void
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(s); // Dùng merge để cập nhật detached entity
            entityManager.getTransaction().commit();
            System.out.println("✅ [Repository] Đã cập nhật sinh viên.");
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi cập nhật sinh viên qua Repository", e);
        }
    }

    // Xóa sinh viên theo ID (Trả về void như trong hình)
    public void deleteStudent(int id) { // Trả về void
        try {
            entityManager.getTransaction().begin();
            Student s = entityManager.find(Student.class, id);
            if (s != null) {
                entityManager.remove(s);
                entityManager.getTransaction().commit();
                System.out.println("✅ [Repository] Đã xóa sinh viên có ID: " + id);
            } else {
                entityManager.getTransaction().rollback(); // Rollback nếu không tìm thấy
                System.out.println("⚠️ [Repository] Không tìm thấy sinh viên để xóa với ID: " + id);
                // Có thể ném exception ở đây nếu việc không tìm thấy là một lỗi
                // throw new jakarta.persistence.EntityNotFoundException("Không tìm thấy Student với ID: " + id);
            }
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi xóa sinh viên qua Repository", e);
        }
    }

    // Tìm sinh viên theo ID
    public Student findById(int id) {
        // find không cần transaction tường minh
        return entityManager.find(Student.class, id);
    }
}