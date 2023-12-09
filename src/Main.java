import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        //laba5();
        laba6();
    }

    public static void laba5(){
        String part = "a+(b+c+d+(e+f)+g)+h+f(a)";
        //String part = "((a + f(x)) + (c + d)) + ((e + f) + (g + h))-f(x)";
        //String part = "a*b*c/d + e*f*g/h + t*(a-q) - 5.0*i - 4*j + k + L + m*n*k*(p-1) + sin(pi*R)*log(q)/sin(3*pi/4 + x*pi/2)";
        Analyzer analyzer = new Analyzer();
        ExpressionConverter expressionConverter = new ExpressionConverter();
        Worker worker = new Worker();
        List<Character> correct = analyzer.printCorrectExpression(part, analyzer.getErrorPositions());
        System.out.println();
        List<Character> result = expressionConverter.convertExpression(correct);

        for (char c : result) {
            System.out.print(c);
        }

        System.out.println();
        Builder builder = new Builder();
//        TreeNode root = builder.buildTree(new TreeNode(), result);
//        Builder.printTree(root);
        TreeNode root = builder.buildExpressionTree(result);
        TreeDrawer.startDrawing(root);
        System.out.println();
        int rootHeight = root.height();
        TreeNode rebuild = builder.rebuild(root);
        TreeDrawer.startDrawing(rebuild);
        System.out.println();
        System.out.println("First tree height: " + rootHeight);
        System.out.println("Second tree height: " + rebuild.height());
        Matrix matrix = new Matrix();
        //matrix.manageOperations(rebuild);
        matrix.processTree(rebuild);
    }

    public static void laba6(){
        String part = "a+(b*a-c-a*b*c)+b*c+c+c*d*e+e*f+k*(d+f+2*(a-c*e+w)-q*t)";
        Analyzer analyzer = new Analyzer();
        ExpressionConverter expressionConverter = new ExpressionConverter();
        Worker worker = new Worker();
        String comutations = worker.generateComutations1(part);
        String distributions = worker.distrib(part);
        System.out.println("Comutations: " + comutations);
        System.out.println("Distributions: " + distributions);
        List<Character> correct = analyzer.printCorrectExpression(part, analyzer.getErrorPositions());
        List<Character> result = expressionConverter.convertExpression(correct);
        Builder builder = new Builder();
//        TreeNode root = builder.buildTree(new TreeNode(), result);
//        Builder.printTree(root);
        TreeNode root = builder.buildExpressionTree(result);

        System.out.println();
//        int rootHeight = root.height();
        TreeNode rebuild = builder.rebuild(root);
//        TreeDrawer.startDrawing(root);
//        TreeDrawer.startDrawing(rebuild);
        System.out.println();
//        System.out.println("First tree height: " + rootHeight);
//        System.out.println("Second tree height: " + rebuild.height());
        Matrix matrix = new Matrix();
        //matrix.manageOperations(rebuild);
        int first = matrix.processTree(rebuild);
        List<Character> correctCommutation = analyzer.printCorrectExpression(comutations, analyzer.getErrorPositions());
        List<Character> resultCommutation = expressionConverter.convertExpression(correctCommutation);
        TreeNode rootCommutation = builder.buildExpressionTree(resultCommutation);
        TreeNode rebuildCommutation = builder.rebuild(rootCommutation);
        int second = matrix.processTree(rebuildCommutation);
        List<Character> correctDistribution = analyzer.printCorrectExpression(distributions, analyzer.getErrorPositions());
        List<Character> resultDistribution = expressionConverter.convertExpression(correctDistribution);
        TreeNode rootDistribution = builder.buildExpressionTree(resultDistribution);
        TreeNode rebuildDistribution = builder.rebuild(rootDistribution);
        int third = matrix.processTree(rebuildDistribution);
        System.out.println("Time for expression: " + first);
        System.out.println("Time for commutation: " + second);
        System.out.println("Time for distribution: " + third);

    }
}