
package Common;

import cc.johnwu.date.DateMethod;
import cc.johnwu.sql.DBC;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.geom.AffineTransform;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Steven
 */
public class PrintTools {
     private int m_Type;
     private ResultSet m_Rs;      // 基本資料
     private ResultSet m_RsData;  // 迴圈列印資料
     private String m_RegGuid;    // 迴圈列印資料
     private String m_CashierType;    // 收據類別
     private final String X_RAY_CODE = Constant.X_RAY_CODE;      // 處置X光代碼

     // 收據
     public void DoPrint(int i, String regGuid, String cashierType ) {
        PrinterJob pj = PrinterJob.getPrinterJob();
        PageFormat pf = pj.defaultPage();
        Paper paper = new Paper();
        pf.setPaper(paper);
        pj.setPrintable(new MyPrintable(), pf);
        m_RegGuid = regGuid;
        m_CashierType = cashierType;
        m_Type = i ;
        //if (pj.printDialog()) {
        try {
            pj.print();
        } catch (PrinterException e) {

        }
    }

     /**
     * 列印斷行使用
     * str:要進行斷行的字串 num:每行字串長度 x:目前所在x值 y:目前所在y值 space:換行間距
     * return y值
     */
    public int getLineBreaksWord(Graphics2D g2, String str, int num, int x, int y ,int space) {

        String newStr = "";
        String[] sa = str.split(" ");
        boolean state = false;
        for (int i = 0; i < sa.length; i++) {
            if (sa.length > i + 1 && (newStr.length() + sa[i+1].length()) >=  num) {

                g2.drawString(newStr , x , y += space);
                newStr = "";
                state = true;
            } else {
                if (state) {
                    i--;
                    state = false;
                }

                newStr += (sa[i] + " ");
            }
        }

        g2.drawString(newStr , x , y += space);
        return y;
    }

    // 病歷室
    public void DoPrint(int i,String guid , boolean t) {
        m_RegGuid = guid;
         m_Type = i ;
        PrinterJob pj = PrinterJob.getPrinterJob();

        PageFormat pf = pj.defaultPage();
        Paper paper = new Paper();
        pf.setPaper(paper);
        pj.setPrintable(new MyPrintable(), pf);

        //if (pj.printDialog()) {
        try {
            pj.print();
        } catch (PrinterException e) {

        }
    }

      // 藥袋
     public void DoPrint(int i,String guid) {
         m_RegGuid = guid;
         m_Type = i ;
         // 病患資料
         String sqlPatient = "SELECT  patients_info.p_no, "+
                            "registration_info.pharmacy_no, registration_info.modify_count,concat(patients_info.firstname,'  ',patients_info.lastname) AS name, "+
                            "patients_info.gender, patients_info.birth , shift_table.shift_date AS date, policlinic.name AS dept, poli_room.name AS clinic, " +
                            "concat(staff_info.firstname,'  ',staff_info.lastname) AS doctor, " +
                            "(YEAR(CURDATE())-YEAR(patients_info.birth))-(RIGHT(CURDATE(),5)< RIGHT(patients_info.birth,5)) AS age, "+
                            "CASE shift_table.shift  "+
                                "WHEN '1' THEN 'Morning'  "+
                                "WHEN '2' THEN 'Afternoon'  "+
                                "WHEN '3' THEN 'Night'  "+
                                "ELSE 'All Night' "+
                                "END shift "+
                            "FROM registration_info, patients_info, shift_table,poli_room,policlinic, staff_info WHERE registration_info.guid = '"+m_RegGuid+"'  " +
                            "AND registration_info.p_no = patients_info.p_no "+
                            "AND registration_info.shift_guid = shift_table.guid " +
                            "AND shift_table.room_guid = poli_room.guid " +
                            "AND poli_room.poli_guid = policlinic.guid " +
                            "AND shift_table.s_id = staff_info.s_id";
        try {
            m_Rs = DBC.executeQuery(sqlPatient);
            m_Rs.next();
        } catch (SQLException ex) {
            Logger.getLogger(PrintTools.class.getName()).log(Level.SEVERE, null, ex);
        }


        PrinterJob pj = PrinterJob.getPrinterJob();

        PageFormat pf = pj.defaultPage();
        Paper paper = new Paper();
        pf.setPaper(paper);
        pj.setPrintable(new MyPrintable(), pf);

        //if (pj.printDialog()) {
        try {
            pj.print();
        } catch (PrinterException e) {

        }
    }

     // 列印1 列印2
     public void DoPrint(int i, ResultSet rs) {
        PrinterJob pj = PrinterJob.getPrinterJob();
        PageFormat pf = pj.defaultPage();
        Paper paper = new Paper();
        pf.setPaper(paper);
        pj.setPrintable(new MyPrintable(), pf);
        m_Rs = rs;
        m_Type = i ;
        //if (pj.printDialog()) {
        try {
            pj.print();
        } catch (PrinterException e) {
            System.out.println(e);
        }
    }

     class MyPrintable implements Printable {

