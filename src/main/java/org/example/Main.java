package org.example;

import org.example.entity.Student;
import org.example.service.StudentService;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        StudentService service = new StudentService();

        int choice;
        do {
            System.out.println("\n===== STUDENT MANAGEMENT =====");
            System.out.println("1. Show All Students");
            System.out.println("2. Add New Student");
            System.out.println("3. Update Student");
            System.out.println("4. Delete Student");
            System.out.println("5. Find Student by ID");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            while (!sc.hasNextInt()) {
                System.out.print("⚠️ Invalid input. Enter a number: ");
                sc.next();
            }
            choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1 -> {
                    List<Student> students = service.getAll();
                    if (students.isEmpty()) {
                        System.out.println("⚠️ No students found.");
                    } else {
                        students.forEach(System.out::println);
                    }
                }

                case 2 -> {
                    System.out.print("Enter student name: ");
                    String name = sc.nextLine();

                    System.out.print("Enter age: ");
                    int age = Integer.parseInt(sc.nextLine());

                    System.out.print("Enter email: ");
                    String email = sc.nextLine();

                    Student newStudent = new Student(name, age, email);
                    service.create(newStudent);
                }

                case 3 -> {
                    try {
                        System.out.print("Enter student ID to update: ");
                        int id = Integer.parseInt(sc.nextLine());

                        Student existing = service.findById(id);
                        if (existing == null) {
                            System.out.println("⚠️ Student not found.");
                            break;
                        }

                        System.out.print("New name: ");
                        String name = sc.nextLine();

                        System.out.print("New age: ");
                        int age = Integer.parseInt(sc.nextLine());

                        System.out.print("New email: ");
                        String email = sc.nextLine();

                        Student updatedStudent = new Student(id, name, age, email);
                        service.update(updatedStudent);
                    } catch (NumberFormatException e) {
                        System.err.println("⚠️ Invalid input. Please enter valid data.");
                    }
                }

                case 4 -> {
                    System.out.print("Enter student ID to delete: ");
                    int id = Integer.parseInt(sc.nextLine());
                    service.delete(id);
                }

                case 5 -> {
                    System.out.print("Enter student ID to find: ");
                    int id = Integer.parseInt(sc.nextLine());
                    Student student = service.findById(id);
                    if (student != null) {
                        System.out.println(student);
                    } else {
                        System.out.println("⚠️ Student not found.");
                    }
                }

                case 0 -> System.out.println("👋 Exiting...");

                default -> System.out.println("❌ Invalid choice. Try again.");
            }

        } while (choice != 0);

        service.close();
        sc.close();
    }
}