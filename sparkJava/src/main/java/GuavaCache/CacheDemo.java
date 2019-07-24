package GuavaCache;

import com.google.common.cache.LoadingCache;
import spark.Service;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static spark.Service.ignite;
import static spark.Spark.*;

public class CacheDemo {

    public static  void main() {


        Thread startServer = new Thread(new Runnable() {
            @Override
            public void run() {
                Service port = ignite().port(8080);
                port.get("/prime", (request, response) -> {
                    String id = request.queryParams("n");
                    int param = Integer.parseInt(id);
                    return getValueGuava(param);
                });
                System.out.println("Server started.");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread getResponse = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try {
                        BenchMark.readUrl();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        executorService.scheduleAtFixedRate(startServer,0,1, TimeUnit.SECONDS);
        executorService.scheduleAtFixedRate(getResponse,0,1,TimeUnit.SECONDS);

        try {
            executorService.awaitTermination(5,TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorService.shutdown();
    }

    private static LoadingCache<Integer,Integer> paramCache = cacheTest.getParamCache();

    public static  Integer getValueGuava(int param) throws ExecutionException {
        System.out.println("Cache size = "+ paramCache.size());
        if (paramCache.asMap().putIfAbsent(param, cacheTest.getValue(param)) == null){
            System.out.println(param+" Not existed");          // if the key does not exist
            return paramCache.get(param);               // add it to cache
        }
        else {
            System.out.println(param +" Presented");            // if the key exists
            return paramCache.getIfPresent(param);      //
        }

    }


}
