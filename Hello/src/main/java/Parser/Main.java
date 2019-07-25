package Parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Main {
    private static int S;
    private static int a = 0;
    private static final String url = "http://news.admicro.vn:10002/api/realtime?domain=kenh14.vn";
    private static int count =0;
    private static final Logger logger = LoggerFactory.getLogger(Parse.class);

    private static final ScheduledExecutorService schedule = Executors.newScheduledThreadPool(2);
    public static void main (String[] args){
        final Parse data = new Parse();
        Main main = new Main();
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

        Thread dataParse = new Thread(new Runnable() {
            public void run() {
                //System.out.println("dataParse: "+ Thread.currentThread().getName());
                try{
                    JSONObject json = Parse.readFromUrl(url);
                    final JSONArray jsonArray = json.getJSONArray("source");
                    S=0;

                    for (int i = 0; i < jsonArray.length(); ++i) {
                        final JSONObject detail = jsonArray.getJSONObject(i);
                        S = S+detail.getInt("number");

                    }
                    System.out.println("Thread 1: " + S);

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread level = new Thread(new Runnable() {
            public void run() {
                //System.out.println("level:" + Thread.currentThread().getName());
                if( count == 0) logger.info("Thread 2: "+S + " ; " + count*2);
                else if((float) S/a >= 1.015)
                {
                    logger.info("Thread 2: "+S+" ; "+count*2);

                }
                else if(count%6 == 0) {
                    logger.debug("Thread 2: "+S + " ; " + count*2);

                }
                else System.out.println("Thread 2: " + S + " ; " + count*2);

                a = S;
                count++;
            }
        });

        executorService.scheduleAtFixedRate(dataParse,0,1,TimeUnit.SECONDS);
        executorService.scheduleAtFixedRate(level,0,2,TimeUnit.SECONDS);

        try {
            executorService.awaitTermination(30,TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorService.shutdown();
    }
}
