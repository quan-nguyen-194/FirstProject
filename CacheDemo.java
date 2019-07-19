package GuavaCache;

import com.google.common.cache.LoadingCache;

import java.util.Random;
import java.util.concurrent.ExecutionException;

import static spark.Spark.*;

public class CacheDemo {


    /*public static void main(String[] args){
        CacheDemo test = new CacheDemo();
        try {
            Random ramdom = new Random();
            for(int i = 0;i<15;i++){
                int param = ramdom.nextInt(50);
                System.out.println(param);
                System.out.println(test.getValueGuava(param));
                System.out.println("-----------------------");
            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }*/

    public static  void main(String[] args) {


        port(8080);
        get("prime", "application/json", (request, response)->{
            String id = request.queryParams("n");
            int param = Integer.parseInt(id);
            return getValueGuava(param);
        });

        stop();
    }

    private static LoadingCache<Integer,Integer> paramCache = cacheTest.getParamCache();

    private static  Integer getValueGuava(int param) throws ExecutionException {
        System.out.println("Cache size = "+ paramCache.size());
        if (paramCache.asMap().putIfAbsent(param, cacheTest.getValue(param)) == null){
            System.out.println("Not existed");          // if the key does not exist
            return paramCache.get(param);               // add it to cache
        }
        else {
            System.out.println("Presented");            // if the key exists
            return paramCache.getIfPresent(param);      //
        }

    }


}
