package org.example;

import org.example.model.Student;
import org.example.service.StudentService;
import org.example.util.DatabaseUtil;

import java.sql.Connection;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try (Connection conn = DatabaseUtil.getConnection()) {
            StudentService service = new StudentService(conn);

            int choice;
            do {
                System.out.println("\n===== STUDENT MANAGEMENT SYSTEM =====");
                System.out.println("1. View all students");
                System.out.println("2. Add new student");
                System.out.println("3. Update student");
                System.out.println("4. Delete student");
                System.out.println("5. Find student by ID");
                System.out.println("0. Exit");
                System.out.print("Choose: ");
                choice = sc.nextInt();
                sc.nextLine(); // Clear newline

                switch (choice) {
                    case 1 -> {
                        List<Student> students = service.getAll();
                        System.out.println("\n--- Student List ---");
                        students.forEach(System.out::println);
                    }
                    case 2 -> {
                        System.out.print("Enter name: ");
                        String name = sc.nextLine();
                        System.out.print("Enter age: ");
                        int age = sc.nextInt();
                        sc.nextLine(); // Clear newline
                        System.out.print("Enter email: ");
                        String email = sc.nextLine();
                        service.create(new Student(name, age, email));
                    }
                    case 3 -> {
                        System.out.print("Enter student ID to update: ");
                        int id = sc.nextInt();
                        sc.nextLine(); // Clear newline
                        Student existing = service.findById(id);
                        if (existing == null) {
                            System.out.println("⚠️ Student not found.");
                        } else {
                            System.out.print("New name: ");
                            String name = sc.nextLine();
                            System.out.print("New age: ");
                            int age = sc.nextInt();
                            sc.nextLine(); // Clear newline
                            System.out.print("New email: ");
                            String email = sc.nextLine();
                            service.update(new Student(id, name, age, email));
                        }
                    }
                    case 4 -> {
                        System.out.print("Enter student ID to delete: ");
                        int id = sc.nextInt();
                        sc.nextLine(); // Clear newline
                        service.delete(id);
                    }
                    case 5 -> {
                        System.out.print("Enter student ID to find: ");
                        int id = sc.nextInt();
                        sc.nextLine();
                        Student student = service.findById(id);
                        if (student != null) {
                            System.out.println("✅ Found: " + student);
                        } else {
                            System.out.println("⚠️ Student not found.");
                        }
                    }
                    case 0 -> System.out.println("👋 Exiting...");
                    default -> System.out.println("❌ Invalid option. Try again.");
                }

            } while (choice != 0);
        } catch (InputMismatchException e) {
            System.err.println("❌ Invalid input. Exiting...");
        } catch (Exception e) {
            System.err.println("❌ Application error: " + e.getMessage());
        }
    }
}
