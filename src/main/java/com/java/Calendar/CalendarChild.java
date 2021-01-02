/*
 * Created by JFormDesigner on Tue Dec 29 12:43:12 CST 2020
 */

package com.java.Calendar;

import com.alibaba.fastjson.JSONObject;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;

/**
 * @author unknown
 */
public class CalendarChild extends JFrame {
    Map<String, String> map;
    CalendarFrame father;
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JScrollPane scrollPane1;
    private JTable table1;
    private JButton save,addLine;
    public CalendarChild(String map, CalendarFrame father) {
        this.father=father;
        this.map= (Map<String, String>) JSONObject.parse(map);
        initComponents();
    }

    private void initComponents() {
        DefaultTableModel model = new DefaultTableModel();
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        scrollPane1 = new JScrollPane();
        table1 = new JTable(model);
        save = new JButton();
        addLine = new JButton();
        Vector data = new Vector();
        JScrollPane scroll = new JScrollPane(table1);
        add(scroll);
        if (map != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                Vector row = new Vector();
                row.add(entry.getKey());
                row.add(entry.getValue());
                data.add(row);
                System.out.println(entry.getKey());
            }
        } else map = new HashMap<>();
        Vector names = new Vector();
        names.add("时间");
        names.add("事件");
        model.setDataVector(data, names);


        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
            dialogPane.addPropertyChangeListener(e -> {
                if ("bord\u0065r".equals(e.getPropertyName())) throw new RuntimeException();
            });
            dialogPane.setLayout(new BorderLayout());
            {
                contentPanel.setLayout(null);
                {
                    scrollPane1.setViewportView(table1);
                }
                contentPanel.add(scrollPane1);
                scrollPane1.setBounds(5, 5, scrollPane1.getPreferredSize().width, 265);
                save.setText("保存");
                contentPanel.add(save);
                addLine.setText("添加");
                contentPanel.add(addLine);
                save.setBounds(new Rectangle(new Point(105, 285), save.getPreferredSize()));
                addLine.setBounds(new Rectangle(new Point(255, 285), save.getPreferredSize()));
                save.addActionListener(e -> {
                    int row = table1.getRowCount();
                    if (map==null)
                        map=new HashMap<>();
                    for (int i = 0; i < row; i++) {
                        String time = (String) table1.getValueAt(i, 0);
                        String content = (String) table1.getValueAt(i, 1);
                        try{
                            int len=time.split(":").length;
                            if(len!=2){
                                JOptionPane.showMessageDialog(null, "日期格式错误，应为xx:xx", "", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                            int hour=Integer.parseInt(time.split(":")[0]);
                            int minutes=Integer.parseInt(time.split(":")[1]);
                            if(hour<0||hour>24||minutes<0||minutes>60){
                                JOptionPane.showMessageDialog(null, "时间错误", "", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                        }catch (NumberFormatException ne){
                            JOptionPane.showMessageDialog(null, "日期格式错误，应为xx:xx", "", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        if (content==null||content.trim().equals("")){
                            JOptionPane.showMessageDialog(null, "内容不得为空", "", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        map.put(time, content);
                    }
                    try {
                        father.data.put(father.last_clicked_date,JSONObject.toJSONString(map));
                        father.writer.save(JSONObject.toJSONString(father.data));
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                });
                addLine.addActionListener(e->{
                    Vector data1 = new Vector();
                    Vector rows=new Vector();
                    int row = table1.getRowCount();
                    for (int i = 0; i < row; i++) {
                        String time = (String) table1.getValueAt(i, 0);
                        String content = (String) table1.getValueAt(i, 1);
                        rows.add(time);
                        rows.add(content);
                        data1.add(rows);
                        rows = new Vector();
                    }
                    rows.add("");
                    rows.add("");
                    data1.add(rows);
                    model.setDataVector(data1, names);
                });
                {
                    Dimension preferredSize = new Dimension();
                    for (int i = 0; i < contentPanel.getComponentCount(); i++) {
                        Rectangle bounds = contentPanel.getComponent(i).getBounds();
                        preferredSize.width = Math.max(bounds.x + bounds.width + 5, preferredSize.width);
                        preferredSize.height = Math.max(bounds.y + bounds.height + 5, preferredSize.height);
                    }
                    Insets insets = contentPanel.getInsets();
                    preferredSize.width += insets.right;
                    preferredSize.height += insets.bottom;
                    contentPanel.setMinimumSize(preferredSize);
                    contentPanel.setPreferredSize(preferredSize);
                }
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
    }



}
