import java.util.List;

public class Main {
    public static void main(String[] args) {

        String expression = "1+(2x^2-5x+7)-(0-i)+(j+i)/0.1-(i*funk(2,7-x, )/q+send(i-(2x+7)/A[j,i],127.0)+a";
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