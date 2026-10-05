import java.util.*;

/** Console UI: menu loop, input reading and validation. */
public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static final String HEADER =
            "+-------+----------------------+------+--------------------+-------+\n" +
                    "| ID    | Name                 | Age  | Course             | GPA   |\n" +
                    "+-------+----------------------+------+--------------------+-------+";
    private static final String FOOTER =
            "+-------+----------------------+------+--------------------+-------+";

    public static void main(String[] args) {
        StudentManager manager = new StudentManager(new FileStorage("students.csv"));
        System.out.println("=== Student Management System ===");

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Enter choice: ");
            try {
                switch (choice) {
                    case 1 -> addStudent(manager);
                    case 2 -> updateStudent(manager);
                    case 3 -> deleteStudent(manager);
                    case 4 -> viewStudent(manager);
                    case 5 -> printTable(manager.getAll());
                    case 6 -> printTable(manager.searchByName(readLine("Name to search: ")));
                    case 7 -> sortMenu(manager);
                    case 0 -> { running = false; System.out.println("Data saved. Goodbye!"); }
                    default -> System.out.println("Invalid choice. Try again.");
                }
            } catch (StudentNotFoundException | DuplicateStudentException | IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n1. Add student");
        System.out.println("2. Update student");
        System.out.println("3. Delete student");
        System.out.println("4. View student by ID");
        System.out.println("5. View all students");
        System.out.println("6. Search by name");
        System.out.println("7. Sort students");
        System.out.println("0. Exit");
    }

    private static void addStudent(StudentManager m) throws DuplicateStudentException {
        int id = readInt("ID: ");
        String name = readLine("Name: ");
        int age = readInt("Age: ");
        String course = readLine("Course: ");
        double gpa = readDouble("GPA (0-10): ");
        m.addStudent(new Student(id, name, age, course, gpa));
        System.out.println("Student added successfully.");
    }

    private static void updateStudent(StudentManager m) throws StudentNotFoundException {
        int id = readInt("ID of student to update: ");
        Student s = m.getStudent(id);
        System.out.println("Current: " + s);
        System.out.println("(Press Enter to keep the current value)");

        String name = readLine("New name: ");
        String ageStr = readLine("New age: ");
        String course = readLine("New course: ");
        String gpaStr = readLine("New GPA: ");

        try {
            m.updateStudent(id,
                    name.isBlank() ? null : name,
                    ageStr.isBlank() ? null : Integer.parseInt(ageStr),
                    course.isBlank() ? null : course,
                    gpaStr.isBlank() ? null : Double.parseDouble(gpaStr));
            System.out.println("Student updated successfully.");
        } catch (NumberFormatException e) {
            System.out.println("Error: age and GPA must be numbers. Nothing was changed.");
        }
    }

    private static void deleteStudent(StudentManager m) throws StudentNotFoundException {
        int id = readInt("ID of student to delete: ");
        String confirm = readLine("Delete " + m.getStudent(id).getName() + "? (y/n): ");
        if (confirm.equalsIgnoreCase("y")) {
            m.deleteStudent(id);
            System.out.println("Student deleted.");
        } else {
            System.out.println("Cancelled.");
        }
    }

    private static void viewStudent(StudentManager m) throws StudentNotFoundException {
        printTable(List.of(m.getStudent(readInt("ID: "))));
    }

    private static void sortMenu(StudentManager m) {
        System.out.println("Sort by: 1) Name  2) GPA (high to low)  3) Age  4) ID");
        Comparator<Student> cmp = switch (readInt("Choice: ")) {
            case 1 -> Comparator.comparing(s -> s.getName().toLowerCase());
            case 2 -> Comparator.comparingDouble(Student::getGpa).reversed();
            case 3 -> Comparator.comparingInt(Student::getAge);
            case 4 -> Comparator.comparingInt(Student::getId);
            default -> null;
        };
        if (cmp == null) System.out.println("Invalid option.");
        else printTable(m.sortedBy(cmp));
    }

    private static void printTable(List<Student> list) {
        if (list.isEmpty()) { System.out.println("No records found."); return; }
        System.out.println(HEADER);
        list.forEach(System.out::println);
        System.out.println(FOOTER);
        System.out.println("Total: " + list.size());
    }

    // ---------- input helpers ----------
    private static String readLine(String prompt) {
        System.out.print(prompt);
        return sc.hasNextLine() ? sc.nextLine().trim() : "";
    }

    private static int readInt(String prompt) {
        while (true) {
            try { return Integer.parseInt(readLine(prompt)); }
            catch (NumberFormatException e) { System.out.println("Please enter a valid whole number."); }
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            try { return Double.parseDouble(readLine(prompt)); }
            catch (NumberFormatException e) { System.out.println("Please enter a valid number."); }
        }
    }
}