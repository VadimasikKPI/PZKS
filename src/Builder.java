import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;

public class Builder {
    private static final String OPERATORS = "+-*/";

    private static List<String> tokenize(List<Character> expression) {
        List<String> tokens = new ArrayList<>();
        StringBuilder operand = new StringBuilder();

        for (char c : expression) {
            if (Character.isLetterOrDigit(c)) {
                operand.append(c);
            } else if (c == '(' || c == ')') {
                if (operand.length() > 0) {
                    tokens.add(operand.toString());
                    operand.setLength(0);
                }
                tokens.add(String.valueOf(c));
            } else if (OPERATORS.contains(String.valueOf(c))) {
                if (operand.length() > 0) {
                    tokens.add(operand.toString());
                    operand.setLength(0);
                }
                tokens.add(String.valueOf(c));
            }
        }

        if (operand.length() > 0) {
            tokens.add(operand.toString());
        }

        return tokens;
    }

    private static boolean isOperand(String token) {
        return Character.isLetterOrDigit(token.charAt(0));
    }

    private static boolean isOperator(String token) {
        return OPERATORS.contains(token);
    }

    public static int priority(String operator) {
        if (operator.equals("+") || operator.equals("-")) {
            return 1;
        } else if (operator.equals("*") || operator.equals("/")) {
            return 2;
        }
        return 0;
    }

    public static void buildSubTree(Stack<TreeNode> operands, Stack<String> operators) {
        String operator = operators.pop();
        TreeNode right = operands.pop();
        TreeNode left = operands.pop();
        TreeNode root = new TreeNode(operator);
        root.left = left;
        root.right = right;
        operands.push(root);
    }

    public static TreeNode buildExpressionTree(List<Character> expression) {
        List<String> tokens = tokenize(expression);
        Stack<TreeNode> operands = new Stack<>();
        Stack<String> operators = new Stack<>();
        for (String token : tokens) {
            if (isOperand(token)) {
                operands.push(new TreeNode(token));
            } else if (isOperator(token)) {
                while (!operators.isEmpty() && priority(operators.peek()) >= priority(token)) {
                    buildSubTree(operands, operators);
                }
                operators.push(token);
            } else if (token.equals("(")) {
                operators.push(token);
            } else if (token.equals(")")) {
                while (!operators.peek().equals("(")) {
                    buildSubTree(operands, operators);
                }
                operators.pop();
            }
        }

        while (!operators.isEmpty()) {
            buildSubTree(operands, operators);
        }

        return operands.pop();
    }
    public static TreeNode rebuild(TreeNode root){
        if (root == null) {
            return null;
        }
        if(root.left!=null && root.right!=null && root.left.left!=null && root.left.left.right!=null){
            if(Objects.equals(root.value, root.left.left.value) && root.right.height()==1 && root.left.left.right.height()==1){
                TreeNode temp = root.right;
                root.right = new TreeNode(root.left.left.value);
                root.right.left = new TreeNode(root.left.left.right.value);
                root.right.right = temp;
                root.left.left = root.left.left.left;

            }
            else if(Objects.equals(root.value, root.left.value)&& root.right.height()==1 && root.left.right.height()==1){
                TreeNode temp = root.right;
                root.right = new TreeNode(root.left.value);
                root.right.left = new TreeNode(root.left.right.value);
                root.right.right = temp;
                root.left = root.left.left;
            }
            else {
                root.left = rebuild(root.left);
                root.right = rebuild(root.right);
            }
        }
        else if(root.left!=null && root.right!=null && root.left.left!=null){
            if(Objects.equals(root.value, root.left.value)&& root.right.height()==1 && root.left.left.height()==1){
                if(root.left.right!=null){
                    TreeNode temp = root.left.right;
                    root.left.right = new TreeNode(root.right.value);
                    root.right = temp;
                }
                else{
                TreeNode temp = root.right;
                root.right = new TreeNode(root.left.value);
                root.right.left = new TreeNode(root.left.left.value);
                root.right.right = temp;
                root.left = root.left.right;}
            }
        }
        else if(root.right!=null && root.left!=null){
            if(Objects.equals(root.value, root.left.value)&& root.right.height()==1 && root.left.right.height()==1){
                TreeNode temp = root.right;
                root.right = new TreeNode(root.left.value);
                root.right.left = new TreeNode(root.left.right.value);
                root.right.right = temp;
                root.left = root.left.left;
            }
            else {
                root.left = rebuild(root.left);
                root.right = rebuild(root.right);
            }
        }
        if(root.left!=null && root.left.right!=null && root.left.right.height()==1){
            rebuild(root.left);
        }
        return root;
    }
}
