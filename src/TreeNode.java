public class TreeNode {
    String value;
    TreeNode left;
    TreeNode right;

    TreeNode(String value) {
        this.value = value;
        this.left = null;
        this.right = null;
    }

    public TreeNode() {
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
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
    public int size()
    {
        if (value == null) {
            return 0;
        }
        return 1 + left.size() + right.size();
    }
    public int height() {
        if(value == null) {
            return 0;
        }
        if(left==null || right==null) {
            return 1;
        }
        int leftHeight = left.height();
        int rightHeight = right.height();
        if(leftHeight > rightHeight) {
            return leftHeight + 1;
        }
        return rightHeight + 1;
    }
    public boolean isEmpty()
    {
        return value == null;
    }

    public boolean isLeaf()
    {
        return value!=null && left.isEmpty() && right.isEmpty();
    }

    public void show()
    {
        if ( value != null )
        {
            left.show();
            System.out.println( value );
            right.show();
        }
    }

    public boolean hasLeft()
    {
        return left != null;
    }
    public boolean hasRight()
    {
        return right != null;
    }

}
