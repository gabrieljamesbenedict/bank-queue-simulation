package org.gjbmloslos.bankqueuesim.sample;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SampleTest {

    static ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
    static Random r = new Random();

    static void hello(int id, int delay) {
        Runnable run = new Runnable() {
            @Override
            public void run() {
                System.out.println("Hello " + id);
                System.out.println("Delay of " + delay);
                hello(id+1, r.nextInt(10));
            }
        };
        service.schedule(run, delay, TimeUnit.SECONDS);
    }

    public static void main(String[] args) {
        hello(0,1);
    }

}
