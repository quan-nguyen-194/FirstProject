package Extractor;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Execute {
    public static void main(String[] args) throws IOException {
        Document doc = Jsoup.connect("http://dantri.com.vn").get();
        Element body = doc.body();
        Elements paragraphs = body.getElementsByTag("body");
        String title = doc.title();
        String text = "";
        for(Element paragraph: paragraphs){
            // System.out.print(paragraph.text());
            text += paragraph.text();
        }
        DateFormat dateFormat = new SimpleDateFormat("hh:mm dd-MM-YYYY");
        Date date = new Date();
        String FileName = dateFormat.format(date);
        //long time = System.currentTimeMillis();
        File file = new File("/home/quan/"+ FileName+".txt");
        createFile.writeToFile(file,text);
        System.out.println(FileName);
    }
}
