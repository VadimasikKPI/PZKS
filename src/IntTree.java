public class IntTree {
    int value;
    IntTree left;
    IntTree right;

    public IntTree(int value) {
        this.value = value;
        this.left = null;
        this.right = null;
    }

    public IntTree() {
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public IntTree getLeft() {
        return left;
    }

    public void setLeft(IntTree left) {
        this.left = left;
    }

    public IntTree getRight() {
        return right;
    }

    public void setRight(IntTree right) {
        this.right = right;
    }
}
