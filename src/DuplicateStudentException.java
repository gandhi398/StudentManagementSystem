/** Thrown when trying to add a student whose ID already exists. */
public class DuplicateStudentException extends Exception {
    public DuplicateStudentException(int id) {
        super("A student with ID " + id + " already exists.");
    }
}