/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package GpsLog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class GpsLog  {
    String FileName = "GpsLog.txt" ;
    String FileLocation = "./lib/" ;
    boolean isFirst = false ;
    /** */
    public void setData(String gps, String message){
        isFile() ;
        String s_inputData = "\nTime : " + new SimpleDateFormat("yyyy/MM/dd HH:mm").format(Calendar.getInstance().getTime()) + "\n" +
                        "Gps : "+gps + "\n" +
                        "Message : "+message + "\n" +
                        "-------------------------";
        /*將資料轉換成 byte */
        byte[] b_Writer = s_inputData.getBytes() ;

        FileOutputStream writer;
        try {
            writer = new FileOutputStream(FileLocation + FileName, true);
            writer.write(b_Writer);
        } catch (FileNotFoundException ex) {
        }catch (IOException ex) {
        }
    }
    /**  */
    public void setData(String gps, String country, String state, String town){
        isFile() ;
        String s_inputData = "\nTime : " + new SimpleDateFormat("yyyy/MM/dd HH:mm").format(Calendar.getInstance().getTime()) + "\n" +
                        "Gps : "+gps + "\n" +
                        "Country : "+country + "\n" +
                        "State : "+state + "\n" +
                        "Town : "+town + "\n" +
                        "-------------------------";
        /*將資料轉換成 byte */
        byte[] b_Writer = s_inputData.getBytes() ;

        FileOutputStream writer;
        try {
            writer = new FileOutputStream(FileLocation + FileName, true);
            writer.write(b_Writer);
        } catch (FileNotFoundException ex) {
        }catch (IOException ex) {
        }
    }
  
    /**將字串陣列轉為字串*/
    public String getVariable(String[] variable){
        String str = "" ;
        for(int i = 0 ; i < variable.length ; i++)
            str = str + variable[i] + "\n" ;
        return str ;
    }

    /**確認檔案夾是否存在,不存在則新增此檔案*/
    public void isFile() {
        try{
            File fi = new File(FileLocation,FileName) ;
            if(!fi.exists()){
                fi.createNewFile() ;
                isFirst = false ;
            }else{
                isFirst = true ;
            }
        }catch(IOException e){
            System.out.println(e) ;
        }
    }
}