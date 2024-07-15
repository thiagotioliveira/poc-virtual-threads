package com.xgeeks.poc.thiagooliveira.virtualthreads;

import java.util.ArrayList;

public class PocThreads {

    private static final int INDEX_THREAD_TYPE = 0;
    private static final int INDEX_LIMIT = 1;

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Starting main\n");

        var threads = new ArrayList<Thread>();

        int limit = getLimit(args);
        boolean virtualThread = isVirtualThread(args);
        sleep(3000);//3s

        for (int i = 0; i < limit; i++){
            var thread = createThread(virtualThread);

            if(!virtualThread){
                thread.start();
            }

            threads.add(thread);
        }
        if(virtualThread) {
            for (Thread thread : threads) {
                thread.join();
            }
        }

        System.out.println("Ending main");
    }

    private static Thread createThread(boolean virtualThreads) {
        if(virtualThreads){
            return Thread.startVirtualThread(PocThreads::handleUserRequest);
        } else {
            return new Thread(PocThreads::handleUserRequest);
        }
    }

    private static void handleUserRequest(){
        System.out.println("Starting thread " + Thread.currentThread());

        sleep(15_000);//15s - This represents I/O operation

        System.out.println("Ending thread " + Thread.currentThread());
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean isVirtualThread(String[] args) {
        boolean result = false;
        if(args != null && args.length > 0){
            result = Boolean.parseBoolean(args[INDEX_THREAD_TYPE]);
        }
        System.out.println("Using virtual threads? " + result);
        return result;
    }
    private static int getLimit(String[] args) {
        int limit = 100;//default
        if(args != null && args.length > 1){
            limit = Integer.parseInt(args[INDEX_LIMIT]);
        }
        System.out.println("Limit: " + limit);
        return limit;
    }

}
