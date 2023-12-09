import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        //String expression = "a/(b-c)";
        String expression = "a+b+f(5.9-q)";
        //String expression = "a+b*c-d*a";
        //String expression = "a+(b+c)-d";
        //String expression1 = "(a+(b-c))";
        //String expression1 = "(a+b)-(c+d)";
        String expression1 = "a-b*(k-t)-(f-g)*(f*5.9-q)-(f+g)/(d+q-w)";
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
        //List<String> comutations = worker.generateComutations(expression);
        List<String> distributions = worker.generateDistributions1(expression1);
//        System.out.println("Comutations:");
        int i = 1;
//        for(String comutation : comutations){
//            System.out.println(i+": "+comutation);
//            i++;
//        }
        i=1;
        System.out.println("Distributions:");
        for (String distribution : distributions) {
            if(distribution.substring(0,1).equals("+")){
                distribution = distribution.substring(1);
            }
            else if(distribution.equals(")")){
                break;
            }
            System.out.println(i+ ": " + distribution);
            i++;
        }

        String lastDistribution = distributions.get(distributions.size()-1);
        System.out.println("Last distribution: "+lastDistribution);
    }
}