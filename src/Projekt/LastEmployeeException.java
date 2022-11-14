package Projekt;

public class LastEmployeeException extends Exception {
    public LastEmployeeException() {
        System.out.println("\nTen pracownik był Twoim ostatnim na tym stanowisku.\n");
    }
}
