package Registration;

import AutoComplete.CompleterComboBox;
import GPSConn.GPSPosition;
import GpsLog.GpsLog;
import Multilingual.language;
import System.Setting;
import java.awt.Frame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 *
 * @author Steven
 */
public class ReSearchGps  extends Thread {
    private Setting set = new Setting();
   // private String[] message = new String(set.setSystem("MESSAGE")).split("\n") ;
    private String[] line = new String(set.setSystem("GIS")).split("\n") ;

        /*多國語言變數*/
    private language paragraph = new language();
    private String[] languageline = new String(paragraph.setlanguage("REGISTRATION")).split("\n") ;
    private String[] languagemessage = new String(paragraph.setlanguage("MESSAGE")).split("\n") ;

    private GPSPosition GPS = new GPSPosition();
    private GpsLog m_GpsLog = new GpsLog();
    private long m_Time = Long.parseLong(set.getSetting(line, "READTIME"));        // 讀取間隔時間 (秒)
    private boolean m_RunState = true; // 是否持續讀取
    private JLabel m_Port;
    private JLabel m_Gis;
    private CompleterComboBox m_Country;
    Frm_Registration m_Frm_Reg;


    // 偵測port狀態 讀取到的資訊
     ReSearchGps(Frm_Registration frm_Reg, JLabel port, JLabel gis,CompleterComboBox country){
         m_Frm_Reg = frm_Reg;
         m_Gis = gis;
         m_Port =  port;
         m_Country = country;
     }


     // 持續讀取 GPSPosition 資料
    @Override
    public void run(){
        System.out.println("持續讀取 GPSPosition 資料  : "+m_RunState );
     while (m_RunState) {
            try {
                // Not Found GPS USB Port Number

                System.out.println(GPS.getScanPort() + " <-- GPS.getScanPort()");

                if (GPS.getScanPort() != null && GPS.getPosition(GPS.getScanPort()) != null) {
                    
                    // GPS 未偵測到資訊
                    String GpsInfo = GPS.getPosition(GPS.getScanPort());
                    if (GpsInfo.equals("0")) {
                       
                       System.out.println("GPS 未偵測到資訊" );
                       ShowChekClearGis();
                   
                        m_GpsLog.setData("-", "Not Found GPS Information.");

                        if (m_Frm_Reg.m_ShowGPS) {
                            System.out.println("暫停");
                            m_RunState = false;
                        }
                        m_Frm_Reg.ShowGpsFrom();
                        
                    } else {
                        // 如果Default有打勾,又再次偵測到位置跳出提示訊息,將GPS　msgbox清空
                        if (!m_Frm_Reg.m_ShowGPS ) {
                            m_Frm_Reg.m_ShowGPS = true;
                            m_Country.setSelectedIndex(0);
                            System.out.println("又偵測到了");

                            //JOptionPane.showMessageDialog ( null, "GPS signal is detected: "+ GpsInfo.trim()) ;
                            JOptionPane.showMessageDialog(new Frame(), paragraph.getLanguage(languagemessage , "GPSSIGNALISDETECTED")+ GpsInfo.trim());
                        }
                            System.out.println("有取得 " +GpsInfo.trim());
                        m_Gis.setText(GpsInfo.trim());
                        m_GpsLog.setData(GpsInfo.trim(),"");

                    }
                } else {
                     System.out.println("PORT 沒取得");
                     m_Port.setText(paragraph.getLanguage(languagemessage , "NOTFONDGPSUSBPORTNUMBER"));
                     ShowChekClearGis();
                   
                     
                     m_GpsLog.setData("-",paragraph.getLanguage(languagemessage , "NOTFONDGPSINFORMATION"));
                     if (m_Frm_Reg.m_ShowGPS) {
                         System.out.println("暫停");
                         m_RunState = false;
                     }
                    
                     m_Frm_Reg.ShowGpsFrom();
                     //GPS.setConnClose();
                }
                ReSearchGps.sleep(m_Time);
                continue;
            } catch (InterruptedException ex) {
                System.out.println("錯誤");
                //Logger.getLogger(ReSearchGps.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    // 控制是否持續讀取ＧＰＳ
    public void SetRunState(boolean state) {
        System.out.println("控制是否持續讀取ＧＰＳ"  + state);
        m_RunState = state;
    }

    // 關閉讀取ＧＰＳ
    public void SetGpsConnClose() {
        m_RunState = false;
        if (GPS.getScanPort() != null) {
            GPS.setConnClose();
            System.out.println("CLOSE GPS");
        }
        
        
    }

    private void ShowChekClearGis() {
         // 判斷是否有先前保留資訊
        System.out.println("m_Gis " + m_Gis.getText());
        if (!m_Gis.getText().equals("") && m_Gis.getText() != null && m_Frm_Reg.m_ShowGPS && m_RunState) {
            Object[] options = {"YES","NO"};
            int response = JOptionPane.showOptionDialog(
                            new Frame(),
                            paragraph.getLanguage(languagemessage , "GPSDETECTIONOFFAILUREWHETHERTOUSETHELASTINFORMATION"),
                            "Message",
                            JOptionPane.YES_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            options,
                            options[0]
                        );
            if(response!=0 ){
                 m_Gis.setText("");
                m_Frm_Reg.m_ShowGPS = true;
            } else {

                m_Frm_Reg.m_ShowGPS = false;
               
            }
        }
    }

}
