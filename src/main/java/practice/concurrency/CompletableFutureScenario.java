package practice.concurrency;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static java.lang.Thread.sleep;

public class CompletableFutureScenario {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Customer customer = new Customer();
        CompletableFuture<String> name =  customer.getCustomer();
        CompletableFuture<Double> balance = customer.getAccountBalance();

        CompletableFuture<String> result = combineBothResults(name, balance);

        CompletableFuture<String> result2 = getStringCompletableFutureUsingCompose(name, customer);

        System.out.println(result.get());
        System.out.println(result2.get());

    }

    private static CompletableFuture<String> getStringCompletableFutureUsingCompose(CompletableFuture<String> name, Customer customer) {
        CompletableFuture<String> result2 = name.thenCompose((nameResult) -> {
           return customer.getAccountBalance2(nameResult);
        });
        return result2;
    }

    private static CompletableFuture<String> combineBothResults(CompletableFuture<String> name, CompletableFuture<Double> balance) {
        CompletableFuture<String> result = name.thenCombine(balance, (name2, balance2) -> {

            return "Customer: " + name2
                    + ", Balance: " + balance2;
        });
        return result;
    }


}

class Customer {


    public CompletableFuture<String> getCustomer() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "Rakesh";
        });
    }

    public CompletableFuture<Double> getAccountBalance() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return 5000.0;
        });
    }

    public CompletableFuture<String> getAccountBalance2(String name) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return name+ 5000.0;
        });
    }
}
