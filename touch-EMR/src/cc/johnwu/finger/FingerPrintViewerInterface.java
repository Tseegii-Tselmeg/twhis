package cc.johnwu.finger;


import java.awt.image.BufferedImage;


public abstract interface FingerPrintViewerInterface {

    public abstract void onFingerDown();

    public abstract void showImage(BufferedImage bufferedimage,String msg);
}
