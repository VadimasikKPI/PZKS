import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        laba5();
        //laba6();
    }

    public static void laba5(){
        String part = " a+b+c+d+e+f+g+h";
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
        TreeNode root = builder.buildExpressionTree(correct);
        root = builder.addFunctionOperations(root);
        TreeDrawer.startDrawing(root);
        System.out.println();
        int rootHeight = root.height();
        TreeNode rebuild = builder.rebuild(root);
        TreeDrawer.startDrawing(rebuild);
//        System.out.println();
        System.out.println("First tree height: " + rootHeight);
        System.out.println("Second tree height: " + rebuild.height());
//        Matrix matrix = new Matrix();
//        //matrix.manageOperations(rebuild);
//        matrix.processTree(root);
    }

    public static void laba6(){
        //String part = "a+(b+c+d+(e+f)+g)+h+f(a)";
        //String part = "((a+f(x))+(c+d))+((e+f)+(g+h))-f(x)";
        String part = "a*b*c/d+e*f*g/h+t*(a-q)-5.0*i-4*j+k+L+m*n*k*(p-1)+sin(pi*R)*log(q)/sin(3*pi/4+x*pi/2)";
        Analyzer analyzer = new Analyzer();
        ExpressionConverter expressionConverter = new ExpressionConverter();
        Worker worker = new Worker();
        String comutations = worker.generateComutations1(part);
        String distributions = worker.distrib(part);

        System.out.println("Comutations: " + comutations);
        distributions = worker.postProcess(distributions);
        System.out.println("Distributions: " + distributions);
        Builder builder = new Builder();
//        TreeNode root = builder.buildTree(new TreeNode(), result);
//        Builder.printTree(root);
        System.out.println();
//        int rootHeight = root.height();
//        TreeDrawer.startDrawing(root);
//        TreeDrawer.startDrawing(rebuild);
        System.out.println();
//        System.out.println("First tree height: " + rootHeight);
//        System.out.println("Second tree height: " + rebuild.height());
        Matrix matrix = new Matrix();
        //matrix.manageOperations(rebuild);
        List<Character> correct = analyzer.printCorrectExpression(part, analyzer.getErrorPositions());
        List<Character> result = expressionConverter.convertExpression(correct);
        TreeNode root = builder.buildExpressionTree(correct);
        root = builder.addFunctionOperations(root);
        //TreeNode rebuild = builder.rebuild(root);
        int first = matrix.processTree(root);
        List<Character> correctCommutation = analyzer.printCorrectExpression(comutations, analyzer.getErrorPositions());
        List<Character> resultCommutation = expressionConverter.convertExpression(correctCommutation);
        TreeNode rootCommutation = builder.buildExpressionTree(correctCommutation);
        rootCommutation = builder.addFunctionOperations(rootCommutation);
        //TreeNode rebuildCommutation = builder.rebuild(rootCommutation);
        int second = matrix.processTree(rootCommutation);
        List<Character> correctDistribution = analyzer.printCorrectExpression(distributions, analyzer.getErrorPositions());
        List<Character> resultDistribution = expressionConverter.convertExpression(correctDistribution);
        TreeNode rootDistribution = builder.buildExpressionTree(correctDistribution);
        rootDistribution = builder.addFunctionOperations(rootDistribution);
        //TreeNode rebuildDistribution = builder.rebuild(rootDistribution);
        int third = matrix.processTree(rootDistribution);
        System.out.println("Time for expression: " + first);
        System.out.println("Time for commutation: " + second);
        System.out.println("Time for distribution: " + third);

        List<String> distribs = worker.distribWithSteps(part);
        System.out.println("Distributions: --------------------------------------------");
        for(String s: distribs){
            System.out.println(s);
        }
        for(String s: distribs){
            List<Character> corectdistrib = analyzer.printCorrectExpression(s, analyzer.getErrorPositions());
            List<Character> resultdistrib = expressionConverter.convertExpression(corectdistrib);
            TreeNode rootdistrib = builder.buildExpressionTree(corectdistrib);
            rootdistrib = builder.addFunctionOperations(rootdistrib);
            int time = matrix.processTree(rootDistribution);
            System.out.println("Time for distribution: " + time);
        }
    }
}