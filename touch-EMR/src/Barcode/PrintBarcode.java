package Barcode;

import cc.johnwu.sql.DBC;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PrintBarcode {

    private static String m_Barcode;
    private static int m_Status;
    Paper paper;

    public static void PrintBarcode(String barcode) {
        m_Barcode = barcode;
        PrinterJob pj = PrinterJob.getPrinterJob();
        PageFormat pf = pj.defaultPage();
        Paper paper = new Paper();
        pf.setPaper(paper);

        //if (pj.printDialog())
        try {
            m_Status = 0;
            pj.setPrintable(new MyPrintable(), pf);
            pj.print();

            m_Status = 1;
            pj.setPrintable(new MyPrintable(), pf);
            pj.print();
        } catch (PrinterException e) {
        }
    }

    private static class MyPrintable implements Printable {
      
        public int print(Graphics g, PageFormat pf, int pageIndex) {
            try {
                System.out.println(pf.getPaper().getHeight());
                if (pageIndex != 0) {
                    return NO_SUCH_PAGE;
                }
                ResultSet rs = null;
                Graphics2D g2 = (Graphics2D) g;
                BufferedImage image = new BufferedImage(500, 500, 1);
                Barcode barcode = null;
                g = image.getGraphics();
                g.setColor(Color.white);
                g.fillRect(0, 0, 500, 500);
                /**設定barcode相關資訊*/
                barcode = Barcode.getInstance();
                barcode.setData(m_Barcode);
                barcode.setWidth(1);
                barcode.setInitx(50);
                barcode.setInity(50);
                barcode.setShowDesc(true);
                barcode.setCharLeft(20);
                barcode.setCharTop(5);
                barcode.setShowStartAndEnd(true);
                try {
                    /**繪製條碼*/
                    barcode.printBarcode(g);
                } catch (Exception ex) {
                    Logger.getLogger(PrintBarcode.class.getName()).log(Level.SEVERE, null, ex);
                }
                rs = DBC.executeQuery("SELECT * FROM patients_info WHERE p_no = '"+m_Barcode+"'");
                    //rs = DBC.executeQuery("SELECT * FROM patients_info WHERE p_no = '0'");
                    rs.next();
               

                switch (m_Status) {
                    case 0:
                        //225,95
                        g2.drawImage(image, null, 225, 120);
                        g2.setPaint(Color.black);
                        g2.setFont(new Font("Arial", Font.PLAIN, 23));
                        g2.drawString("Royal Victoria Teaching Hospital", 120, 105);
                        g2.drawString("Medical records", 195, 140);
                        g2.setFont(new Font("Arial", Font.PLAIN, 13));
                        g2.drawString("Patient No.:"+rs.getString("p_no"), 80, 260);
                        g2.drawString("Birthday:"+rs.getString("birth"), 225, 290);
                        g2.drawString("Gender:"+rs.getString("gender"), 375, 290);
                        g2.drawString("Name:"+rs.getString("firstname")+" "+rs.getString("lastname"), 80, 290);
                        g2.drawString("Address:" + rs.getString("address"), 80, 350);
                        g2.drawString("Cellphone:"+rs.getString("cell_phone"), 80, 320);
                        g2.drawRect(75, 370, 455, 66);
                        g2.drawString("Allergic reactions:", 80, 390);
                        g2.drawRect(75, 436, 455, 66);
                        g2.drawString("Notes:", 80, 456);
                        break;
                    case 1:
                        
                        g2.drawImage(image, null, 150, 40);
                        g2.setPaint(Color.black);
                        g2.setFont(new Font("Arial", Font.PLAIN, 16));
                        g2.drawString("Register card", 80, 90);
                        g2.setFont(new Font("Arial", Font.PLAIN, 10));
                        g2.drawString("Pation No.:"+rs.getString("p_no"), 80, 120);
                        g2.drawString("Name:" +rs.getString("firstname")+ " " +rs.getString("lastname"), 80, 145);
                        g2.drawString("Gender:"+rs.getString("gender"), 80, 170);
                        g2.drawString("Birthday:"+rs.getString("birth"), 160, 170);
                }
                /**drawImagee後面的兩個數字控制列印的x，y軸*/
                return PAGE_EXISTS;
            } catch (SQLException ex) {
                Logger.getLogger(PrintBarcode.class.getName()).log(Level.SEVERE, null, ex);
            }
            return PAGE_EXISTS;
        }
    }
}

/**---------------------------------------------------------------------------------------*/
abstract class Barcode {

    protected int initx;
    protected int inity;
    protected int width;
    protected int height;
    protected String data;
    protected boolean isShowDesc;
    protected boolean isShowStartAndEnd;
    protected int charLeft;
    protected int charTop;

    public Barcode() {
        initx = 10;
        inity = 10;
        width = 2;
        height = 50;
        data = null;
        isShowDesc = false;
        isShowStartAndEnd = false;
        charLeft = 0;
        charTop = 0;
    }

