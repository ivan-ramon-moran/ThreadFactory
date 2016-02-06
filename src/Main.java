/**
 * Created by k3rnel on 5/2/16.
 */
public class Main {

    public static void main(String [] args){
        ThreadFactory threadFactory = new ThreadFactory(50);

        Runnable r = new Runnable() {
            @Override
            public void run() {
                long randomNumber = (long)(Math.random() * 200);

                try {
                    Thread.sleep(randomNumber);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                threadFactory.freeOne();
            }
        };


        Runnable r1 = new Runnable() {
            @Override
            public void run() {
                Thread t;

                long timeIni, timeFin, timeTotal = 0;

                for (int i = 0; i < 40000; i++){
                    timeIni = System.currentTimeMillis();
                    t = threadFactory.getThread(r);
                    timeFin = System.currentTimeMillis();

                    timeTotal += timeFin - timeIni;
                    t.start();
                }

                System.out.println("Tiempo medio para conseguir un thread: " + ((timeTotal / 30) / 1000.0));
            }
        };

        Thread t1 = new Thread(r1);
        Thread t2 = new Thread(r1);
        Thread t3 = new Thread(r1);
        Thread t4 = new Thread(r1);

        t1.start();
        t2.start();
        t3.start();
        t4.start();

        System.out.println("Empiezo a esperar a los threads");
        threadFactory.joinAll();
        System.out.println("Threads Activos: " + threadFactory.getAliveThreads());

        try {
            t1.join();
            t2.join();
            t3.join();
            t4.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {

            Thread.sleep(10000);
            System.out.println("SSSSS");
            Thread.sleep(10000);
            System.out.println("SSSSS");

            Thread.sleep(10000);
            System.out.println("SSSSS");

            Thread.sleep(10000);
            System.out.println("SSSSS");

            Thread.sleep(10000);
            System.out.println("SSSSS");

            Thread.sleep(10000);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
