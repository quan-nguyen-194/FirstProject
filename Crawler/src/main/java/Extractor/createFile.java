package Extractor;

import java.io.*;


public class createFile {
    /*public static void create() throws IOException {
        File file = new File("D://" + LocalTime.now()+ ".txt");
        file.createNewFile();
        if (file.exists()){
            System.out.println("The file is created successfully");
        }
    }*/

    public static void writeToFile(File file, String text) throws FileNotFoundException {
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        FileWriter fw = null;
        try {
            fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(text);
            System.out.println("Content is written to file");
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