    public static Barcode getInstance() {
        return new Barcode128();
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getInitx() {
        return initx;
    }

    public void setInitx(int initx) {
        this.initx = initx;
    }

    public int getInity() {
        return inity;
    }

    public void setInity(int inity) {
        this.inity = inity;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public boolean isShowDesc() {
        return isShowDesc;
    }

    public void setShowDesc(boolean isShowDesc) {
        this.isShowDesc = isShowDesc;
    }

    public abstract void printBarcode(Graphics g) throws Exception;

    public boolean isShowStartAndEnd() {
        return isShowStartAndEnd;
    }

    public void setShowStartAndEnd(boolean isShowStartAndEnd) {
        this.isShowStartAndEnd = isShowStartAndEnd;
    }

    public int getCharLeft() {
        return charLeft;
    }

    public void setCharLeft(int charLeft) {
        this.charLeft = charLeft;
    }

    public int getCharTop() {
        return charTop;
    }

    public void setCharTop(int charTop) {
        this.charTop = charTop;
    }
}

/**---------------------------------------------------------------------------------------*/
class Barcode128Constant {

    public static final String BARCODE[] = {
        "11011001100", "11001101100", "11001100110", "10010011000", "10010001100", "10001001100", "10011001000", "10011000100", "10001100100", "11001001000",
        "11001000100", "11000100100", "10110011100", "10011011100", "10011001110", "10111001100", "10011101100", "10011100110", "11001110010", "11001011100",
        "11001001110", "11011100100", "11001110100", "11101101110", "11101001100", "11100101100", "11100100110", "11101100100", "11100110100", "11100110010",
        "11011011000", "11011000110", "11000110110", "10100011000", "10001011000", "10001000110", "10110001000", "10001101000", "10001100010", "11010001000",
        "11000101000", "11000100010", "10110111000", "10110001110", "10001101110", "10111011000", "10111000110", "10001110110", "11101110110", "11010001110",
        "11000101110", "11011101000", "11011100010", "11011101110", "11101011000", "11101000110", "11100010110", "11101101000", "11101100010", "11100011010",
        "11101111010", "11001000010", "11110001010", "10100110000", "10100001100", "10010110000", "10010000110", "10000101100", "10000100110", "10110010000",
        "10110000100", "10011010000", "10011000010", "10000110100", "10000110010", "11000010010", "11001010000", "11110111010", "11000010100", "10001111010",
        "10100111100", "10010111100", "10010011110", "10111100100", "10011110100", "10011110010", "11110100100", "11110010100", "11110010010", "11011011110",
        "11011110110", "11110110110", "10101111000", "10100011110", "10001011110", "10111101000", "10111100010", "11110101000", "11110100010", "10111011110",
        "10111101110", "11101011110", "11110101110", "11010000100", "11010010000", "11010011100", "11000111010"
    };
    public static final String CHAR_STAR_A = "11010000100";
    public static final String CHAR_STAR_B = "11010010000";
    public static final String CHAR_STAR_C = "11010011100";
    public static final String CHAR_END = "11000111010";

    public Barcode128Constant() {
    }
}

/**---------------------------------------------------------------------------------------*/
class Barcode128 extends Barcode {

    public Barcode128() {
    }

    public void printBarcode(Graphics g) throws Exception {
        int x = initx;
        x = exe("11010010000", x, g, true);
        for (int i = 0; i < data.length(); i++) {
            char c = data.charAt(i);
            x = exe(Barcode128Constant.BARCODE[c - 32], x, g, false);
        }

        x = exe(getEndChars(data), x, g, false);
        x = exe("11000111010", x, g, false);
        x = exe("11", x, g, true);
        if (isShowDesc) {
            printDesc(g);
        }
    }

    private int exe(String str, int x, Graphics g, boolean isStatrOrEnd) {
        for (int i = 0; i < str.length(); i++) {
            char cc = str.charAt(i);
            if (cc == '1') {
                paint(g, Color.black, x, isStatrOrEnd);
            } else {
                paint(g, Color.white, x, isStatrOrEnd);
            }
            x += width;
        }

        return x;
    }

    private void paint(Graphics g, Color color, int x, boolean isStartOrEnd) {
        g.setColor(color);
        if (isStartOrEnd && isShowStartAndEnd) {
            g.fillRect(x, inity, width, height + 10);
        } else {
            g.fillRect(x, inity, width, height);
        }
    }

    private String getEndChars(String str) {
        int num = 104;
        for (int i = 1; i <= str.length(); i++) {
            num += (str.charAt(i - 1) - 32) * i;
        }

        num = num % 103 + 32;
        if (num >= 32) {
            num -= 32;
        } else {
            num += 64;
        }
        return Barcode128Constant.BARCODE[num];
    }

    private void printDesc(Graphics g) {
        g.setColor(Color.black);
        g.drawString(data, initx + charLeft, inity + height + 10 + charTop);
    }
}
