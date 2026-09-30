package practice.concurrency;

import java.util.Queue;
import java.util.concurrent.*;

public class ProducerConsumerTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        final BlockingQueue queue  = new ArrayBlockingQueue(20);

        ExecutorService ex = Executors.newFixedThreadPool(2);
        Producer producer = new Producer(queue);
        Consumer consumer = new Consumer(queue);
        Future<?> future = ex.submit(() -> {
            for (int i = 0; i < 10; i++) {
                producer.enque(i);
            }
        });

        Future<?> future2 = ex.submit(() -> {
            for (int i = 0; i < 10; i++) {
                consumer.deque();
            }
        });

        future.get();
        future2.get();

        ex.shutdown();



    }



}


class Producer {

    private BlockingQueue queue;

    public Producer(BlockingQueue queue) {
        this.queue = queue;
    }

    public void enque(int i) {
        try {
            queue.put(i);
            System.out.println("Enque : " + i);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

}

class Consumer {

    private BlockingQueue queue;

    public Consumer(BlockingQueue queue) {
        this.queue = queue;
    }

    public int deque() {
        int i =  (int) queue.poll();
        System.out.println("Deque : " + i);
        return i;
    }

}