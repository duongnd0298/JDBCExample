package org.example.repository;

import org.example.entity.Student;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class StudentRepository {

    private EntityManagerFactory emf;
    private EntityManager em;

    public StudentRepository() {
        this.emf = Persistence.createEntityManagerFactory("studentPU");
        this.em = emf.createEntityManager();
    }

    public void close() {
        if (em != null) em.close();
        if (emf != null) emf.close();
    }

    // Thêm sinh viên
    public void addStudent(Student student) {
        em.getTransaction().begin();
        em.persist(student);
        em.getTransaction().commit();
    }

    // Lấy danh sách tất cả sinh viên
    public List<Student> getAllStudents() {
        TypedQuery<Student> query = em.createQuery("SELECT s FROM Student s", Student.class);
        return query.getResultList();
    }

    // Tìm theo ID
    public Student findById(int id) {
        return em.find(Student.class, id);
    }

    // Cập nhật
    public void updateStudent(Student student) {
        em.getTransaction().begin();
        em.merge(student);
        em.getTransaction().commit();
    }

    // Xoá
    public void deleteStudent(int id) {
        Student student = em.find(Student.class, id);
        if (student != null) {
            em.getTransaction().begin();
            em.remove(student);
            em.getTransaction().commit();
        }
    }
}
