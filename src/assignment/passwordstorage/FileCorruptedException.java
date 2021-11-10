package assignment.passwordstorage;

public class FileCorruptedException extends Exception {
    public FileCorruptedException() {
        super("Corrupted file");
    }
}
