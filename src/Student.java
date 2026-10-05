/**
 * Model class representing a single student record.
 * Demonstrates encapsulation: private fields, public getters/setters, validation.
 */
public class Student {
    private final int id;
    private String name;
    private int age;
    private String course;
    private double gpa;

    public Student(int id, String name, int age, String course, double gpa) {
        this.id = id;
        setName(name);
        setAge(age);
        setCourse(course);
        setGpa(gpa);
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getCourse() { return course; }
    public double getGpa() { return gpa; }

    public void setName(String name) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Name cannot be empty.");
        this.name = name.trim();
    }

    public void setAge(int age) {
        if (age < 5 || age > 100)
            throw new IllegalArgumentException("Age must be between 5 and 100.");
        this.age = age;
    }

    public void setCourse(String course) {
        if (course == null || course.isBlank())
            throw new IllegalArgumentException("Course cannot be empty.");
        this.course = course.trim();
    }

    public void setGpa(double gpa) {
        if (gpa < 0.0 || gpa > 10.0)
            throw new IllegalArgumentException("GPA must be between 0.0 and 10.0.");
        this.gpa = gpa;
    }

    /** Serialises the student to one CSV line (commas in text are replaced to keep the format safe). */
    public String toCsv() {
        return id + "," + name.replace(",", " ") + "," + age + "," + course.replace(",", " ") + "," + gpa;
    }

    /** Rebuilds a Student from a CSV line. */
    public static Student fromCsv(String line) {
        String[] p = line.split(",");
        if (p.length != 5) throw new IllegalArgumentException("Malformed record: " + line);
        return new Student(Integer.parseInt(p[0].trim()), p[1], Integer.parseInt(p[2].trim()),
                p[3], Double.parseDouble(p[4].trim()));
    }

    @Override
    public String toString() {
        return String.format("| %-5d | %-20s | %-4d | %-18s | %-5.2f |", id, name, age, course, gpa);
    }
}