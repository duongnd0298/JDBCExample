package org.example;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        Controller controller = new Controller();
        int choice;
        do {
            System.out.println("=== MENU ===");
            System.out.println("1. Hiển thị sinh viên");
            System.out.println("2. Thêm sinh viên");
            System.out.println("3. Cập nhật sinh viên");
            System.out.println("4. Xóa sinh viên");
            System.out.println("5. Tìm theo ID");
            System.out.println("0. Thoát");
            System.out.print("Chọn: ");
            choice = sc.nextInt();
            sc.nextLine(); // bỏ dòng thừa

            switch (choice) {
                case 1 -> {
                    try {
                        controller.getAllStudents().forEach(System.out::println);
                    } catch(InputMismatchException e) {
                        System.out.println("Invalid input. Please enter correct values.");
                        sc.nextLine();
                    }
                    System.out.print("\nNhấn Enter để quay lại menu...");
                    sc.nextLine();
                }
                case 2 -> {
                    try {
                        System.out.print("Enter student name: ");
                        String name = sc.nextLine();
                        int age;
                        do {
                            System.out.print("Enter age : ");
                            age = sc.nextInt();
                            sc.nextLine();
                        } while (age < 0);
                        String email;
                        do {
                            System.out.print("Enter Email: ");
                            email = sc.nextLine();
                        } while (email.isEmpty());
                        Student newStudent = new Student();
                        newStudent.setName(name);
                        newStudent.setAge(age);
                        newStudent.setEmail(email);

                        controller.addStudent(newStudent);
                    }catch (InputMismatchException e) {
                        System.out.println("Invalid input. Please enter correct values.");
                        sc.nextLine();
                    }
                    System.out.print("\nNhấn Enter để quay lại menu...");
                    sc.nextLine();

                }
                case 3 -> {
                    System.out.println("\n-- Update Student --");

                    try {
                        System.out.print("Enter student ID to update: ");
                        int id = sc.nextInt();
                        sc.nextLine(); // Đọc bỏ dòng thừa

                        System.out.print("Enter new student name: ");
                        String newName = sc.nextLine();

                        int newAge;
                        do {
                            System.out.print("Enter new student age: ");
                            newAge = sc.nextInt();
                            sc.nextLine(); // Clear newline
                        } while (newAge < 0);

                        System.out.print("Enter new student email: ");
                        String newEmail = sc.nextLine();

                        // Tạo đối tượng Student mới với ID cũ và thông tin mới
                        Student updatedStudent = new Student(id, newName, newAge, newEmail);

                        controller.updateStudent(updatedStudent);

                    } catch (InputMismatchException e) {
                        System.err.println("Invalid input. Please enter correct values.");
                        sc.nextLine(); // Đọc bỏ đầu vào không hợp lệ
                    }

                    System.out.print("\nNhấn Enter để quay lại menu...");
                    sc.nextLine();
                }
                case 4 -> {
                    System.out.println("\n-- Delete Student --");
                    try{
                        System.out.print("Enter student ID to delete: ");
                        int id = sc.nextInt();
                        sc.nextLine();
                        controller.deleteStudent(id);

                    }catch (InputMismatchException e){
                        System.err.println("Invalid input. Please enter correct values.");
                        sc.nextLine();

                    }
                    System.out.print("\nNhấn Enter để quay lại menu...");
                    sc.nextLine();
                }
                case 5 -> {
                    System.out.println("\n-- Find Student by ID --");
                    try {
                        System.out.print("Enter student ID to find: ");
                        int id = sc.nextInt();
                        sc.nextLine();
                        controller.findById(id);
                        Student foundStudent = controller.findById(id);
                        if (foundStudent != null) {
                            System.out.println("\n-- Student Found --");
                            System.out.println("ID: " + foundStudent.getId());
                            System.out.println("Name: " + foundStudent.getName());
                            System.out.println("Age: " + foundStudent.getAge());
                            System.out.println("Email: " + foundStudent.getEmail());
                            // Add other relevant student information here
                        } else {
                            System.out.println("Student with ID " + id + " not found.");
                        }

                    } catch (InputMismatchException e) {
                        System.err.println("Invalid input. Please enter correct values.");
                        sc.nextLine();
                    }
                    System.out.print("\nNhấn Enter để quay lại menu...");
                    sc.nextLine();
                }
                case 0 -> System.out.println("Tạm biệt!");
                default -> System.out.println("Không hợp lệ!");
            }
        } while (choice != 0);

    }
}