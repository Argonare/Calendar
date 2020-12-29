package com.java.Calendar;

import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.text.SimpleDateFormat;

public class WriteToFIle {
    public WriteToFIle() {

    }
    public String read() throws IOException {
        File file=new File("config.config");
        if (file.exists()) {
            FileInputStream fileInputStream = new FileInputStream("config.config");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String tempString = "";
            String readJson = "";
            while ((tempString = reader.readLine()) != null) {
                readJson += tempString;
            }
            reader.close();
            fileInputStream.close();
            return readJson;
        }
        else{
            file.createNewFile();
            return "";
        }
    }
    public void save(String str) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter("config.config"));
        out.write(str);
        out.close();
    }
}
