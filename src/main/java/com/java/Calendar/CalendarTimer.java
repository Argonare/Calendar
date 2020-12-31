package com.java.Calendar;

import javazoom.jl.player.Player;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;

public class CalendarTimer extends TimerTask {
    Map<String,String> data;
    Map<String ,Integer> flags;
    public CalendarTimer( Map<String,String> data){
        if (data==null)
            data=new HashMap<>();
        this.data=data;
        flags=new HashMap<>();
        for (Map.Entry<String,String>entry:data.entrySet()){
            flags.put(entry.getKey(),0);
        }
    }
    @Override
    public void run() {
        Calendar calendar = Calendar.getInstance();
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH)+1;
        int day=calendar.get(Calendar.DATE);

        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        for (Map.Entry<String,String>entry:data.entrySet()){
            String key= entry.getKey();
            int len=key.split(":").length;
            if (len==2){
                try {
                    int hours=Integer.parseInt(key.split(":")[0]);
                    int minutes=Integer.parseInt(key.split(":")[1]);
                    if(hour*60+minute-hours*60-minutes<=60){
                        play();
                    }
                }catch (NumberFormatException e){
                    e.printStackTrace();
                }
            }
        }
    }
    public void play() {//播放mp3文件
        try {
            BufferedInputStream buffer = new BufferedInputStream(new FileInputStream("当我想你的时候.mp3"));
            Player player = new Player(buffer);
            player.play();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
