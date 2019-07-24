package GuavaCache;


import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static spark.Service.ignite;
import static spark.Spark.*;
//@State(Scope.Benchmark)
public class BenchMark
{

    @org.openjdk.jmh.annotations.Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    //@Fork(1)
    @Warmup(iterations = 3)
    @Measurement(iterations = 5)
    public static void init() {
        Thread startServer = new Thread(new Runnable() {
            @Override
            public void run() {
                port(8080);
                get("/prime", (request, response) -> {
                    String param = request.queryParams("n");
                    return CacheDemo.getValueGuava(Integer.parseInt(param));
                });
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    System.out.println("Interrupted!");
                }
                //System.out.println("Server started.");

            }
        });

        Thread getResponse = new Thread(new Runnable() {
            @Override
            public void run() {
                Runnable task1 = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            readUrl();
                            System.out.println("Task 1");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                };

                Runnable task2 = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            readUrl();
                            System.out.println("Task 2");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                };
                Runnable task3 = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            readUrl();
                            System.out.println("Task 3");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                };
                Runnable task4 = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            readUrl();
                            System.out.println("Task 4");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                };
                ExecutorService executorService = Executors.newFixedThreadPool(4);
                executorService.submit(task1);
                executorService.submit(task2);
                executorService.submit(task3);
                executorService.submit(task4);
                executorService.shutdown();

            }
        });

        startServer.start();
        try {
            startServer.sleep(2000);
        } catch (InterruptedException e) {
            System.out.println("Interrupted!");
        }
        getResponse.start();
    }

    public static String setLink(){
        Random random = new Random();
        int param = random.nextInt(10000);
        String url = "http://localhost:8080/prime?n="+param;
        return url;
    }

    public static void readUrl() throws IOException {
        BufferedReader br = null;
        StringBuilder stringBuilder;

        URL myUrl = new URL(setLink());
        HttpURLConnection con = (HttpURLConnection) myUrl.openConnection();
        con.setRequestMethod("GET");
        con.setReadTimeout(2000);
        con.connect();
        br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        stringBuilder = new StringBuilder();
        String line ;
        while((line = br.readLine())!=null){
            stringBuilder.append(line+"\n");
        }
        System.out.println(stringBuilder.toString());

    }

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(GuavaCache.BenchMark.class.getSimpleName())
                .forks(0)
                .build();

        new Runner(options).run();



    }
}

