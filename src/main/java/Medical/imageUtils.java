package Medical;

import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageEncoder;
import sun.misc.BASE64Encoder;

import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;
import java.io.*;

public class imageUtils {

    public static String ImageToBase64ByLocal(String imgFile) {
        InputStream in = null;
        byte[] data = null;
        String baseResult = "";
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
            BASE64Encoder encoder = new BASE64Encoder();
            baseResult = encoder.encode(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baseResult;
    }


    public static void main(String[] args) {
//        String tifstr = imageUtils.ImageToBase64ByLocal("E:\\test\\Image002.tif");
        String jpgstr = imageUtils.ImageToBase64ByLocal("E:\\test\\Image001.jpg");
        System.out.println("over");

    }
}
