package my.treeCounter;

public class Node {

    enum CStatus {IDLE, FIRST, SECOND, RESULT, ROOT}

    private boolean locked;
    private CStatus cStatus;
    private int firstValue, secondValue;
    private int result;
    Node parent;

    public Node() {
        cStatus = CStatus.ROOT;
        locked = false;
    }

    public Node(Node myParent) {
        parent = myParent;
        cStatus = CStatus.IDLE;
        locked = false;
    }

    synchronized boolean precombine() throws InterruptedException {
        while (locked) wait();
        switch (cStatus) {
            case IDLE:
                cStatus = CStatus.FIRST;
                return true;
            case FIRST:
                locked = true;
                cStatus = CStatus.SECOND;
                return false;
            case ROOT:
                return false;
            default:
                throw new RuntimeException("unexpected Node state" + cStatus);
        }
    }

    synchronized int combine(int combined) throws InterruptedException {
        while (locked) wait();
        locked = true;
        firstValue = combined;
        switch (cStatus) {
            case FIRST:
                return firstValue;
            case SECOND:
                return firstValue + secondValue;
            default:
                throw new RuntimeException("unexpected Node state " + cStatus);
        }
    }

    synchronized int op(int combined) throws InterruptedException {
        switch (cStatus) {
            case ROOT:
                int prior = result;
                result += combined;
                return prior;
            case SECOND:
                secondValue = combined;
                locked = false;
                notifyAll(); // wake up waiting threads
                while (cStatus != CStatus.RESULT) wait();
                locked = false;
                notifyAll();
                cStatus = CStatus.IDLE;
                return result;
            default:
                throw new RuntimeException("unexpected Node state");
        }
    }

    synchronized void distribute(int prior) {
        switch (cStatus) {
            case FIRST:
                cStatus = CStatus.IDLE;
                locked = false;
                break;
            case SECOND:
                result = prior + firstValue;
                cStatus = CStatus.RESULT;
                break;
            default:
                throw new RuntimeException("unexpected Node state");
        }
        notifyAll();
    }


}
