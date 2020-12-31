package com.java.Calendar;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class CalendarMain {
    public static void main(String args[]) throws IOException, SQLException {
        CalendarFrame frame = new CalendarFrame();
        frame.setBounds(100, 100, 1000, 600);
        frame.setTitle("日历小程序");
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}