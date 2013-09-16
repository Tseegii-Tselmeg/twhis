/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ErrorMessage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author opo10818
 */
public class StoredErrorMessage  {
    String FileName = "errorMessage.txt" ;
    String FileLocation = "./lib/" ;
    boolean isFirst = false ;
    /**存出錯誤資訊 , project為資料夾 , methods為方法 , variable為當時變數*/
    public void setData(String project , String methods , String errorMessage , String[] variable){
        isFile() ;
        String s_inputData = "\nTime : " + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime()) + "\n" +
                        "Project : "+project + "\n" +
                        "Method : "+methods + "\n" +
                        "ErrorMessage : "+errorMessage + "\n" +
                        "Variable : \n" + getVariable(variable) +
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
    /**存出錯誤資訊 , project為資料夾 , methods為方法 */
    public void setData(String project ,String frm , String methods , String errorMessage){
        isFile() ;
        String s_First = "" ;
        if(isFirst){
            s_First = "\n" ;
        }
        String s_inputData = s_First+"Time : " + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime()) + "\n" +
                        "Frame : " + frm + "\n" +
                        "Project : "+project + "\n" +
                        "Method : "+methods + "\n" +
                        "ErrorMessage : "+errorMessage + "\n" +
                        "-------------------------";
        /*將資料轉換成 byte */
        byte[] b_Writer = s_inputData.getBytes() ;
        
        FileOutputStream writer;
        try {
            writer = new FileOutputStream(FileLocation + FileName, true);
            writer.write(b_Writer);
        }catch (FileNotFoundException ex) {
            System.out.println(ex) ;
        }catch (IOException ex) {
            System.out.println(ex) ;
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