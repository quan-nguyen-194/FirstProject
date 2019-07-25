package MavenParse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class App {
    //private static LocalTime t0 = LocalTime.now();
    private static int S;
    private static final String url = "http://news.admicro.vn:10002/api/realtime?domain=kenh14.vn";

    public static JSONObject readFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String text = br.readLine();
            JSONObject json = new JSONObject(text);

            return json;
        }finally {
            is.close();
        }

    }
    private static int count =0;
    private static final Logger logger = LoggerFactory.getLogger(App.class);
    public static void main(String[] args){
        Runnable runnable1 = new Runnable() {
            public void run() {
                try{
                    JSONObject json = readFromUrl(url);
                    final JSONArray jsonArray = json.getJSONArray("source");
                    S=0;

                    for (int i = 0; i < jsonArray.length(); ++i) {
                        final JSONObject detail = jsonArray.getJSONObject(i);
                        S = S+detail.getInt("number");

                    }
                    //LocalTime t = LocalTime.now();
                    System.out.println(S);
                    int a=0;
                    if(a!=0){
                        if ((S-a)/a>0.015){
                            logger.info(S + " " + a);
                        }else count ++;
                        if(count==6){
                            logger.debug(S + " " +a);
                        }
                    }
                    a=S;
                }catch(IOException e){
                    e.getStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        /*Runnable runnable2 = new Runnable() {
            public void run() {

                int a = 0;
                if (a!=0){
                    if (a/S > 0.015){
                        logger.info("INFO");
                        count =0;
                    }
                    else count ++;
                    if (count ==6){
                        logger.debug("DEBUG");
                    }
                }
                a=S;
            }
        } ;*/

        ScheduledExecutorService ses1 = Executors.newSingleThreadScheduledExecutor();
        ses1.scheduleAtFixedRate(runnable1,0,2, TimeUnit.SECONDS);
        try {
            ses1.awaitTermination(10,TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        /*ScheduledExecutorService ses2 = Executors.newSingleThreadScheduledExecutor();
        ses2.scheduleAtFixedRate(runnable2,1,2, TimeUnit.SECONDS);
        try {
            ses2.awaitTermination(20,TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        ses1.shutdown();
        //ses2.shutdown();
    }
}
