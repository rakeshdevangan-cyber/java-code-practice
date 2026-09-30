package practice.concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

public class CounterIncrementViaMultiThread {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        ExecutorService es = Executors.newFixedThreadPool(10);
        Counter counter = new Counter();
        List<Future<?>>  futures = new ArrayList<>();
        for (int i = 0; i < 10; i++) {

           Future<?> future =  es.submit(() -> {

                for (int j = 0; j < 1000; j++) {
                    counter.incrementCount();
                }

            });
           futures.add(future);
        }
        for (Future<?> future : futures) {
            future.get();
        }
        System.out.println("Count Value :"+counter.getCount());
        es.shutdown();


    }
}

/*class Counter {
    int count = 0;

    public synchronized void incrementCount() {
        count++;
    }

    public int getCount() {
        return count;
    }
}*/

class Counter {
    private final AtomicInteger count = new AtomicInteger();

    public  void incrementCount() {
        count.getAndIncrement();
    }

    public int getCount() {
        return count.get();
    }
}
