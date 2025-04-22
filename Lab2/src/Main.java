import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        // Создание первого полинома 3x^2 + 5x + 2
        Map<Integer, Double> poly1Coeffs = new HashMap<>();
        poly1Coeffs.put(2, 3.0);
        poly1Coeffs.put(1, 5.0);
        poly1Coeffs.put(0, 2.0);
        Polynomial poly1 = new Polynomial(poly1Coeffs);

        // Создание второго полинома x^2 - 2x + 4
        Map<Integer, Double> poly2Coeffs = new HashMap<>();
        poly2Coeffs.put(2, 1.0);
        poly2Coeffs.put(1, -2.0);
        poly2Coeffs.put(0, 4.0);
        Polynomial poly2 = new Polynomial(poly2Coeffs);

        // Выполнение операций
        Polynomial sum = poly1.add(poly2);
        Polynomial difference = poly1.subtract(poly2);
        Polynomial product = poly1.multiply(poly2);
        Polynomial scalarMultiplication = poly1.multiplyByScalar(2);
        Polynomial[] divisionResult = poly1.divide(poly2);
        Polynomial quotient = divisionResult[0];
        Polynomial remainder = divisionResult[1];

        // Вывод результатов
        System.out.println("Poly1: " + poly1);
        System.out.println("Poly2: " + poly2);
        System.out.println("Sum: " + sum);
        System.out.println("Difference: " + difference);
        System.out.println("Product: " + product);
        System.out.println("Scalar Multiplication (2 * Poly1): " + scalarMultiplication);
        System.out.println("Quotient: " + quotient);
        System.out.println("Remainder: " + remainder);
    }
}