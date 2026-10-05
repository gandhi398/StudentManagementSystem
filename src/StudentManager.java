import java.util.*;
import java.util.stream.Collectors;

/**
 * Business-logic layer. Uses a LinkedHashMap (Collection Framework) for O(1) lookup
 * by ID while preserving insertion order. Every change is persisted via FileStorage.
 */
public class StudentManager {
    private final Map<Integer, Student> students = new LinkedHashMap<>();
    private final FileStorage storage;

    public StudentManager(FileStorage storage) {
        this.storage = storage;
        for (Student s : storage.load()) students.put(s.getId(), s);
    }

    public void addStudent(Student s) throws DuplicateStudentException {
        if (students.containsKey(s.getId())) throw new DuplicateStudentException(s.getId());
        students.put(s.getId(), s);
        storage.save(students.values());
    }

    public Student getStudent(int id) throws StudentNotFoundException {
        Student s = students.get(id);
        if (s == null) throw new StudentNotFoundException(id);
        return s;
    }

    public void updateStudent(int id, String name, Integer age, String course, Double gpa)
            throws StudentNotFoundException {
        Student s = getStudent(id);
        // null means "keep the existing value"
        if (name != null) s.setName(name);
        if (age != null) s.setAge(age);
        if (course != null) s.setCourse(course);
        if (gpa != null) s.setGpa(gpa);
        storage.save(students.values());
    }

    public void deleteStudent(int id) throws StudentNotFoundException {
        if (students.remove(id) == null) throw new StudentNotFoundException(id);
        storage.save(students.values());
    }

    public List<Student> getAll() {
        return new ArrayList<>(students.values());
    }

    public List<Student> searchByName(String keyword) {
        String k = keyword.toLowerCase();
        return students.values().stream()
                .filter(s -> s.getName().toLowerCase().contains(k))
                .collect(Collectors.toList());
    }

    public List<Student> sortedBy(Comparator<Student> comparator) {
        List<Student> list = getAll();
        list.sort(comparator);
        return list;
    }

    public int size() { return students.size(); }
}