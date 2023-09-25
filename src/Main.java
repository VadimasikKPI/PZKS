import java.util.List;

public class Main {
    public static void main(String[] args) {
        String expression = "-f(c, e++, 4m)/h[k+i] - 2.0005^(N-1)-(1-L) + s(t_1+e*m) - 1,7/kgh+b-g(e, 1)-(- 0)";
        Analyzer analyzer = new Analyzer();
        List<String> errors = analyzer.analyze(expression);

        if (errors.isEmpty()) {
            System.out.println("Вираз вірний.");
        } else {
            System.out.println("Помилки:");
            for (String error : errors) {
                System.out.println(error);
            }
        }
        analyzer.printCorrectExpression(expression, analyzer.getErrorPositions());
    }
}