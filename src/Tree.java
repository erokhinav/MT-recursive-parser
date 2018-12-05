import java.util.Arrays;
import java.util.List;

public class Tree {
    String node;
    List<Tree> children;

    public Tree(String node, Tree... children) {
        this.node = node;
        this.children = Arrays.asList(children);
    }

    public Tree(String node) {
        this.node = node;
    }

    // Code of Object.toString()
    public String toString() {
        StringBuilder sb = new StringBuilder(getClass().getName() + "(" + node + ")");
        if (children != null) {
            sb.append(" [" + children + "]");
        }
        return sb.toString();
    }
}
