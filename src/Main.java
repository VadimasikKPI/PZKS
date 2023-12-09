import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        laba5();
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
}