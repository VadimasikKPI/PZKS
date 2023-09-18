import java.util.List;

public class Main {
    public static void main(String[] args) {
        String expression = "f1(2.1+b)+(A+B)*a(5.0,b)+5+C/D+G+(K/L+M+N)";
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