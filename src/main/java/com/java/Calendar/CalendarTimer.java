package com.java.Calendar;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import javax.swing.*;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;

public class CalendarTimer extends TimerTask  {
    String key;
    String value;
    public CalendarTimer(String key,String value){
        this.key=key;
        this.value=value;
    }
    @Override
    public void run() {
        try {
            BufferedInputStream buffer = new BufferedInputStream(new FileInputStream("src/main/resources/1.mp3"));
            Player player = new Player(buffer);
            new Thread(()->{
                try {
                    player.play();
                } catch (JavaLayerException e) {
                    e.printStackTrace();
                }
            }).start();
            int f=JOptionPane.showConfirmDialog(null, key+"    "+value, "", JOptionPane.YES_OPTION);
            if (f==JOptionPane.YES_OPTION||f==JOptionPane.NO_OPTION){
                player.close();
                this.cancel();
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
