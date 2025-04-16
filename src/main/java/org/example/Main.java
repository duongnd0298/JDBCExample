package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
// Bỏ import DAO nếu không dùng trực tiếp nữa
// import org.example.dao.StudentDAO;
import org.example.repository.StudentRepository; // 1. Import Repository
import org.example.service.StudentService;      // 2. Import Service (đã được sửa để dùng Repository)
import org.example.entity.Student;              // Import Student entity nếu cần dùng trực tiếp
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        EntityManagerFactory emf = null; // Khai báo ngoài try để finally có thể truy cập
        EntityManager em = null;
        Scanner scanner = new Scanner(System.in); // Khởi tạo Scanner

        try {
            // 1. Khởi tạo EntityManagerFactory và EntityManager (Giữ nguyên)
            emf = Persistence.createEntityManagerFactory("student-jpa-unit");
            em = emf.createEntityManager();

            // 2. Truyền EntityManager vào Repository và Service (ĐÃ SỬA)
            // StudentDAO studentDAO = new StudentDAO(em); // Bỏ dòng này
            StudentRepository studentRepository = new StudentRepository(em); // 3. Tạo StudentRepository
            StudentService studentService = new StudentService(studentRepository); // 4. Truyền Repository vào Service

            // 3. Menu tương tác
            // Phần logic gọi các phương thức của studentService ở đây không cần thay đổi,
            // vì StudentService sẽ tự động gọi đến Repository thay vì DAO.

            while (true) {
                System.out.println("\n====== MENU ======");
                System.out.println("1. Thêm sinh viên");
                System.out.println("2. Hiển thị danh sách");
                System.out.println("3. Cập nhật sinh viên");
                System.out.println("4. Xóa sinh viên");
                System.out.println("5. Tìm theo ID");
                System.out.println("0. Thoát");
                System.out.print("Chọn chức năng: ");

                int choice = -1;
                try {
                    // Đọc cả dòng rồi chuyển sang số để tránh lỗi Scanner
                    choice = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Lựa chọn không hợp lệ. Vui lòng nhập số.");
                    continue; // Quay lại vòng lặp
                }

                switch (choice) {
                    case 1:
                        addStudentUI(scanner, studentService);
                        break;
                    case 2:
                        displayStudentsUI(studentService);
                        break;
                    case 3:
                        updateStudentUI(scanner, studentService);
                        break;
                    case 4:
                        deleteStudentUI(scanner, studentService);
                        break;
                    case 5:
                        findStudentByIdUI(scanner, studentService);
                        break;
                    case 0:
                        System.out.println("Đang thoát chương trình...");
                        // Thoát vòng lặp và đến khối finally
                        return; // Kết thúc hàm main
                    default:
                        System.out.println("Lựa chọn không hợp lệ!");
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi: Đã có lỗi xảy ra trong quá trình chạy.");
            e.printStackTrace(); // In chi tiết lỗi để debug
        } finally {
            // 5. Đóng tài nguyên (Rất quan trọng!)
            if (scanner != null) {
                scanner.close(); // Đóng Scanner
                System.out.println("Scanner đã đóng.");
            }
            if (em != null && em.isOpen()) {
                em.close(); // Đóng EntityManager
                System.out.println("EntityManager đã đóng.");
            }
            if (emf != null && emf.isOpen()) {
                emf.close(); // Đóng EntityManagerFactory
                System.out.println("EntityManagerFactory đã đóng.");
            }
        }
    }

    // --- Các phương thức helper cho từng chức năng menu ---
    // (Bạn cần tạo các phương thức này để code trong main gọn hơn)

    private static void addStudentUI(Scanner scanner, StudentService studentService) {
        try {
            System.out.print("Nhập tên sinh viên: ");
            String name = scanner.nextLine();
            System.out.print("Nhập tuổi sinh viên: ");
            int age = Integer.parseInt(scanner.nextLine());
            System.out.print("Nhập email sinh viên: ");
            String email = scanner.nextLine();

            Student newStudent = new Student(name, age, email);
            studentService.createStudent(newStudent); // Gọi service
            System.out.println("=> Thêm sinh viên thành công!");
            // ID sẽ được tự gán nếu cấu hình đúng
            if (newStudent.getId() != null) {
                System.out.println("ID sinh viên mới: " + newStudent.getId());
            }
        } catch (NumberFormatException e) {
            System.out.println("Lỗi: Tuổi phải là một số nguyên.");
        } catch (Exception e) {
            System.out.println("Lỗi khi thêm sinh viên: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void displayStudentsUI(StudentService studentService) {
        System.out.println("\n--- Danh sách sinh viên ---");
        List<Student> students = studentService.findAllStudents(); // Gọi service
        if (students.isEmpty()) {
            System.out.println("Không có sinh viên nào.");
        } else {
            students.forEach(System.out::println); // In từng sinh viên
        }
        System.out.println("--------------------------");
    }

    private static void updateStudentUI(Scanner scanner, StudentService studentService) {
        try {
            System.out.print("Nhập ID sinh viên cần cập nhật: ");
            int id = Integer.parseInt(scanner.nextLine());

            Student existingStudent = studentService.getStudentById(id); // Gọi service
            if (existingStudent == null) {
                System.out.println("=> Không tìm thấy sinh viên với ID: " + id);
                return;
            }

            System.out.println("Thông tin hiện tại: " + existingStudent);
            System.out.print("Nhập tên mới (bỏ trống nếu không đổi): ");
            String name = scanner.nextLine();
            System.out.print("Nhập tuổi mới (bỏ trống nếu không đổi): ");
            String ageStr = scanner.nextLine();
            System.out.print("Nhập email mới (bỏ trống nếu không đổi): ");
            String email = scanner.nextLine();

            // Cập nhật thông tin nếu người dùng nhập
            if (name != null && !name.trim().isEmpty()) {
                existingStudent.setName(name);
            }
            if (ageStr != null && !ageStr.trim().isEmpty()) {
                try {
                    existingStudent.setAge(Integer.parseInt(ageStr));
                } catch (NumberFormatException e) {
                    System.out.println("Tuổi nhập không hợp lệ, giữ nguyên tuổi cũ.");
                }
            }
            if (email != null && !email.trim().isEmpty()) {
                existingStudent.setEmail(email);
            }

            studentService.modifyStudent(existingStudent); // Gọi service để cập nhật
            System.out.println("=> Cập nhật sinh viên ID " + id + " thành công!");

        } catch (NumberFormatException e) {
            System.out.println("Lỗi: ID phải là một số nguyên.");
        } catch (Exception e) {
            System.out.println("Lỗi khi cập nhật sinh viên: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void deleteStudentUI(Scanner scanner, StudentService studentService) {
        try {
            System.out.print("Nhập ID sinh viên cần xóa: ");
            int id = Integer.parseInt(scanner.nextLine());

            // Optional: Xác nhận trước khi xóa
            Student studentToDelete = studentService.getStudentById(id);
            if (studentToDelete == null) {
                System.out.println("=> Không tìm thấy sinh viên với ID: " + id + " để xóa.");
                return;
            }
            System.out.println("Bạn có chắc muốn xóa sinh viên: " + studentToDelete + "? (y/n)");
            String confirm = scanner.nextLine();

            if (confirm.equalsIgnoreCase("y")) {
                studentService.removeStudent(id); // Gọi service
                System.out.println("=> Đã xóa sinh viên có ID: " + id);
            } else {
                System.out.println("=> Hủy bỏ thao tác xóa.");
            }

        } catch (NumberFormatException e) {
            System.out.println("Lỗi: ID phải là một số nguyên.");
        } catch (Exception e) {
            System.out.println("Lỗi khi xóa sinh viên: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void findStudentByIdUI(Scanner scanner, StudentService studentService) {
        try {
            System.out.print("Nhập ID sinh viên cần tìm: ");
            int id = Integer.parseInt(scanner.nextLine());
            Student student = studentService.getStudentById(id); // Gọi service
            if (student != null) {
                System.out.println("=> Tìm thấy: " + student);
            } else {
                System.out.println("=> Không tìm thấy sinh viên với ID: " + id);
            }
        } catch (NumberFormatException e) {
            System.out.println("Lỗi: ID phải là một số nguyên.");
        } catch (Exception e) {
            System.out.println("Lỗi khi tìm sinh viên: " + e.getMessage());
            e.printStackTrace();
        }
    }
}