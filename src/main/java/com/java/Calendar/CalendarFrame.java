package com.java.Calendar;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import javazoom.jl.player.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Timer;

class CalendarFrame extends JFrame implements ActionListener {
    LunarDate nongli = new LunarDate();
    JLabel labelDay[] = new JLabel[42], titleName[] = new JLabel[7];
    JTextField text = new JTextField(10);
    JPanel pCenter = new JPanel();
    String name[] = {"日", "一", "二", "三", "四", "五", "六"};
    JButton button, nextMonth, previousMonth, save = new JButton("编辑"), but[] = new JButton[42];
    CalendarBean calendar;
    Calendar calendars = Calendar.getInstance();
    int year = calendars.get(Calendar.YEAR), month = calendars.get(Calendar.MONTH) + 1;
    JLabel lbl1 = new JLabel("请输入年份："), lbl2 = new JLabel("      "), showMessage = new JLabel("", JLabel.CENTER);
    JTextArea scheduletext = new JTextArea();

    Map<String, String> data = null;
    WriteToFIle writer = new WriteToFIle();
    String last_clicked_date = "";

    public CalendarFrame() throws IOException {
        JPanel pNorth = new JPanel(), pSouth = new JPanel(), pRight = new JPanel();
        nextMonth = new JButton("下月");
        previousMonth = new JButton("上月");
        button = new JButton("确定");
        nextMonth.addActionListener(this);
        previousMonth.addActionListener(this);
        button.addActionListener(this);

        JLabel scheduleLabel = new JLabel("日程安排");
        JScrollPane jsp = new JScrollPane(scheduletext);
        jsp.setViewportView(scheduletext);
        jsp.setPreferredSize(new Dimension(180, 240));
        JPanel jp = new JPanel();
        jp.setLayout(new BorderLayout(0, 0));
        jp.add(scheduleLabel, BorderLayout.NORTH);
        scheduletext.setEnabled(false);
        scheduletext.setDisabledTextColor(Color.BLACK);
        jp.add(save, BorderLayout.SOUTH);
        jp.add(jsp, BorderLayout.CENTER);
        pRight.add(jp);
        pNorth.add(showMessage);
        pNorth.add(lbl2);
        pNorth.add(previousMonth);
        pNorth.add(nextMonth);
        pSouth.add(lbl1);
        pSouth.add(text);
        pSouth.add(button);
        pCenter.setLayout(new GridLayout(7, 7));
        text.addActionListener(this);
        calendar = new CalendarBean();
        calendar.setYear(year);
        calendar.setMonth(month);
        String day[] = calendar.getCalendar();
        resetPanel(day, pCenter);
        showMessage.setText("日历：" + calendar.getYear() + "年"
                + calendar.getMonth() + "月" + '/' + nongli.cyclical(year) + '(' + nongli.AnimalsYear(year) + ')' + "年" + nongli.nStr1[month] + "月");
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.add(pCenter);
        add(scrollPane, BorderLayout.CENTER);
        add(pNorth, BorderLayout.NORTH);
        add(pSouth, BorderLayout.SOUTH);
        add(pRight, BorderLayout.EAST);
        loadData();
        last_clicked_date = year + "-" + month + "-" + calendars.get(Calendar.DATE);
        save.addActionListener(e -> {
            String s = data.get(last_clicked_date);
            CalendarChild c = new CalendarChild(s, this);
            c.setVisible(true);
        });
    }

