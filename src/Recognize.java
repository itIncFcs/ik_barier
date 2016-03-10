import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by admin on 10.03.16.
 */
public class Recognize {

    public static Mat getIMGFromCamera(int camera_id){
        VideoCapture camera = new VideoCapture(camera_id);
        Mat frame = new Mat();
        if (!camera.isOpened())
            System.out.println("Error");
        else {

            if (camera.read(frame)) {
                System.out.println("Frame Obtained");
                System.out.println("Captured Frame Width " +
                        frame.width() + " Height " + frame.height());

                Imgcodecs.imwrite("C:/camera.jpg", frame);
            }
        }
        camera.release();

        return frame;
    }

    public static String recognize() {

        int numberHeightMin = 10;
        int numberHeightMax = 23;
        int numberWidthMin = 100;
        int numberWidthMax = 500;

        int numberImage = 0;

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        Mat m = getIMGFromCamera(0);

        Mat Grey = new Mat(m.size(), Core.NORM_TYPE_MASK);
        Mat Blurr = new Mat(m.size(), Core.NORM_TYPE_MASK);
        Mat imageA = new Mat(m.size(), Core.NORM_TYPE_MASK);
        Imgproc.cvtColor(m, Grey, Imgproc.COLOR_RGBA2GRAY);//в градиции серого
        Imgproc.GaussianBlur(Grey, Blurr, new Size(5,5), 0);//сглаживание
        Imgproc.adaptiveThreshold(Blurr, imageA, 255,Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY,7, 5);

        java.util.List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(imageA, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);



        Tesseract instance = Tesseract.getInstance();

        Mat subimage = new Mat();
        for(int i=0; i< contours.size();i++){
            if (Imgproc.contourArea(contours.get(i)) > 5 * numberHeightMin ){
                Rect rect1 = Imgproc.boundingRect(contours.get(i));
                if(rect1.height > numberHeightMin && rect1.height * 2.5 < rect1.width && rect1.height * 5 > rect1.width && rect1.y>20 && rect1.x>20 && rect1.width < numberWidthMax){
                    Imgproc.rectangle(m, new Point(rect1.x, rect1.y), new Point(rect1.x + rect1.width, rect1.y + rect1.height), new Scalar(0, 0, 255));

                    subimage = m.submat(rect1);
                    Imgproc.resize(subimage, subimage, new Size(250, 50));

                    BufferedImage buffim = new BufferedImage(subimage.width(),subimage.height(),BufferedImage.TYPE_BYTE_GRAY);

                    Imgcodecs.imwrite("C:/test3.jpg", subimage);
                    File imageFile = new File("C:/3.jpg");

                    BufferedImage qwe;
                try {
                    String result = instance.doOCR(imageFile);
                    System.out.println(result);

                            result = result.toUpperCase();
                            if (result.length()>10)
                                result = result.substring(0, 10);

                            char buf[] = new char[1];
                            String res = "";
                            for (int n = 0; n < result.length(); n++)
                            {
                                Pattern p = Pattern.compile("^[ETYOPKHAXCBM0123456789]$");
                                String a = result.substring(n,n+1);
                                Matcher ma = p.matcher(a);
                                if(ma.matches())
                                    res += a;
                                else res += "";
                            }
                            numberImage = i;
                            Pattern p = Pattern.compile("^[ETYOPKHAXCBM]\\d{3}[ETYOPKHAXCBM]{2}\\d{2,3}$");
                            Matcher ma = p.matcher(res);
                            if(ma.matches()) {
                                System.out.println(res);
                                numberImage = i;
                            }
                } catch (TesseractException e) {
                    e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }
}
