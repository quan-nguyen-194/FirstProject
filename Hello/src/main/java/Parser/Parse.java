package Parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;

public class Parse {
    private static int S;
    private static final String url = "http://news.admicro.vn:10002/api/realtime?domain=kenh14.vn";
    private static int count =0;
    private static final Logger logger = LoggerFactory.getLogger(Parse.class);

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



    public void webParse(){
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
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setLevel(){
        int a = 0;

        synchronized (this){
            if (a!=0){
                if ((S-a)/a > 0.015){
                    logger.info("INFO");
                    count =0;
                }
                else count ++;
                if (count ==6){
                    logger.debug("DEBUG");
                }
            }
            a=S;
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
