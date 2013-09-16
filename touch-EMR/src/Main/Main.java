package Main;


import Test.Frm_PoliManage;
import Test.Frm_PrintTest;
import cc.johnwu.login.*;
import cc.johnwu.finger.FingerPrintScanner;


public class Main {
    public static void main(String[] args) throws InterruptedException {
        //啟動指紋機
       // System.out.println(System.getProperty("java.library.path"));
        FingerPrintScanner.open();
        //取得登入使用者資訊，使用者登入後，打開視窗。
        new UserInfo(true).openFrame(new Frm_Main());
    }

}
