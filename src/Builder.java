import java.util.ArrayList;
import java.util.List;

public class Builder {
    public TreeNode buildTree(TreeNode root, List<Character> expression) {
        List<Integer> operationsPositions = new ArrayList<>();
        for(int i = 0; i<expression.size();i++){
            if(isOperator(expression.get(i))){
                operationsPositions.add(i);
            }
        }
        if(operationsPositions.size()==0){
            return new TreeNode(expression.get(0));
        }
        else{
            root.setValue(expression.get(findCenter(operationsPositions)));
            root.setLeft(buildTree(root, expression.subList(0, findCenter(operationsPositions))));
            root.setRight(buildTree(root, expression.subList(findCenter(operationsPositions), expression.size())));
        }
        return root;
    }

    public static void printTree(TreeNode root) {

    }

    public int findCenter(List<Integer> positions){
        return positions.get(positions.size()/2);
    }
    public boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c=='^';
    }
}