        public int print(Graphics g, PageFormat pf, int pageIndex) {
            try {
                String sqlData = "";
                int x = 0;
                int y = 0; // 起始值
                int index = 0;
                int space = 0; //間距
                int rowNo = 0;
                if (pageIndex != 0) {
                    return NO_SUCH_PAGE;
                }
                Graphics2D g2 = (Graphics2D) g;
                g2.setFont(new Font("Serif", Font.PLAIN, 10));
                g2.setPaint(Color.black);
                int i = 100;
                switch (m_Type) {
                    case 1:
                        // 1.待診單(給病患)
                        setLogoTitle(g2, "Single Registration <"+m_Rs.getString("type")+">   (For Patient)");
                        x = 150;
                        y = 210; // 起始值
                        space = 50; // 間距
                        g2.setFont(new Font("Serif", Font.PLAIN, 30));
                        g2.drawString("Patient No.:", x, y += space);
                        g2.drawString("Name:", x, y += space);
                        g2.drawString("Gender:", x, y += space);
                        g2.drawString("Birth:", x, y += space);
                        g2.drawString("Age.:", x, y += space);
                        x = 750;
                        y = 190;
                        space = 70; // 間距
                        g2.setFont(new Font("Serif", Font.PLAIN, 48));
                        g2.drawString("Date:", x, y += space);
                        g2.drawString("Shift:", x, y += space);
                        g2.drawString("Dept.:", x, y += space);
                        g2.drawString("Clinic:", x, y += space);
                        g2.drawString("Doctor:", x, y += space);
                        g2.drawString("Waiting No.:", x, y += space);
                        x = 250;
                        y = 210; // 起始值
                        space = 50; // 間距
                        g2.setFont(new Font("Serif", Font.BOLD, 30));
                        g2.drawString(m_Rs.getString("p_no"), x + 45, y += space);
                        g2.drawString(m_Rs.getString("firstname") +" "+m_Rs.getString("lastname"), x - 15, y += space);
                        g2.drawString(m_Rs.getString("gender"), x + 5, y += space);
                        g2.drawString(m_Rs.getString("birth"), x - 30, y += space);
                        g2.drawString(m_Rs.getString("age"), x - 30, y += space);
                        x = 950;
                        y = 190;
                        space = 70; // 間距
                        g2.setFont(new Font("Serif", Font.BOLD, 48));
                        g2.drawString(m_Rs.getString("date"), x - 70, y += space);
                        g2.drawString(m_Rs.getString("shift"), x - 65, y += space);
                        g2.drawString(m_Rs.getString("dept"), x - 65, y += space);
                        g2.drawString(m_Rs.getString("clinic"), x - 35, y += space);
                        g2.drawString(m_Rs.getString("doctor"), x - 35, y += space);
                        g2.drawString(m_Rs.getString("waitno"), x + 70, y += space);
                        break;
                    case 2:
                        // 2.流程單(給病患)
                        // -------------　列印表單名稱 --------------
                        setLogoTitle(g2, "Single Process");
                        x = 50;
                        y = 190; // 起始值
                        space = 50; //間距
                        g2.setFont(new Font("Serif", Font.PLAIN, 28));
                        g2.drawString("Patient No.:", x, y += space);
                        g2.drawString("Name:", x, y += space);
                        g2.drawString("Date:", x, y += space);
                        g2.drawString("Shift:", x, y += space);
                        g2.drawString("Dept.:", x, y += space);
                        g2.drawString("Clinic:", x, y += space);
                        g2.drawString("Doctor:", x, y += space);
                        x = 150;
                        y = 190; // 起始值
                        space = 50; //間距
                        g2.setFont(new Font("Serif", Font.BOLD, 32));
                        g2.drawString(m_Rs.getString("p_no"), x + 45, y += space);
                        g2.drawString(m_Rs.getString("firstname") +" "+m_Rs.getString("lastname"), x - 20, y += space);
                        g2.drawString(m_Rs.getString("date"), x - 20, y += space);
                        g2.drawString(m_Rs.getString("shift"), x - 20, y += space);
                        g2.drawString(m_Rs.getString("dept"), x - 20, y += space);
                        g2.drawString(m_Rs.getString("clinic"), x, y += space);
                        g2.drawString(m_Rs.getString("doctor"), x, y += space);
                        x = 700;
                        y = 175; // 起始值
                        space = 70; //間距
                        g2.setFont(new Font("Serif", Font.BOLD, 48));
                        g2.drawString("Case Management", x, y += space);
                        g2.drawString("Clinic", x, y += space);
                        g2.drawString("Laboratory", x, y += space);
                        g2.drawString("Pharmacy", x, y += space);
                        x = 1100;
                        y = 175; // 起始值
                        g2.drawString("-----------□", x, y += space);
                        g2.drawString("-----------□", x, y += space);
                        g2.drawString("-----------□", x, y += space);
                        g2.drawString("-----------□", x, y += space);
                        break;
                    case 3:
                            sqlData =
                                "SELECT registration_info.pharmacy_no , " +
                                     "registration_info.p_no AS 'p_no', " +
                                     "concat(patients_info.firstname,'  ',patients_info.lastname) AS 'name', "+
                                     "patients_info.birth AS 'birth', " +
                                     "patients_info.gender AS 'gender', "+
                                     "registration_info.reg_time AS 'reg_time', "+
                                    "poli_room.name AS 'poliname', "+
                                    "shift_table.shift_date AS 'shift_date', "+
                                     "CASE shift_table.shift " +
                                    "WHEN '1' THEN 'Morning' " +
                                    "WHEN '2' THEN 'Afternoon' " +
                                    "WHEN '3' THEN 'Night' " +
                                    "ELSE 'All Night'" +
                                    "END 'shift' , " +
                                     "policlinic.name AS dept, CASE registration_info.register " +
                                   "WHEN 'R' THEN 'Reservation' " +
                                   "ELSE 'Locality' " +
                                   "END 'Register', " +
                                   "concat(staff_info.firstname,'  ',staff_info.lastname) AS 'doctor' "+
                                 "FROM registration_info, patients_info, shift_table,staff_info ,poli_room, policlinic, outpatient_services ,medicine_stock "+
                                 "WHERE registration_info.shift_guid = shift_table.guid "+
                                    "AND shift_table.room_guid = poli_room.guid "+
                                    "AND poli_room.poli_guid = policlinic.guid "+
                                    "AND shift_table.s_id = staff_info.s_id "+
                                    "AND registration_info.p_no = patients_info.p_no "+
                                    "AND registration_info.guid = '"+m_RegGuid+"' "+
                                  
                                    "AND medicine_stock.os_guid = outpatient_services.guid "+
                                    "GROUP BY registration_info.guid "+
                                    "ORDER BY 'No.' ";
                            System.out.println(sqlData);
                        m_Rs = DBC.executeQuery(sqlData);
                        m_Rs.next();

                        // 3.調閱單(病歷室)
                        // -------------　列印表單名稱 --------------
                        g2.setFont(new Font("Serif", Font.PLAIN, 12));
                        i += 15;
                        g2.drawString("Patient No.："+m_Rs.getString("p_no")+"", 80, i);
                        i += 15;
                        g2.drawString("Name："+m_Rs.getString("name")+" ("+m_Rs.getString("gender")+")", 80, i);
                        i += 15;
                        g2.drawString("Birthday："+m_Rs.getString("birth")+"", 80, i);
                        i += 15;
                        g2.drawString("Register Time："+m_Rs.getString("reg_time")+"", 80, i);
                        i += 15;
                        g2.drawString("==========================================================================================" + "==========================================================================================", 0, i);
                        i += 15;
                        g2.drawString("Date："+m_Rs.getString("shift_date")+"", 80, i);
                        i += 15;
                        g2.drawString("Shift："+m_Rs.getString("shift")+"", 80, i);
                        i += 15;
                        g2.drawString("Depart/Clinic："+m_Rs.getString("dept")+"", 80, i);
                        i += 15;
                        g2.drawString("Clinic："+m_Rs.getString("poliname")+"", 80, i);
                        i += 95;
                        g2.drawString("Patient No.："+m_Rs.getString("p_no")+"", 80, i);
                        i += 15;
                        g2.drawString("Name："+m_Rs.getString("name")+" ("+m_Rs.getString("gender")+")", 80, i);
                        i += 15;
                        g2.drawString("Birthday："+m_Rs.getString("birth")+"", 80, i);
                        i += 15;
                        g2.drawString("Register Time："+m_Rs.getString("reg_time")+"", 80, i);
                        i += 15;
                        g2.drawString("==========================================================================================" + "==========================================================================================", 0, i);
                        i += 15;
                        g2.drawString("Date："+m_Rs.getString("shift_date")+"", 80, i);
                        i += 15;
                        g2.drawString("Shift："+m_Rs.getString("shift")+"", 80, i);
                        i += 15;
                        g2.drawString("Depart/Clinic："+m_Rs.getString("dept")+"", 80, i);
                        i += 15;
                        g2.drawString("Clinic："+m_Rs.getString("poliname")+"", 80, i);
                        g2.setFont(new Font("Serif", Font.BOLD, 16));
                        g2.drawString("Access History <"+m_Rs.getString("Register")+">    (For Medical Record)", 80, 100);
                        g2.setFont(new Font("Serif", Font.BOLD, 16));
                        g2.drawString("Access History <"+m_Rs.getString("Register")+">    (With Folder)", 80, 310);
                        BufferedImage bSrc;
                        try {
                            g2.drawString("---------------------------------------------------------------------------------------------" + "---------------------------------------------------------------------------------------------", 80, 260);
                            bSrc = ImageIO.read(new File("./img/cut.png"));
                            AffineTransform at = new AffineTransform();
                            g2.translate(80, 250);
                            g2.scale(0.2, 0.2);
                            g2.drawRenderedImage(bSrc, at);
                        } catch (IOException ex) {
                            Logger.getLogger(PrintTools.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                    case 4:
                        // 4.X-Ray(給病患)
                        sqlData = "SELECT prescription_code.code AS code, prescription_code.name AS name, prescription.place, prescription_code.type " +
                            "FROM prescription, prescription_code, outpatient_services, registration_info " +
                            "WHERE registration_info.guid = '"+m_RegGuid+"' " +
                              "AND prescription.os_guid = outpatient_services.guid " +
                              "AND prescription_code.code = prescription.code " +
                              "AND outpatient_services.reg_guid = registration_info.guid "+
                              "AND prescription_code.type = '"+Constant.X_RAY_CODE+"' ";
       
                        m_RsData = DBC.executeQuery(sqlData);


                        setLogoTitle(g2, "Radiology (To The Radiology Department)    (For Patient)");
                        x = 60;
                        y = setPatientInfoTitle(g2);
                        space = 50; //間距
                        g2.setFont(new Font("Serif", Font.PLAIN, 32)); // 表頭
                        g2.drawString("Urgent", x - 45, y);
                        g2.drawString("Item", x + 70, y);
                        g2.setFont(new Font("Serif", Font.BOLD, 32)); // 資料
                        y += 40;
                        rowNo = 0;

                         while (m_RsData.next()) {
                            g2.drawString(++rowNo + ". " + m_RsData.getString("code") +" "+ m_RsData.getString("name"), x + 75, y);
                            y += space;
                         }
                           
                        break;
                    case 5:
                      sqlData =
                            "SELECT prescription_code.code AS code, prescription_code.name AS name, prescription.place, prescription_code.type, prescription.cost AS cost, prescription.result AS result, " +
                               "CASE prescription.isnormal WHEN 1 THEN 'Y' WHEN 0 THEN 'N' ELSE null  END 'Normal' " +
                            "FROM prescription, prescription_code, outpatient_services, registration_info " +
                            "WHERE registration_info.guid = '"+m_RegGuid+"' " +
                              "AND prescription.os_guid = outpatient_services.guid " +
                              "AND prescription_code.code = prescription.code " +
                              "AND outpatient_services.reg_guid = registration_info.guid " +
                              "AND prescription_code.type = '"+Constant.X_RAY_CODE+"'";
                         m_RsData = DBC.executeQuery(sqlData);
                        // 5.X-Ray結果單(給病歷室)
                        setLogoTitle(g2, "Radiology Data(To The Medical Records)");
                        x = 60;
                        y = setPatientInfoTitle(g2);
                        space = 100; //間距
                        g2.setFont(new Font("Serif", Font.PLAIN, 32)); // 表頭
                        g2.drawString("Urgent", x - 45, y);
                        g2.drawString("Item", x + 70, y);
                        g2.drawString("Normal", x + 1300, y);
                        g2.setFont(new Font("Serif", Font.BOLD, 32)); // 資料
                        y += 40;
                        rowNo = 0;
                        while (m_RsData.next()) {
                            g2.drawString(++rowNo + ". " + m_RsData.getString("code") +" "+ m_RsData.getString("name"), x + 75, y);
                            g2.setFont(new Font("Serif", Font.PLAIN, 32)); // 資料
                            g2.drawString("Result:", x + 120, y + 50);
                            g2.setFont(new Font("Serif", Font.BOLD, 32)); // 資料
                            g2.drawString(m_RsData.getString("Normal"), x + 1320, y);
                            y = getLineBreaksWord(g2,m_RsData.getString("result"), 80 , x + 220, y , 50);
                            y += space;
                        }
                            
                            
                        
                        break;
                    case 6:
                        // 6.檢驗單(給病患)
                        String sql = "SELECT prescription_code.code AS code, prescription_code.name AS name, prescription.place, prescription_code.type " +
                                "FROM prescription, prescription_code, outpatient_services, registration_info " +
                                "WHERE registration_info.guid = '"+m_RegGuid+"' " +
                                  "AND prescription.os_guid = outpatient_services.guid " +
                                  "AND prescription_code.code = prescription.code " +
                                  "AND outpatient_services.reg_guid = registration_info.guid " +
                                  "AND prescription_code.type <> '"+Constant.X_RAY_CODE+"'";

                        m_RsData = DBC.executeQuery(sql);
                        setLogoTitle(g2, "Laboratory (To The Laboratory)    (For Patient)");
                        x = 60;
                        y = setPatientInfoTitle(g2);
                        space = 50; //間距
                        g2.setFont(new Font("Serif", Font.PLAIN, 32)); // 表頭
                        g2.drawString("Urgent", x - 45, y);
                        g2.drawString("Item", x + 70, y);
                        g2.drawString("Type", x + 1100, y);
                        g2.setFont(new Font("Serif", Font.BOLD, 32)); // 資料
                        y += 40;
                        while (m_RsData.next()) {
                            g2.drawString(++rowNo + ". " + m_RsData.getString("code") +" "+ m_RsData.getString("name"), x + 75, y);
                            g2.drawString(m_RsData.getString("type"), x + 1105, y);
                            y += space;
                        }
                        break;
                    case 7:
                        // 7.檢驗結果單(給病歷室)
                       sqlData =
                            "SELECT prescription_code.code AS code, prescription_code.name AS name, prescription.place, prescription_code.type, prescription.cost AS cost, prescription.result AS result, " +
                               "CASE prescription.isnormal WHEN 1 THEN 'Y' WHEN 0 THEN 'N' ELSE null  END 'Normal' " +
                            "FROM prescription, prescription_code, outpatient_services, registration_info " +
                            "WHERE registration_info.guid = '"+m_RegGuid+"' " +
                              "AND prescription.os_guid = outpatient_services.guid " +
                              "AND prescription_code.code = prescription.code " +
                              "AND outpatient_services.reg_guid = registration_info.guid " +
                              "AND prescription_code.type <> '"+Constant.X_RAY_CODE+"'";
                        m_RsData = DBC.executeQuery(sqlData);
                        setLogoTitle(g2, "Laboratory Data (To The Medical Records)");
                        x = 60;
                        y = setPatientInfoTitle(g2);
                        space = 100; //間距
                        g2.setFont(new Font("Serif", Font.PLAIN, 32)); // 表頭
                        g2.drawString("Urgent", x - 45, y);
                        g2.drawString("Item", x + 70, y);
                        g2.drawString("Type", x + 900, y);
                        g2.drawString("Normal", x + 1300, y);
                        g2.setFont(new Font("Serif", Font.BOLD, 32)); // 資料
                        y += 40;
                        rowNo = 0;
                        m_RsData.last();
                        index =  m_RsData.getRow();
                        m_RsData.beforeFirst();
                        for (int a = 0; a <  index; a++) {
                            m_RsData.next();
                            g2.drawString(++rowNo + ". " + m_RsData.getString("code") +" "+ m_RsData.getString("name"), x + 75, y);
                            g2.drawString(m_RsData.getString("type"), x + 905, y);
                            g2.setFont(new Font("Serif", Font.PLAIN, 32)); // 資料
                            g2.drawString("Result:", x + 120, y + 50);
                            g2.setFont(new Font("Serif", Font.BOLD, 32)); // 資料
                            g2.drawString(m_RsData.getString("result"), x + 240, y + 50);
                            g2.drawString(m_RsData.getString("Normal"), x + 1320, y);
                            y += space;
                        }
                        break;
                    case 8:
                        // 8.領藥單(給病患)
                         sqlData = "SELECT medicines.code, medicines.item, medicine_stock.dosage, medicines.unit, medicines.injection,medicine_stock.usage, " +
                                    "medicine_stock.way, medicine_stock.day, medicine_stock.quantity, medicine_stock.urgent, " +
                                   "medicine_stock.powder, medicine_stock.ps " +
                            "FROM medicines, medicine_stock, outpatient_services, registration_info " +
                            "WHERE registration_info.guid = '"+m_RegGuid+"' " +
                                "AND medicine_stock.os_guid = outpatient_services.guid " +
                                "AND outpatient_services.reg_guid = registration_info.guid " +
                                "AND medicines.code = medicine_stock.m_code";

                        m_RsData = DBC.executeQuery(sqlData);
                        setLogoTitle(g2, "Medicine (To The Pharmacy)    (For Patient)");
                        x = 10;
                        y = setPatientInfoTitle(g2);
                        space = 110; //間距
                        g2.setFont(new Font("Serif", Font.PLAIN, 28)); // 表頭
                        g2.drawString("Urgent", x - 10, y);
                        g2.drawString("Drug Name", x + 80, y);
                        g2.drawString("Dosage", x + 530, y);
                        g2.drawString("Frequency", x + 700, y);
                        g2.drawString("Usage", x + 1020, y);
                        g2.drawString("Duration", x + 1150, y);
                        g2.drawString("Quantity", x + 1280, y);
                        g2.drawString("Powder", x + 1420, y);
                        g2.setFont(new Font("Serif", Font.BOLD, 28)); // 資料
                        y += 50;
                        while (m_RsData.next()) {
                            g2.drawString(m_RsData.getString("urgent"), x -5, y+40);
                            g2.drawString(++rowNo + ". " + m_RsData.getString("code") +" "+ m_RsData.getString("item")+" "+m_RsData.getString("injection"), x + 85, y);
                            g2.drawString(m_RsData.getString("dosage"), x + 535, y + 40);
                            g2.drawString(m_RsData.getString("usage"), x + 705, y + 40);
                            g2.drawString(m_RsData.getString("way"), x + 1025, y + 40);
                            g2.drawString(m_RsData.getString("day"), x + 1155, y + 40);
                            g2.drawString(m_RsData.getString("quantity"), x + 1285, y + 40);
                            g2.drawString(m_RsData.getString("powder"), x + 1425, y + 40);
                            y += space;
                        }
                        
                        break;
                    case 9:
                        // 9.藥單(藥袋上)
                        String sqlMedicines =
                                "SELECT medicines.code,medicines.injection, medicines.item, medicine_stock.dosage, medicines.unit, medicine_stock.usage, " +
                                       "medicine_stock.way, medicine_stock.day, medicine_stock.quantity, medicine_stock.urgent, " +
                                       "medicine_stock.powder, medicine_stock.ps " +
                                "FROM medicines, medicine_stock, outpatient_services, registration_info " +
                                "WHERE registration_info.guid = '"+m_RegGuid+"' " +
                                    "AND medicine_stock.os_guid = outpatient_services.guid " +
                                    "AND outpatient_services.reg_guid = registration_info.guid " +
                                    "AND medicines.code = medicine_stock.m_code";

                   
                        m_RsData = DBC.executeQuery(sqlMedicines);

                        setLogoTitle(g2, "Medicine Package");
                        x = 10;
                        y = setPatientInfoTitle(g2);
                        g2.setFont(new Font("Serif", Font.BOLD, 30)); // 資料
                        g2.setFont(new Font("---------------", Font.BOLD, 45)); // 資料
                        g2.drawString("Pick No. : " + "1", 1180, 50); // 領藥號
                        space = 110; //間距
                        g2.setFont(new Font("Serif", Font.PLAIN, 28)); // 表頭
                        g2.drawString("Urgent", x - 10, y);
                        g2.drawString("Drug Name", x + 80, y);
                        g2.drawString("Dosage", x + 530, y);
                        g2.drawString("Frequency", x + 700, y);
                        g2.drawString("Usage", x + 1020, y);
                        g2.drawString("Duration", x + 1150, y);
                        g2.drawString("Quantity", x + 1280, y);
                        g2.drawString("Powder", x + 1420, y);
                        g2.setFont(new Font("Serif", Font.BOLD, 28)); // 資料
                        y += 50;
                        rowNo = 0;
                    
                 
                        while (m_RsData.next()) {
                            g2.drawString(m_RsData.getString("urgent"), x -5, y+40);
                            g2.drawString(++rowNo + ". " + m_RsData.getString("code") +" "+ m_RsData.getString("item")+" "+m_RsData.getString("injection"), x + 85, y);
                            g2.drawString(m_RsData.getString("dosage"), x + 535, y + 40);
                            g2.drawString(m_RsData.getString("usage"), x + 705, y + 40);
                            g2.drawString(m_RsData.getString("way"), x + 1025, y + 40);
                            g2.drawString(m_RsData.getString("day"), x + 1155, y + 40);
                            g2.drawString(m_RsData.getString("quantity"), x + 1285, y + 40);
                            g2.drawString(m_RsData.getString("powder"), x + 1425, y + 40);
                            y += space;
                        }

                        break;
                    case 10:
                        // 10.病歷列印(給病歷室)
                        ResultSet rs = null;
                        setLogoTitle(g2, "Prescription Note");
                        x = 10;
                        y = setPatientInfoTitle(g2);
                        g2.setFont(new Font("Serif", Font.BOLD, 28)); // 表頭
                        g2.drawString("======== Summary =========================" + "============================================================================", x, y);
                        String sqlSummary  =
                            "SELECT outpatient_services.summary, outpatient_services.ps " +
                            "FROM outpatient_services, registration_info " +
                            "WHERE registration_info.guid = '"+m_RegGuid+"' " +
                            "AND outpatient_services.reg_guid = registration_info.guid";

                        rs = DBC.executeQuery(sqlSummary);
                        rs.next();


           
                        g2.setFont(new Font("Serif", Font.BOLD, 28)); // 資料

                    //    str:要進行斷行的字串 num:每行字串長度 x:目前所在x值 y:目前所在y值 space:換行間距
                        y = getLineBreaksWord(g2, rs.getString("summary"), 80, x + 5, y ,50);


                        y += 40;
                        space = 50; //間距
                       
                       
                        String sqlDia  =
                            "SELECT diagnosis_code.icd_code, diagnosis_code.name " +
                              "FROM  diagnosis_code, diagnostic, outpatient_services, registration_info " +
                              "WHERE registration_info.guid = '"+m_RegGuid+"' " +
                              "AND diagnostic.os_guid = outpatient_services.guid " +
                              "AND outpatient_services.reg_guid = registration_info.guid " +
                              "AND diagnosis_code.icd_code = diagnostic.dia_code";
                        rs = DBC.executeQuery(sqlDia);
                        if (rs.next()) {
                            g2.setFont(new Font("Serif", Font.BOLD, 28)); // 表頭
                            g2.drawString("======== Diagnosis =========================" + "============================================================================", x, y);
                            rs.beforeFirst();
                            y += 50;
                            g2.setFont(new Font("Serif", Font.BOLD, 28)); // 資料

                            rowNo = 0;
                            while (rs.next()) {
                                g2.drawString(++rowNo + ". "+rs.getString("icd_code") +"  "+rs.getString("name"), x + 75, y);
                                y += space;
                            }
                        }
                        
                         sql =
                            "SELECT prescription_code.code AS code, prescription_code.name AS name, prescription.place, prescription_code.type " +
                            "FROM prescription, prescription_code, outpatient_services, registration_info " +
                            "WHERE registration_info.guid = '"+m_RegGuid+"' " +
                              "AND prescription.os_guid = outpatient_services.guid " +
                              "AND prescription_code.code = prescription.code " +
                              "AND outpatient_services.reg_guid = registration_info.guid " +
                              "AND prescription_code.type <> '"+X_RAY_CODE+"'";
                        rs = DBC.executeQuery(sql);
                        if (rs.next()){
                            space = 50; //間距
                            g2.setFont(new Font("Serif", Font.BOLD, 28)); // 表頭
                            g2.drawString("======== Laboratory =================================" + "============================================================================", x, y);
                            rs.beforeFirst();
                            y += 50;
                            g2.setFont(new Font("Serif", Font.PLAIN, 28)); // 表頭
                            g2.drawString("Urgent", x - 10, y);
                            g2.drawString("Item", x + 80, y);
                            g2.drawString("Type", x + 1100, y);
                            g2.setFont(new Font("Serif", Font.BOLD, 28)); // 資料
                            y += 40;
                            rowNo = 0;
                            while (rs.next()) {
                                g2.drawString(++rowNo + ". " + rs.getString("code") +"  "+rs.getString("name"), x + 85, y);
                                g2.drawString(rs.getString("type"), x + 1105, y);
                                y += space;
                            }
                        }

                       
                        
                         sql =
                            "SELECT prescription_code.code AS code, prescription_code.name AS name, prescription.place, prescription_code.type " +
                            "FROM prescription, prescription_code, outpatient_services, registration_info " +
                            "WHERE registration_info.guid = '"+m_RegGuid+"' " +
                              "AND prescription.os_guid = outpatient_services.guid " +
                              "AND prescription_code.code = prescription.code " +
                              "AND outpatient_services.reg_guid = registration_info.guid "+
                              "AND prescription_code.type = '"+X_RAY_CODE+"' ";
                        rs = DBC.executeQuery(sql);
                        if (rs.next()) {
                             x = 10;
                            space = 50; //間距
                            g2.setFont(new Font("Serif", Font.BOLD, 28)); // 表頭
                            g2.drawString("======== Radiology =================================" + "============================================================================", x, y);
                            rs.beforeFirst();
                            y += 50;
                            g2.setFont(new Font("Serif", Font.PLAIN, 28)); // 表頭
                            g2.drawString("Urgent", x - 10, y);
                            g2.drawString("Item", x + 80, y);
                            g2.setFont(new Font("Serif", Font.BOLD, 28)); // 資料
                            y += 40;
                            rowNo = 0;
                            while (rs.next()) {
                                g2.drawString(++rowNo + ". " + rs.getString("code") +"  "+rs.getString("name"), x + 85, y);
                                y += space;
                            }
                            
                        }
                        
                        
                        sql =
                            "SELECT medicines.code, medicines.item, medicine_stock.dosage, medicines.unit, medicines.injection,medicine_stock.usage, " +
                                   "medicine_stock.way, medicine_stock.day, medicine_stock.quantity, medicine_stock.urgent, " +
                                   "medicine_stock.powder, medicine_stock.ps " +
                            "FROM medicines, medicine_stock, outpatient_services, registration_info " +
                            "WHERE registration_info.guid = '"+m_RegGuid+"' " +
                                "AND medicine_stock.os_guid = outpatient_services.guid " +
                                "AND outpatient_services.reg_guid = registration_info.guid " +
                                "AND medicines.code = medicine_stock.m_code";
                        rs = DBC.executeQuery(sql);
                        if (rs.next()) {
                            x = 10;
                            space = 110; //間距
                            rs.beforeFirst();
                            g2.setFont(new Font("Serif", Font.BOLD, 28)); // 表頭
                            g2.drawString("======== Medicine =================================" + "============================================================================", x, y);
                            y += 50;
                            g2.setFont(new Font("Serif", Font.PLAIN, 28)); // 表頭
                            g2.drawString("Urgent", x - 10, y);
                            g2.drawString("Drug Name", x + 80, y);
                            g2.drawString("Dosage", x + 530, y);
                            g2.drawString("Frequency", x + 700, y);
                            g2.drawString("Usage", x + 1020, y);
                            g2.drawString("Duration", x + 1150, y);
                            g2.drawString("Quantity", x + 1280, y);
                            g2.drawString("Powder", x + 1420, y);
                            g2.setFont(new Font("Serif", Font.BOLD, 28)); // 資料
                            y += 50;
                            rowNo = 0;
                            while (rs.next()) {
                                g2.drawString(++rowNo + ". " + rs.getString("code") +"  "+rs.getString("item") +"  "+rs.getString("injection"), x + 85, y);
                                g2.drawString(rs.getString("dosage"), x + 535, y + 40);
                                g2.drawString(rs.getString("usage"), x + 705, y + 40);
                                g2.drawString(rs.getString("way"), x + 1025, y + 40);
                                g2.drawString(rs.getString("day"), x + 1155, y + 40);
                                g2.drawString(rs.getString("quantity"), x + 1285, y + 40);
                                g2.drawString(rs.getString("powder"), x + 1425, y + 40);
                                y += space;
                            }
                        }
                        
                        break;
                    case 11:
                        sql = "SELECT cashier.no, cashier.amount_receivable, cashier.arrears ," +
                                "cashier.paid_amount , cashier.payment_time, cashier.p_no, concat(patients_info.firstname,'  ',patients_info.lastname) AS 'name' , cashier.s_no, " +
                                "CASE cashier.typ " +
                                "WHEN 'R' THEN 'Registration' " +
                                "WHEN 'X' THEN 'Radiology(X-RAY)' " +
                                "WHEN 'L' THEN 'Laboratory' END 'sys' " +
                                "FROM cashier, patients_info " +
                                "WHERE cashier.p_no = patients_info.p_no AND reg_guid = '"+m_RegGuid+"' AND typ = '"+m_CashierType+"'";
                        System.out.println(sql);
                        rs = DBC.executeQuery(sql);
                        rs.next();
                        String sys = rs.getString("sys"); // 付款類型
                        String paytime = rs.getString("payment_time"); // 付款時間
                        String name = rs.getString("name"); // 付款人
                        String pno = rs.getString("p_no"); // 付款人編號
                        String sno = rs.getString("s_no"); // 收款人編號
                        String no = rs.getString("no");    // 收據編號
                        String arrears = rs.getString("arrears");// 欠款
                        String paid_amount = rs.getString("paid_amount");// 實收
                        String amount_receivable = rs.getString("amount_receivable");// 總金額




                        if (sys.equals("Pharmacy")) {
                            sql = "SELECT medicines.item AS 'Item', " +
                                    "medicine_stock.price AS 'Cost' " +
                                    "FROM medicines, medicine_stock, outpatient_services, registration_info " +
                                    "WHERE registration_info.guid = '" + m_RegGuid + "' " +
                                    "AND medicine_stock.os_guid = outpatient_services.guid " +
                                    "AND outpatient_services.reg_guid = registration_info.guid " +
                                    "AND medicines.code = medicine_stock.m_code";
                        } else if (sys.equals("Laboratory")) {
                            sql = "SELECT concat(prescription.code,'  ',prescription_code.name) AS Item, " +
                                    "prescription.cost AS 'Cost' " +
                                    "FROM prescription LEFT JOIN outpatient_services ON prescription.os_guid = outpatient_services.guid, registration_info, " +
                                         "prescription_code, shift_table, policlinic,poli_room,staff_info " +
                                    "WHERE registration_info.guid = '"+m_RegGuid+"' " +
                                        "AND registration_info.shift_guid = shift_table.guid " +
                                        "AND shift_table.room_guid = poli_room.guid " +
                                        "AND poli_room.poli_guid = policlinic.guid " +
                                        "AND staff_info.s_id = shift_table.s_id " +
                                        "AND  (outpatient_services.reg_guid = registration_info.guid " +
                                        "OR prescription.case_guid = registration_info.guid) " +
                                        "AND prescription_code.code = prescription.code " +
                                        "AND prescription_code.type <> '"+Constant.X_RAY_CODE+"' ";
                        } else if (sys.equals("Registration")) {
                          sql = "SELECT 'Registration' AS Item, " +
                                "registration_info.cost AS 'Cost' "+
                                "FROM registration_info WHERE guid = '"+m_RegGuid+"' ";
                        } else if (sys.equals("Radiology(X-RAY)")) {
                          sql = "SELECT concat(prescription.code,'  ',prescription_code.name) AS Item, " +
                            "prescription.cost AS 'Cost' " +
                            "FROM prescription, outpatient_services, registration_info, " +
                                 "prescription_code, shift_table, policlinic,poli_room,staff_info " +
                            "WHERE registration_info.guid = '"+m_RegGuid+"' " +
                                "AND registration_info.shift_guid = shift_table.guid " +
                                "AND shift_table.room_guid = poli_room.guid " +
                                "AND poli_room.poli_guid = policlinic.guid " +
                                "AND staff_info.s_id = shift_table.s_id " +
                                "AND prescription.os_guid = outpatient_services.guid " +
                                "AND prescription_code.code = prescription.code " +
                                "AND outpatient_services.reg_guid = registration_info.guid " +
                                "AND prescription_code.type = '"+Constant.X_RAY_CODE+"' ";
                        }
                        System.out.println(sql);
//                        rs = DBC.executeQuery(sql);

                        setLogoTitle(g2, "Drug Receipt <Counterfoil>");
                        x = 10;


                        y = setPatientInfoTitle(g2, -1,sys, pno, name,no,paytime, sno);
                        space = 150;//間距
                        g2.setFont(new Font("Serif", Font.PLAIN, 28));  // 表頭
                        g2.drawString("Item", x + 75, y);
                        g2.drawString("Cost", x + 1420, y);

                        g2.setFont(new Font("Serif", Font.BOLD, 28));  // 資料
                        y += 50;

                        rowNo = 0;
//                        while (rs.next()) {
//                            g2.drawString("Cost:", x + 1280, y + 90);
//                            g2.drawRect(x + 1355, y + 65, 160, 33);
//                            g2.drawString(rs.getString("cost"),x+1355,y+95);
//                            y += space;
//                        }

                        g2.drawString(paid_amount, x+300 , y+30);
                        g2.drawString("Paid Amount:____________________", x + 100, y += 30);
                        g2.drawString(arrears, x+800 , y);
                        g2.drawString("Arrears:____________________", x + 600, y  );
                        g2.drawString(amount_receivable, x+1350, y);
                        g2.drawString("Amount Receivable:____________________", x +1000, y);//總金額


                        int yLine = y + 60;  // 列印剪下線
                        y += 60;

                        g2.setFont(new Font("Serif", Font.BOLD, 40));
                        g2.drawString("Drug Receipt", 650, y + 80);

                        x = 10;

                        y = setPatientInfoTitle(g2, y += 100,sys, pno, name,no,paytime, sno);
                        space = 150;//間距
                        g2.setFont(new Font("Serif", Font.PLAIN, 28));  // 表頭

                        g2.drawString("Item", x + 75, y);

                        g2.drawString("Cost", x + 1420, y);

                        g2.setFont(new Font("Serif", Font.BOLD, 28));  // 資料
                        y += 50;

                        rowNo = 0;
//                        rs.beforeFirst();
//                        while (rs.next()) {
//                            g2.drawString("Cost:", x + 1280, y + 90);
//                            g2.drawRect(x + 1355, y + 65, 160, 33);
//                            g2.drawString(rs.getString("cost"),x+1355,y+90);
//                            y += space;
//                        }
                         //Arrears
                        g2.drawString(paid_amount, x+300 , y);
                        g2.drawString("Paid Amount:____________________", x + 100, y );
                        g2.drawString(arrears, x+800 , y);
                        g2.drawString("Arrears:____________________", x + 600, y );
                        g2.drawString(amount_receivable, x+1350, y);
                        g2.drawString("Amount Receivable:____________________", x +1000, y);//總金額
                        g2.setFont(new Font("Serif", Font.PLAIN, 28));  // 表頭
                        try {
                            //BufferedImage bSrc;
                            g2.drawString("---------------------------------------------------------------------------------------------"
                                    + "---------------------------------------------------------------------------------------------", 80, yLine);
                            bSrc = ImageIO.read(new File("./img/cut.png"));
                            AffineTransform at = new AffineTransform();
                            g2.translate(0, yLine - 30);
                            g2.scale(0.6, 0.6);
                            g2.drawRenderedImage(bSrc, at);

                        } catch (IOException ex) {

                        }
                        break;
                     
                        // CASE 的檢驗給病患
                     case 12:
                        // 6.檢驗單(給病患)
                        sql = "SELECT prescription_code.code AS code, prescription_code.name AS name, prescription.place, prescription_code.type " +
                                "FROM prescription, prescription_code, outpatient_services, registration_info " +
                                "WHERE registration_info.guid = '"+m_RegGuid+"' " +
                                  "AND prescription.case_guid = '"+m_RegGuid+"'" +
                                  "AND prescription_code.code = prescription.code " +
                                  "AND outpatient_services.reg_guid = registration_info.guid " +
                                  "AND prescription_code.type <> '"+Constant.X_RAY_CODE+"'";

                        m_RsData = DBC.executeQuery(sql);
                        setLogoTitle(g2, "Laboratory (To The Laboratory)    (For Patient)");
                        x = 60;
                        y = setPatientInfoTitle(g2);
                        space = 50; //間距
                        g2.setFont(new Font("Serif", Font.PLAIN, 32)); // 表頭
                        g2.drawString("Urgent", x - 45, y);
                        g2.drawString("Item", x + 70, y);
                        g2.drawString("Type", x + 1100, y);
                        g2.setFont(new Font("Serif", Font.BOLD, 32)); // 資料
                        y += 40;
                        while (m_RsData.next()) {
                            g2.drawString(++rowNo + ". " + m_RsData.getString("code") +" "+ m_RsData.getString("name"), x + 75, y);
                            g2.drawString(m_RsData.getString("type"), x + 1105, y);
                            y += space;
                        }
                        break;
                }
                return PAGE_EXISTS;
                } catch (SQLException ex) {
                    Logger.getLogger(PrintTools.class.getName()).log(Level.SEVERE, null, ex);
                }
//            finally {
//                        try{DBC.closeConnection(m_Rs);
//                        DBC.closeConnection(m_RsData);}
//                        catch (SQLException e) {
//
//                        }
//                    }
        
                return 0;
            }

        private int setPatientInfoTitle(Graphics2D g2, int sety, String sys, String pno, String name,String no,String date, String cashier) {
            int x = 130;
            int y = 180;   // 起始值
            int space = 60;//間距

            if (sety != -1) {
                y = sety;
            }

            g2.setFont(new Font("Serif", Font.PLAIN, 36));

            g2.drawString("System:", x, y += space);

            g2.drawString("Patient No.:", x, y += space);
            g2.drawString("Name:", x, y += space);


            x = 820;
            y = 180;
            if (sety != -1) {
                y = sety;
            }
            g2.drawString("No.:", x, y += space);
            g2.drawString("Date:", x, y += space);

            g2.drawString("Cashier:", x, y += space);



            g2.setFont(new Font("Serif", Font.BOLD, 32));

            x = 320;
            y = 180;
            if (sety != -1) {
                y = sety;
            }
            g2.drawString(sys, x, y += space);//system
            g2.drawString(pno, x -15, y += space);//pa_no
            g2.drawString(name, x - 55, y += space);//塞Name


            x = 960;
            y = 180;
            if (sety != -1) {
                y = sety;
            }
            g2.drawString(no, x - 40, y += space);//NO
            g2.drawString(date, x - 35, y += space);//繳費時間
            g2.drawString(cashier, x - 15, y += space);//收費人員
  
            g2.drawString("========================================"
                    + "==============================================================", 0, y += space);
            return (y + space);
        }


            private int setPatientInfoTitle(Graphics2D g2) {

                try {
                    int x = 130;
                    int y = 180; // 起始值
                    int space = 60; //間距
                    g2.setFont(new Font("Serif", Font.PLAIN, 36));
                    //                g2.drawString("Modify", 1300, 100);
                    //                g2.drawString("Reprint", 1300, 140);
                    if (m_Type == 1 || m_Type == 4 || m_Type == 6 || m_Type == 12) {
                      g2.drawString("Unpaid", 1300, 180);
                    }
                                    
                    g2.drawString("Patient No.:", x, y += space);
                    g2.drawString("Name:", x, y += space);
                    g2.drawString("Gender:", x, y += space);
                    g2.drawString("Birth:", x, y += space);
                    g2.drawString("Age:", x, y += space);
                    x = 820;
                    y = 180;
                    g2.drawString("Date:", x, y += space);
                    g2.drawString("Shift:", x, y += space);
                    g2.drawString("Dept.:", x, y += space);
                    g2.drawString("Clinic:", x, y += space);
                    g2.drawString("Doctor:", x, y += space);
                    g2.setFont(new Font("Serif", Font.BOLD, 32));
                    x = 320;
                    y = 180;
                    g2.drawString(m_Rs.getString("p_no"), x+10, y += space);
                    g2.drawString(m_Rs.getString("name"), x - 65, y += space);
                    g2.drawString(m_Rs.getString("gender"), x - 45, y += space);
                    g2.drawString(m_Rs.getString("birth"), x - 70, y += space);
                    g2.drawString(m_Rs.getString("age"), x - 90, y += space);
                    x = 960;
                    y = 180;
                    g2.drawString(m_Rs.getString("date"), x - 30, y += space);
                    g2.drawString(m_Rs.getString("shift"), x - 25, y += space);
                    g2.drawString(m_Rs.getString("dept"), x - 20, y += space);
                    g2.drawString(m_Rs.getString("clinic"), x - 10, y += space);
                    g2.drawString(m_Rs.getString("doctor"), x , y += space);
                    g2.drawString("========================================" + "==============================================================", 0, y += space);
                    return y + space;
                } catch (SQLException ex) {
                    Logger.getLogger(PrintTools.class.getName()).log(Level.SEVERE, null, ex);
                }
                return 0;
            }



            private void setLogoTitle(Graphics2D g2, String tabName) {
                String hosName = "Royal Victoria Teaching Hospital"; // 醫院名稱
                try {
                    // -------------　列印醫院LOGO --------------
                    BufferedImage bSrc = ImageIO.read(new File("./img/hospitallogo.png"));
                    AffineTransform at = new AffineTransform();
                    g2.translate(80, 100);
                    g2.scale(0.3, 0.3);
                    g2.drawRenderedImage(bSrc, at);
                    // -------------　列印醫院名稱 --------------
                    g2.setFont(new Font("Serif", Font.BOLD, 48));
                    g2.drawString(hosName , 210, 90);
                    // -------------　列印表單名稱 --------------
                    g2.setFont(new Font("Serif", Font.BOLD, 40));
                    g2.drawString(tabName , 400, 150);
                } catch (IOException ex) {
                     Logger.getLogger(PrintTools.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }


}




