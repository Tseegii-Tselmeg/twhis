/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Multilingual;

import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author opo10818
 */
public class language {
    public String setlanguage(String Title){
        try {

            FileReader reader = new FileReader ("./lib/language");
            char buffer[] = new char[100000];
            //利用 Reader 物件的 read 方法讀文字資料
            reader.read(buffer);
            String[] paragraph = new String(buffer).split("@");
            for(int i = 0 ; i<paragraph.length ; i++){
                String[] line = new String(paragraph[i]).split("\n") ;
                if(line[0].equals(Title)){
                    return paragraph[i] ;
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        return null ;
    }
        /**多國語系翻譯*/
    public String getLanguage(String line[] ,String character){
        String word = null ;
        for(int i = 1 ; i<line.length ; i++){
            String[] col = line[i].split("=");
            if(col[0].equals(character))
                word = col[1] ;
        }
        return word ;
    }
}
