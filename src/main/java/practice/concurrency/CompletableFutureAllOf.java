package practice.concurrency;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class CompletableFutureAllOf {
    public static void main(String[] arg) throws ExecutionException, InterruptedException {
        CompletableFuture<String> name  =  getName();

        CompletableFuture<String> surname  =  getSurname();

        CompletableFuture<Void> allOf = CompletableFuture.allOf(name, surname);

        allOf.get();
        System.out.println("All Of"+ name.get() + surname.get());


        CompletableFuture<String>  result = allOf.thenApply((s) -> name.join() + " "+surname.join());

        System.out.println("Join"+ result.join());



    }


    public static CompletableFuture<String> getName() {
        return CompletableFuture.supplyAsync( () -> "Rakesh");
    }

    public static CompletableFuture<String> getSurname() {
        return CompletableFuture.supplyAsync( () -> "Dewangan");
    }
}
