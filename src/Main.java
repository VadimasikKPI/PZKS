import java.util.List;

public class Main {
    public static void main(String[] args) {

        String expression = "a+b+c-d";
//        Analyzer analyzer = new Analyzer();
//        List<String> errors = analyzer.analyze(expression);
//
//        if (errors.isEmpty()) {
//            System.out.println("Вираз вірний.");
//        } else {
//            System.out.println("Помилки:");
//            for (String error : errors) {
//                System.out.println(error);
//            }
//        }
//        analyzer.printCorrectExpression(expression, analyzer.getErrorPositions());
        Worker worker = new Worker();
        List<String> comutations = worker.generateComutations(expression);
        //List<String> distributions = worker.generateDistributions(expression);
        System.out.println("Comutations:");
        int i = 1;
        for(String comutation : comutations){
            System.out.println(i+": "+comutation);
            i++;
        }
//System.out.println("Distributions:");
//        for (String distribution : distributions) {
//            System.out.println(distribution);
//        }
    }
}