    /**
     * 加载数据
     **/
    public void loadData() throws IOException {
        String s = writer.read();
        if (s.equals(""))
            data = new HashMap<>();
        else
            data = JSONObject.parseObject(writer.read(), new TypeReference<Map<String, String>>() {
            });
        Timer timer = new Timer();
        String today = calendars.get(Calendar.YEAR) + "-" + (calendars.get(Calendar.MONTH) + 1) + "-" + calendars.get(Calendar.DATE);
        Map<String, String> schedul = JSON.parseObject(data.get(today), new TypeReference<Map<String, String>>() {});
        schedul=sortMapByKey(schedul);
        for (Map.Entry<String, String> entry : schedul.entrySet()) {
            String key = entry.getKey();
            int len = key.split(":").length;
            if (len == 2) {
                try {
                    int hours = Integer.parseInt(key.split(":")[0]);
                    int minutes = Integer.parseInt(key.split(":")[1]);
                    int dely=hours*60+minutes-calendars.get(Calendar.HOUR)*60-calendars.get(Calendar.MINUTE);
                    CalendarTimer t=new CalendarTimer(key,entry.getValue());
                    if (dely<60&&dely>0){
                        Date tms = new Date(System.currentTimeMillis());
                        timer.schedule(t,tms);
                    }else if (dely>=60){
                        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date tms = sdf.parse(today+" "+(hours-1)+":"+minutes+":00");
                        timer.schedule(t,tms);
                    }
                } catch (NumberFormatException | ParseException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    /*
    刷新日历
     */
    public void resetPanel(String day[], JPanel pCenter) {
        pCenter.removeAll();
        pCenter.repaint();
        int labelCount = 0;
        int butCount = 0;
        String nday = null;
        for (int i = 0; i < 7; i++) {
            titleName[i] = new JLabel(name[i]);
            titleName[i].setHorizontalAlignment(SwingConstants.CENTER);
            pCenter.add(titleName[i]);
        }

        for (int i = 0; i < 42; i++) {
            if (day[i] == null ||
                    (this.year == calendars.get(Calendar.YEAR) && this.month == calendars.get(Calendar.MONTH) + 1 && Integer.parseInt(day[i]) < calendars.get(Calendar.DATE)) ||//日期对的上
                    this.year < calendars.get(Calendar.YEAR) ||//年分对的上
                    (this.year == calendars.get(Calendar.YEAR) && this.month < calendars.get(Calendar.MONTH) + 1))//月份对的上
            {
                labelDay[labelCount] = new JLabel("", JLabel.CENTER);

                //农历
                if (day[i] != null) {
                    nday = nongli.oneDay(year, month, Integer.parseInt(day[i]));

                    //添加日期
                    labelDay[labelCount].setText(day[i] + "/" + nday);


                }
                pCenter.add(labelDay[labelCount]);
                labelCount++;
            } else {
                but[butCount] = new JButton("");
                if (day[i] != null) {
                    nday = nongli.oneDay(year, month, Integer.parseInt(day[i]));

                    //添加日期
                    but[butCount].setText(day[i] + "/" + nday);
                }
                pCenter.add(but[butCount]);
                butCount++;
            }
        }
        for (int i = 0; i < butCount; i++) {
            String nbut = but[i].getText();
            String substring = nbut.substring(0, nbut.indexOf('/'));
            but[i].addMouseListener(new EL1(Integer.parseInt(substring), this));
        }
    }
    public static Map<String, String> sortMapByKey(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return new HashMap<>();
        }
        Map<String, String> sortMap = new TreeMap<String, String>(new MapKeyComparator());
        sortMap.putAll(map);
        return sortMap;
    }
    static class MapKeyComparator implements Comparator<String>{
        @Override
        public int compare(String str1, String str2) {
            return str1.compareTo(str2);
        }
    }
    /*
    日历点击事件
     */
    class EL1 extends MouseAdapter {
        int day;
        CalendarFrame frame;

        public EL1(int day, CalendarFrame calendarFrame) {
            this.day = day;
            this.frame = calendarFrame;
        }

        public void mouseClicked(MouseEvent e) {
            String outStr = "";
            if (e.getButton() == MouseEvent.BUTTON1) {
                last_clicked_date = (year + "-" + month + "-" + day);
                JSONObject mp = JSONObject.parseObject(data.get(year + "-" + month + "-" + day));
                String out = "";
                if (mp != null)
                    for (Map.Entry<String, Object> entry : mp.entrySet()) {
                        String mapKey = entry.getKey();
                        String mapValue = (String) entry.getValue();
                        out += mapKey + "\n" + mapValue + "\n----------\n";
                    }
                out += "";
                scheduletext.setText(out);
            }
            if (e.getClickCount() == 2) {
                String s = data.get(last_clicked_date);
                CalendarChild c = new CalendarChild(s, frame);
                c.setVisible(true);
            }
        }
    }

    /*
    上月下月和搜索年份
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == nextMonth) {
            month = month + 1;
            if (month > 12) {
                month = 1;
                year += 1;
            }
            calendar.setMonth(month);
            calendar.setYear(year);
            String day[] = calendar.getCalendar();
            resetPanel(day, pCenter);

        } else if (e.getSource() == previousMonth) {
            month = month - 1;
            if (month < 1) {
                month = 12;
                year -= 1;
            }
            calendar.setMonth(month);
            calendar.setYear(year);
            String day[] = calendar.getCalendar();
            resetPanel(day, pCenter);
        } else if (e.getSource() == button && text.getText() != null
                && !text.getText().equals("")) {
            month = month + 1;
            if (month > 12)
                month = 1;
            calendar.setYear(Integer.parseInt(text.getText()));
            String day[] = calendar.getCalendar();
            resetPanel(day, pCenter);
        }
        showMessage.setText("日历：" + calendar.getYear() + "年"
                + calendar.getMonth() + "月" + '/' + nongli.cyclical(year) + '(' + nongli.AnimalsYear(year) + ')' + "年" + nongli.nStr1[month] + "月");

    }
}
