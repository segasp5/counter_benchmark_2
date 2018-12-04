package my;

public class SynchronizedCounter {

   private int num = 0;

    synchronized int getAndIncrement(){
        return num++;
    }
}
