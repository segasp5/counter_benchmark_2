package my.treeCounter;

import java.util.Stack;

public class CombiningTree {

    private final Node[] leaf;

    public CombiningTree(int width) {
        Node[] nodes = new Node[width - 1];
        nodes[0] = new Node();
        for (int i = 1; i < nodes.length; i++) {
            nodes[i] = new Node(nodes[(i - 1) / 2]);
        }
        leaf = new Node[(width + 1) / 2];
        for (int i = 0; i < leaf.length; i++) {
            leaf[i] = nodes[nodes.length - i - 1];
        }
    }


    public int getAndIncrement() throws InterruptedException {
        Stack<Node> stack = new Stack<Node>();
        Node myLeaf = leaf[(int)(Thread.currentThread().getId() - 11) / 2];
        Node node = myLeaf;
        // precombining phase
        while (node.precombine()) {
            node = node.parent;
        }
        Node stop = node;
        // combining phase
        node = myLeaf;
        int combined = 1;
        while (node != stop) {
            combined = node.combine(combined);
            stack.push(node);
            node = node.parent;
        }
        // operation phase
        int prior = stop.op(combined);
        // distribution phase
        while (!stack.empty()) {
            node = stack.pop();
            node.distribute(prior);
        }
        return prior;
    }
}
