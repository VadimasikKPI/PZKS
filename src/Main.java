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
        //String expression = "-f(c, e++, 4m)/h[k+i] - 2.0005^(N-1)-(1-L) + s(t_1+e*m) - 1,7/kgh+b-g(e, 1)-(1- 0)";
        //String part = "a+(b+c+d+(e+f)+g)+h+f(a)";
        //String part = "((a + f(x)) + (c + d)) + ((e + f) + (g + h))-f(x)";
        String part = "a*b*c/d + e*f*g/h + t*(a-q) - 5.0*i - 4*j + k + L + m*n*k*(p-1) + sin(pi*R)*log(q)/sin(3*pi/4 + x*pi/2)";

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