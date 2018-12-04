package my;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicCounter {

    private AtomicInteger num = new AtomicInteger(0);

    int getAndIncrement() {
        int curr;
        do {
            curr = num.get();
        } while (!num.compareAndSet(curr, curr + 1));
        return curr;
    }
}
