import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String expression = "-f(c, e++, 4m)/h[k+i] - 2.0005^(N-1)-(1-L) + s(t_1+e*m) - 1,7/kgh+b-g(e, 1)-(1- 0)";
        String part = "a/b/c/d/e/f/g/h";
        //String part = "f(1, 2, --i)/1.0 + 0 - 0*k*h + 2 - 4.8/2 + 1*e/2";
        //String part = "A/B/C/D-M+Q+B";
        //String part = "A+B+C/D+G+K/L+M+N";
        //String part = "A+B+C/D+G+K/L+M+N*R+Q*(I+T)-(M*T)+K/R/M";
        Analyzer analyzer = new Analyzer();
        ExpressionConverter expressionConverter = new ExpressionConverter();
//        List<String> errors = analyzer.analyze(part);
//
//        if (errors.isEmpty()) {
//            System.out.println("Вираз вірний.");
//        } else {
//            System.out.println("Помилки:");
//            for (String error : errors) {
//                System.out.println(error);
//            }
//        }
        List<Character> correct = analyzer.printCorrectExpression(part, analyzer.getErrorPositions());
        System.out.println();
//        List<Character> correct = new ArrayList<>();
//        for (char c : part.toCharArray()) {
//            correct.add(c);
//        }
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

    }
}