import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by k3rnel on 5/2/16.
 *
 * Pool of threads with factory pattern. We are going to create a factory of threads with
 * centralized control and generic for all classes of tasks.
 */

public class ThreadFactory {

    private Thread [] threads;
    private int aliveThreads = 0;
    private ReentrantLock lockThreads = new ReentrantLock();
    private Condition condThreads;

    public ThreadFactory(int size){
        threads = new Thread[size];

        condThreads = lockThreads.newCondition();
    }

    public Thread getThread(Runnable r){
        //we have to search a free thread
        boolean found = false;
        int position = 0;
        Thread t = null;

        lockThreads.lock();

        while (aliveThreads == threads.length){
            try {
                System.out.println("No hay threads disponibles");
                condThreads.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        aliveThreads++;
        System.out.println("Threads: " + aliveThreads);


        while (!found){
            position = 0;

            while (!found && position < threads.length){
                if (threads[position] == null || !threads[position].isAlive()){
                    threads[position] = null;
                    threads[position] = new Thread(r);
                    t = threads[position];
                    found = true;
                }

                position++;
            }
        }

        lockThreads.unlock();

        return t;
    }

    public void joinAll(){
        for (int i = 0; i < threads.length; i++){
            if (threads[i] != null){
                try {
                    threads[i].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void freeOne(){
        lockThreads.lock();
        aliveThreads--;
        System.out.println("Threads: " + aliveThreads);
        condThreads.signal();
        lockThreads.unlock();
    }

    public int getAliveThreads(){
        return aliveThreads;
    }

}
