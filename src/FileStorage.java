import java.io.*;
import java.nio.file.*;
import java.util.*;

/** Handles persistence of student records to a CSV file (file handling). */
public class FileStorage {
    private final Path path;

    public FileStorage(String fileName) {
        this.path = Paths.get(fileName);
    }

    public List<Student> load() {
        List<Student> students = new ArrayList<>();
        if (!Files.exists(path)) return students;   // first run: nothing to load

        try (BufferedReader br = Files.newBufferedReader(path)) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;
                try {
                    students.add(Student.fromCsv(line));
                } catch (IllegalArgumentException e) {
                    System.out.println("Skipping bad record: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Could not read data file: " + e.getMessage());
        }
        return students;
    }

    public void save(Collection<Student> students) {
        try (BufferedWriter bw = Files.newBufferedWriter(path)) {
            for (Student s : students) {
                bw.write(s.toCsv());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Could not save data: " + e.getMessage());
        }
    }
}