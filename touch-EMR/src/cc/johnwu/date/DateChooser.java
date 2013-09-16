/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * NewJPanel.java
 *
 * Created on 2009/9/4, 下午 02:44:55
 */
package cc.johnwu.date;

import java.text.*;
import java.util.Calendar;
import java.util.Date;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DateChooser extends JPanel {

    private int width = 240; //日期控件的宽度
    private int height = 240; //日期控件的高度
    private String colname[] = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    private Font m_font = new java.awt.Font("宋体", java.awt.Font.PLAIN, 12);
    private javax.swing.JDialog dateFrame;
    private String parten;
    private Container owner;
    private DateInterface m_Frame;
    private int startYear = 1900; //最小年份
    private int lastYear = 2100; //最大年份
    private Color backGroundColor = Color.gray; //底色
    //月曆表格配色----------------//
    private Color palletTableColor = Color.white; //日期表底色
    private Color weekFontColor = Color.blue; //星期文字色
    private Color dateFontColor = Color.black; //日期文字色
    private Color colBackground = new java.awt.Color(200, 200, 200); //星期底色
    private Color sundayFontColor = Color.red; //周末文字色
    private Color saturdayFontColor = new java.awt.Color(0, 155, 0); //周末文字色
    private Color moveButtonColor = new java.awt.Color(255, 255, 100); //滑鼠移到的日期底色
    private Color todayBtnColor = new java.awt.Color(230, 230, 230);

     //今天的日期底色
    //控制項配色------------------//
    private Color controlLineColor = new java.awt.Color(0, 155, 0); //控制項底色
    private Color controlTextColor = Color.black; //控制項文字色

    public DateChooser() {
        this.parten = "dd-MM-yyyy";
        initComponents();
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(parten);
        this.setValue(simpleDateFormat.format(date));
    }

    public String getValue() {
        return txt_Date.getText().substring(6,10)
                +txt_Date.getText().substring(2,6)
                +txt_Date.getText().substring(0,2);
    }

    public void setValue(String date) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            formatter.parse(date);
            txt_Date.setText(date);
        } catch (ParseException ex) {
            System.out.println("DateChooser:" + ex);
        }
    }

    public void setParentFrame(DateInterface frame) {
        this.owner = (Container) frame;
        this.m_Frame = frame;
    }

    private void onValueChanged() {
        if (m_Frame != null) {
            m_Frame.onDateChanged();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        txt_Date = new javax.swing.JTextField();
        btn_Choose = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        txt_Date.setEditable(false);
        txt_Date.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_Date.setText("YYYY-MM-DD");
        txt_Date.setMinimumSize(new java.awt.Dimension(30, 29));
        txt_Date.setPreferredSize(new java.awt.Dimension(30, 29));
        txt_Date.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_DateActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 67;
        add(txt_Date, gridBagConstraints);

        btn_Choose.setText("▼");
        btn_Choose.setMaximumSize(new java.awt.Dimension(27, 29));
        btn_Choose.setMinimumSize(new java.awt.Dimension(27, 29));
        btn_Choose.setPreferredSize(new java.awt.Dimension(27, 29));
        btn_Choose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ChooseActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        add(btn_Choose, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void btn_ChooseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ChooseActionPerformed
        java.awt.Rectangle r = txt_Date.getBounds();
        Point pOnScreen = txt_Date.getLocationOnScreen();

        Point result = new Point(pOnScreen.x, pOnScreen.y + r.height);
        Point powner = owner.getLocation();
        int offsetX = (pOnScreen.x + width) - (powner.x + owner.getWidth());
        int offsetY = (pOnScreen.y + r.height + height) -
                (powner.y + owner.getHeight());

        if (offsetX > 0) {
            result.x -= offsetX;
        }

        if (offsetY > 0) {
            result.y -= height + r.height;
        }

        dateFrame = new javax.swing.JDialog();
        dateFrame.setModal(false);
        dateFrame.setUndecorated(true);
        dateFrame.setLocation(result);
        dateFrame.setSize(width, height);
        dateFrame.setTitle("Date Chooser");
        dateFrame.addWindowListener(new WindowAdapter() {

            @Override
            public void windowDeactivated(WindowEvent e) {
                javax.swing.JDialog f = (javax.swing.JDialog) e.getSource();
                onValueChanged();
                f.dispose();
            }
        });
        DatePanel datePanel = new DatePanel(dateFrame, parten);
        dateFrame.getContentPane().setLayout(new BorderLayout());
        dateFrame.getContentPane().add(datePanel);
        dateFrame.setVisible(true);
    }//GEN-LAST:event_btn_ChooseActionPerformed

    private void txt_DateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_DateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_DateActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Choose;
    private javax.swing.JTextField txt_Date;
    // End of variables declaration//GEN-END:variables

    class DatePanel extends JPanel implements MouseListener, ChangeListener {

        JSpinner yearSpin;
        JSpinner monthSpin;
        int today;
        JButton[][] daysButton = new JButton[6][7];
        javax.swing.JDialog f;
        JPanel dayPanel = new JPanel();
        JPanel yearPanel = new JPanel();
        Calendar calendar = Calendar.getInstance();
        String pattern;

        public DatePanel(javax.swing.JDialog target, String pattern) {
            super();

            this.f = target;
            this.pattern = pattern;

            setLayout(new BorderLayout());
            setBorder(new LineBorder(backGroundColor, 2));
            setBackground(backGroundColor);
            initButton();
            createYearAndMonthPanal();

            String[] date = txt_Date.getText().split("-");
            yearSpin.setValue(Integer.parseInt(date[2]));
            monthSpin.setValue(Integer.parseInt(date[1]));
            today = Integer.parseInt(date[0]);
            this.flushWeekAndDayPanal(calendar);
            this.setLayout(new BorderLayout());
            this.add(yearPanel, BorderLayout.NORTH);
            this.add(dayPanel, BorderLayout.CENTER);
        }

        private void initButton() {
            int actionCommandId = 1;
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 7; j++) {
                    JButton numberButton = new JButton();
                    numberButton.setBorder(BorderFactory.createEmptyBorder());
                    numberButton.setHorizontalAlignment(SwingConstants.CENTER);
                    numberButton.setActionCommand(String.valueOf(actionCommandId));

                    numberButton.addMouseListener(this);

                    numberButton.setBackground(palletTableColor);
                    numberButton.setForeground(dateFontColor);
                    numberButton.setText(String.valueOf(actionCommandId));
                    numberButton.setPreferredSize(new Dimension((width - 30) / 7, (height - 30) / 7));
                    daysButton[i][j] = numberButton;
                    actionCommandId++;
                }
            }

        }

        private Calendar getNowCalendar() {
            Calendar result = Calendar.getInstance();
            return result;
        }

        private Date getSelectDate() {
            return calendar.getTime();
        }

        private void createYearAndMonthPanal() {
            Calendar c = getNowCalendar();
            int currentYear = c.get(Calendar.YEAR);
            int currentMonth = c.get(Calendar.MONTH) + 1;
            yearSpin = new JSpinner(new javax.swing.SpinnerNumberModel(currentYear, startYear, lastYear, 1));
            monthSpin = new JSpinner(new javax.swing.SpinnerNumberModel(currentMonth, 1, 12, 1));


            yearPanel.setLayout(new java.awt.FlowLayout());
            yearPanel.setBackground(controlLineColor);

            yearSpin.setPreferredSize(new Dimension(60, 30));
            yearSpin.setName("Year");
            yearSpin.setEditor(new JSpinner.NumberEditor(yearSpin, "####"));
            yearSpin.addChangeListener(this);
            yearPanel.add(yearSpin);

            JLabel yearLabel = new JLabel("Year");
            yearLabel.setForeground(controlTextColor);
            yearPanel.add(yearLabel);

            monthSpin.setPreferredSize(new Dimension(40, 30));
            monthSpin.setName("Month");
            monthSpin.addChangeListener(this);
            yearPanel.add(monthSpin);

            JLabel monthLabel = new JLabel("Month");
            monthLabel.setForeground(controlTextColor);
            yearPanel.add(monthLabel);
        }

        private void flushWeekAndDayPanal(Calendar c) {
            c.set(Calendar.DAY_OF_MONTH, 1);
            c.setFirstDayOfWeek(0);
            int firstdayofWeek = c.get(Calendar.DAY_OF_WEEK);
            int lastdayofWeek = c.getActualMaximum(Calendar.DAY_OF_MONTH);

            dayPanel.setFont(m_font);
            dayPanel.setLayout(new GridBagLayout());
            dayPanel.setBackground(Color.white);

            JLabel cell;

            for (int i = 0; i < 7; i++) {
                cell = new JLabel(colname[i]);
                cell.setBackground(colBackground);
                cell.setHorizontalAlignment(JLabel.CENTER);
                cell.setPreferredSize(new Dimension((width - 30) / 7, (height - 30) / 7));
                cell.setOpaque(true);
                if (i == 0) {
                    cell.setForeground(sundayFontColor);
                } else if (i == 6) {
                    cell.setForeground(saturdayFontColor);
                } else {
                    cell.setForeground(weekFontColor);
                }
                dayPanel.add(cell, new GridBagConstraints(i, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
                        new Insets(0, 0, 0, 0), 0, 0));
            }

            int actionCommandId = 1;
            int rowCount = 0;
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 7; j++) {
                    JButton numberButton = daysButton[i][j];
                    actionCommandId = Integer.parseInt(numberButton.getActionCommand());
                    if (actionCommandId == today) {
                        numberButton.setBackground(todayBtnColor);
                    }
                    if ((actionCommandId + firstdayofWeek - 2) % 7 == 0) {
                        numberButton.setForeground(sundayFontColor);
                    } else if ((actionCommandId + firstdayofWeek - 2) % 7 == 6) {
                        numberButton.setForeground(saturdayFontColor);
                    } else {
                        numberButton.setForeground(dateFontColor);
                    }

                    if (actionCommandId <= lastdayofWeek) {
                        int y = 0;
                        if ((firstdayofWeek - 1) <=
                                (j + firstdayofWeek - 1) % 7) {
                            y = i + 1;
                        } else {
                            y = i + 2;
                            if (y == 2) {
                                rowCount++;
                            }
                        }
                        dayPanel.add(numberButton,
                                new GridBagConstraints((j + firstdayofWeek - 1) %
                                7, y, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                                GridBagConstraints.NONE,
                                new Insets(0, 0, 0, 0), 0, 0));
                    }
                }
            }

            if (rowCount >= 5) {
                dateFrame.setSize(width, height + (height - 30) / 7);
            } else if (lastdayofWeek % 7 == 0 && rowCount == 0) {
                dateFrame.setSize(width, height - (height - 30) / 7);
            } else {
                dateFrame.setSize(width, height);
            }
        }

        private int getSelectedYear() {
            return ((Integer) yearSpin.getValue()).intValue();
        }

        private int getSelectedMonth() {
            return ((Integer) monthSpin.getValue()).intValue();
        }

        public void stateChanged(ChangeEvent e) {
            JSpinner source = (JSpinner) e.getSource();
            if (source.getName().equals("Year")) {

                calendar.set(Calendar.YEAR, getSelectedYear());
                dayPanel.removeAll();
                this.flushWeekAndDayPanal(calendar);
                dayPanel.revalidate();
                dayPanel.updateUI();
                return;
            }
            if (source.getName().equals("Month")) {
                calendar.set(Calendar.MONTH, getSelectedMonth() - 1);

                dayPanel.removeAll();
                this.flushWeekAndDayPanal(calendar);
                dayPanel.revalidate();
                dayPanel.updateUI();
                return;
            }
        }

        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 1) {
                JButton source = (JButton) e.getSource();

                String value = source.getText();
                int day = Integer.parseInt(value);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                Date selectDate = this.getSelectDate();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                        pattern);
                DateChooser.this.setValue(simpleDateFormat.format(selectDate));
                f.dispose();
            }
        }

        public void mousePressed(MouseEvent e) {
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
            JButton jbutton = (JButton) e.getSource();
            jbutton.setBackground(moveButtonColor);

        }

        public void mouseExited(MouseEvent e) {
            JButton jbutton = (JButton) e.getSource();
            int comm = Integer.parseInt(jbutton.getActionCommand());
            if (comm == today) {
                jbutton.setBackground(todayBtnColor);
            } else {
                jbutton.setBackground(palletTableColor);
            }
        }
    }

    class NewJPanelButton
            extends JButton {

        public NewJPanelButton(String text) {
            super(text);
        }

        @Override
        public Insets getInsets() {
            return new Insets(4, 2, 0, 2);
        }
    }
}
