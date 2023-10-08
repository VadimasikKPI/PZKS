import java.util.List;

public class Main {
    public static void main(String[] args) {

        String expression = "-f(1*0)- an*0p(a+b)-1.0005/6*(f(b,1.8-a*(2-6)/1+(b+a))/(6x^2+4x-1)) + d/dt*(smn(at+q)/(4cos(at)-ht^2))";
        Analyzer analyzer = new Analyzer();
        System.out.println(analyzer.amount(expression));
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