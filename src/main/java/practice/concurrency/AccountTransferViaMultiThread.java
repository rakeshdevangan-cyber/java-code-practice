package practice.concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReentrantLock;

public class AccountTransferViaMultiThread {

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        Account account1 = new Account(1, 1000);
        Account account2 = new Account(2, 1000);
        TransferService ts =  new TransferService();

        //transferUsingbasicThreads(ts, account1, account2);

        //transferUsingThreadPool(ts, account1, account2);
       // transferUsingThreadPoolWithDeadLock(ts, account1, account2);
        transferUsingReentrantLock(ts, account1, account2);

    }

    private static void transferUsingReentrantLock(TransferService ts, Account account1, Account account2) throws InterruptedException, ExecutionException {
        ExecutorService es = Executors.newFixedThreadPool(20);

        List<Future<?>> futures = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            // A -> B
            futures.add(es.submit(() ->
                    ts.transferWithReentrantLock(
                            account1,
                            account2,
                            100)));

            // B -> A
            futures.add(es.submit(() ->
                    ts.transferWithReentrantLock(
                            account2,
                            account1,
                            100)));
        }

        for (Future fe : futures) {
            fe.get();
        }
        System.out.println("Account 1 Balance : "+ account1.getBalance());
        System.out.println("Account 2 Balance : "+ account2.getBalance());
        es.shutdown();
    }

    private static void transferUsingThreadPoolWithDeadLock(TransferService ts, Account account1, Account account2) throws InterruptedException, ExecutionException {
        ExecutorService es = Executors.newFixedThreadPool(20);

        List<Future<?>> futures = new ArrayList<>();
        for (int i = 0; i < 100000; i++) {
            // A -> B
            futures.add(es.submit(() ->
                    ts.transferWithDeadlock(
                            account1,
                            account2,
                            100)));

            // B -> A
            futures.add(es.submit(() ->
                    ts.transferWithDeadlock(
                            account2,
                            account1,
                            100)));
        }

        for (Future fe : futures) {
            fe.get();
        }
        System.out.println("Account 1 Balance : "+ account1.getBalance());
        System.out.println("Account 2 Balance : "+ account2.getBalance());
        es.shutdown();
    }

    private static void transferUsingThreadPool(TransferService ts, Account account1, Account account2) throws InterruptedException, ExecutionException {
        ExecutorService es = Executors.newFixedThreadPool(20);

        List<Future<?>> futures = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            // A -> B
            futures.add(es.submit(() ->
                    ts.transfer(
                            account1,
                            account2,
                            100)));

            // B -> A
            futures.add(es.submit(() ->
                    ts.transfer(
                            account2,
                            account1,
                            100)));
        }

        for (Future fe : futures) {
            fe.get();
        }
        System.out.println("Account 1 Balance : "+ account1.getBalance());
        System.out.println("Account 2 Balance : "+ account2.getBalance());
        es.shutdown();
    }

    private static void transferUsingbasicThreads(TransferService ts, Account account1, Account account2) throws InterruptedException {
        List<Thread> threads1 = new ArrayList<>();
        List<Thread> threads2 = new ArrayList<>();

        for (int i = 0; i < 1000; i++) {
            Thread t1 = new Thread(() -> {
                ts.transfer(account1, account2, 100);
            });

            Thread t2 = new Thread(() -> {
                ts.transfer(account2, account1, 100);
            });

            t1.start();
            t2.start();

            threads1.add(t1);
            threads2.add(t2);
           /* t1.join();
            t2.join();*/
        }

        for (Thread t1 : threads1) {
            t1.join();
        }

        for (Thread t2 : threads2) {
            t2.join();
        }


        System.out.println("Account 1 Balance : "+ account1.getBalance());
        System.out.println("Account 2 Balance : "+ account2.getBalance());
    }
}

class Account {

    private int id;
    private double balance;

    private final ReentrantLock lock =
            new ReentrantLock();

    public Account(int id, double balance) {
        this.id = id;
        this.balance= balance;
    }

    public int getId() {
        return id;
    }

    public double getBalance() {
        return balance;
    }

    public void debit(double amount) {
        balance = balance - amount;
    }

    public void credit(double amount) {
        balance = balance +amount;
    }

    public ReentrantLock getLock() {
        return lock;
    }
}

 class TransferService {

     public  void transfer(Account from, Account to, double amount) {
         System.out.println(
                 Thread.currentThread().getName()
                         + " transferring "
                         + amount);
         Account accountFirst;
         Account accountSecond;
         int idFrom = from.getId();
         int idTo = to.getId();
         if(idFrom < idTo) {
             accountFirst = from;
             accountSecond = to;
         } else {
             accountFirst = to;
             accountSecond = from;
         }

         synchronized (accountFirst) {
             synchronized (accountSecond) {
                 /*if (from.getBalance() < amount) {
                     throw new IllegalStateException(
                             "Insufficient balance");
                 }*/
                 from.debit(amount);
                 to.credit(amount);
             }
         }
     }//

     public  void transferWithDeadlock(Account from, Account to, double amount) {
         System.out.println(
                 Thread.currentThread().getName()
                         + " transferring "
                         + amount);

         synchronized (from) {
             System.out.println(Thread.currentThread().getName() + " locked account "+from.getId());

             try {
                 Thread.sleep(100);
             } catch(InterruptedException e) {
                 Thread.currentThread().interrupt();
             }

             synchronized (to) {
                 System.out.println(
                         Thread.currentThread().getName()
                                 + " transferring "
                                 + amount);
                 from.debit(amount);
                 to.credit(amount);
             }
         }
     }//

     public void transferWithReentrantLock(
             Account from,
             Account to,
             double amount) {

         Account first;
         Account second;

         // Deterministic lock ordering
         if (from.getId() < to.getId()) {
             first = from;
             second = to;
         } else {
             first = to;
             second = from;
         }

         boolean firstLocked = false;
         boolean secondLocked = false;

         try {

             firstLocked = first.getLock().tryLock();

             if (!firstLocked) {
                 throw new IllegalStateException(
                         "Could not acquire first account lock");
             }

             secondLocked = second.getLock().tryLock();

             if (!secondLocked) {
                 throw new IllegalStateException(
                         "Could not acquire second account lock");
             }

             if (from.getBalance() < amount) {
                 throw new IllegalStateException(
                         "Insufficient balance");
             }

             from.debit(amount);
             to.credit(amount);

         } finally {

             if (secondLocked) {
                 second.getLock().unlock();
             }

             if (firstLocked) {
                 first.getLock().unlock();
             }
         }
     }
 }