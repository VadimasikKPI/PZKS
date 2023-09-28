public class TreeNode {
    char value;
    TreeNode left;
    TreeNode right;

    TreeNode(char value) {
        this.value = value;
        this.left = null;
        this.right = null;
    }

    public TreeNode() {
    }

    public char getValue() {
        return value;
    }

    public void setValue(char value) {
        this.value = value;
    }

    public TreeNode getLeft() {
        return left;
    }

    public void setLeft(TreeNode left) {
        this.left = left;
    }

    public TreeNode getRight() {
        return right;
    }

    public void setRight(TreeNode right) {
        this.right = right;
    }
}
