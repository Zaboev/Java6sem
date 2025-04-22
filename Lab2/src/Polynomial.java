import java.util.*;

public class Polynomial implements Comparable<Polynomial>, Cloneable {
    private final Map<Integer, Double> coefficients;

    public Polynomial() {
        this.coefficients = new TreeMap<>(Collections.reverseOrder());
    }

    public Polynomial(Map<Integer, Double> coefficients) {
        this();
        for (Map.Entry<Integer, Double> entry : coefficients.entrySet()) {
            if (entry.getValue() != 0) {
                this.coefficients.put(entry.getKey(), entry.getValue());
            }
        }
    }

    public Polynomial add(Polynomial other) {
        Map<Integer, Double> result = new HashMap<>(this.coefficients);
        for (Map.Entry<Integer, Double> entry : other.coefficients.entrySet()) {
            result.put(entry.getKey(), result.getOrDefault(entry.getKey(), 0.0) + entry.getValue());
        }
        return new Polynomial(result);
    }

    public Polynomial subtract(Polynomial other) {
        Map<Integer, Double> result = new HashMap<>(this.coefficients);
        for (Map.Entry<Integer, Double> entry : other.coefficients.entrySet()) {
            result.put(entry.getKey(), result.getOrDefault(entry.getKey(), 0.0) - entry.getValue());
        }
        return new Polynomial(result);
    }

    public Polynomial multiply(Polynomial other) {
        Map<Integer, Double> result = new HashMap<>();
        for (Map.Entry<Integer, Double> e1 : this.coefficients.entrySet()) {
            for (Map.Entry<Integer, Double> e2 : other.coefficients.entrySet()) {
                int power = e1.getKey() + e2.getKey();
                double coefficient = result.getOrDefault(power, 0.0) + e1.getValue() * e2.getValue();
                result.put(power, coefficient);
            }
        }
        return new Polynomial(result);
    }

    public Polynomial multiplyByScalar(double scalar) {
        Map<Integer, Double> result = new HashMap<>();
        for (Map.Entry<Integer, Double> entry : this.coefficients.entrySet()) {
            result.put(entry.getKey(), entry.getValue() * scalar);
        }
        return new Polynomial(result);
    }

    public Polynomial[] divide(Polynomial divisor) {
        Polynomial quotient = new Polynomial();
        Polynomial remainder = new Polynomial(this.coefficients);

        while (!remainder.coefficients.isEmpty() && remainder.degree() >= divisor.degree()) {
            int powerDiff = remainder.degree() - divisor.degree();
            double coeffQuotient = remainder.leadingCoefficient() / divisor.leadingCoefficient();

            Polynomial term = new Polynomial(Map.of(powerDiff, coeffQuotient));
            quotient = quotient.add(term);
            remainder = remainder.subtract(term.multiply(divisor));
        }
        return new Polynomial[]{quotient, remainder};
    }

    public int degree() {
        return this.coefficients.isEmpty() ? 0 : this.coefficients.keySet().iterator().next();
    }

    public double leadingCoefficient() {
        return this.coefficients.isEmpty() ? 0 : this.coefficients.values().iterator().next();
    }

    @Override
    public int compareTo(Polynomial other) {
        return Integer.compare(this.degree(), other.degree());
    }

    @Override
    public Polynomial clone() {
        return new Polynomial(new HashMap<>(this.coefficients));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Integer, Double> entry : this.coefficients.entrySet()) {
            if (sb.length() > 0 && entry.getValue() > 0) {
                sb.append(" + ");
            }
            sb.append(entry.getValue()).append("x^").append(entry.getKey());
        }
        return sb.toString().replace("x^0", "").replace("x^1", "x");
    }
}
