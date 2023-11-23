import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        //String expression = "a/(b-c)";
        String expression = "a-(b*a-c-a*b*c)+b*c+c+c*d*e+e*f+k*(d+f+2*(a-c*e+w)-q*t)";//якщо не має * чи / то душку прибрати (це перед обробкою зробити)
        //String expression = "a+b*c-d*a";
        //String expression = "a+(b+c)-d";
        //String expression1 = "(a/(b-c)-b)";
        //String expression1 = "3*d*(a+a*b*c)";
        //String expression1 = "a*e*(b*c-c*d-b+b/d)";
        //String expression1 = "a*e*(b*c+c*d*(w-f)-b+b/d)";
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
        //String comutations = worker.generateComutations1(expression);
        //String distributions = worker.distrib(expression1);
        List<String> dis = worker.distribWithSteps(expression1);
        int i = 1;
//        if(distributions.charAt(0)=='+'){
//            distributions = distributions.substring(1);
//        }
//        System.out.println(i+ ": " + distributions);
        //System.out.println(i+": "+comutations);
        for(String s: dis){
            if(s.charAt(0)=='+'){
                System.out.println(i+": "+s.substring(1));
                i++;
        }
            else{
            System.out.println(i+": "+s);
            i++;
            }
        }


    }
}