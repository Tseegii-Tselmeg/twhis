package cc.johnwu.finger;


import com.griaule.grfingerjava.*;
import java.io.ByteArrayInputStream;
import java.sql.*;
import java.util.UUID;
import java.io.File;


public class FingerPrintScanner implements IStatusEventListener, IImageEventListener, IFingerEventListener{

    private static FingerPrintScanner s_FingerPrintScanner;
    private static FingerPrintViewerInterface s_Frame;
    private static MatchingContext s_FingerprintSDK;
    private static Template s_Template;
    private static FingerprintImage s_Fingerprint;
    private static String s_Quality;

    public static void open(){
        s_FingerPrintScanner = new FingerPrintScanner();
    }

    private FingerPrintScanner(){
        initFingerprintSDK();
    }

    private void initFingerprintSDK(){
        try
        {
            File file = new File(new File("").getCanonicalPath()+"/lib/bin");
            GrFingerJava.setNativeLibrariesDirectory(file);
            GrFingerJava.setLicenseDirectory(file);
            GrFingerJava.initializeCapture(this);
            notifyAll();
            GrFingerJava.finalizeCapture();
        }
        catch(Exception exception)
        {
            System.out.println("mb");
            //System.out.println(exception.getMessage());
        }
    }

    public void onSensorPlug(String s){
        try
        {
            GrFingerJava.startCapture(s, this, this);
        }
        catch(GrFingerJavaException grfingerjavaexception)
        {
            System.out.println(grfingerjavaexception.getMessage());
        }
    }

    public void onSensorUnplug(String s){
        try
        {
            GrFingerJava.stopCapture(s);
        }
        catch(GrFingerJavaException grfingerjavaexception)
        {
            System.out.println(grfingerjavaexception.getMessage());
        }
    }

    public void onImageAcquired(String s, FingerprintImage fingerprintimage){
        s_Fingerprint = fingerprintimage;
        extract();
        //identify();
        s_Frame.onFingerDown();
    }

    public void onFingerDown(String arg0) {
    }

    public void onFingerUp(String arg0) {
    }

    public static String identify(ResultSet resultset){
        try {
            //取得本地端資料庫的資料
            s_FingerprintSDK.prepareForIdentification(s_Template);
            while (resultset.next()) {
                byte[] abyte0 = resultset.getBytes("template");
                Template template1 = new Template(abyte0);
                boolean flag = s_FingerprintSDK.identify(template1);
                if (flag) {
                    s_Frame.showImage(GrFingerJava.getBiometricImage(s_Template, s_Fingerprint, s_FingerprintSDK),s_Quality);
                    return resultset.getString("id");
                }
            }
        } catch (GrFingerJavaException ex) {
            System.out.println(((GrFingerJavaException)ex).getMessage());
        } catch (SQLException ex) {
            System.out.println("Error accessing database.");
        } catch (IllegalArgumentException ex) {
            System.out.println("Fingerprint not found.");
        }
        return "";
    }

    private void extract(){
        //指紋取樣品質
        try{
            s_Template = s_FingerprintSDK.extract(s_Fingerprint);
            s_Quality = "";
            switch(s_Template.getQuality())            {
                case 2: // '\002'
                    s_Quality = "High quality";
                    break;

                case 1: // '\001'
                    s_Quality = "Medium quality";
                    break;

                case 0: // '\0'
                    s_Quality = "Low quality";
                    break;
            }
            s_Frame.showImage(GrFingerJava.getBiometricImage(s_Template, s_Fingerprint),s_Quality);
        }catch(GrFingerJavaException ex){
        }
    }

    public static void enroll(String id,PreparedStatement stmt){
        try{
            //新增指紋至資料庫
            String uuid = UUID.randomUUID().toString();
            stmt.setString(1,uuid);
            stmt.setString(2,id);
            //stmt.setInt(2,Integer.parseInt(id));
            stmt.setBinaryStream(3, new ByteArrayInputStream(s_Template.getData()), s_Template.getData().length);
            stmt.executeUpdate();
        }catch(SQLException ex){
            System.out.println("Insert Error");
        }
    }

    public static void setParentFrame(FingerPrintViewerInterface frame){
        s_Frame = frame;
        s_Frame.showImage(null,"");
        try{
            s_FingerprintSDK = new MatchingContext();
            GrFingerJava.initializeCapture(s_FingerPrintScanner);
        }catch(Exception exception){
        }
    }

    public static void stop(){
        try{
            GrFingerJava.finalizeCapture();
        }catch(Exception exception){
        }
    }

    @Override
    protected void finalize(){
        try{
            GrFingerJava.finalizeCapture();
            if(s_FingerprintSDK!=null) s_FingerprintSDK.destroy();
        }catch(GrFingerJavaException ex){
        }
    }
}
