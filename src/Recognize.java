import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import java.awt.image.BufferedImage;
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

    public static BufferedImage mat2Img(Mat in)
    {
        BufferedImage out;
        byte[] data = new byte[225 * 45 * (int)in.elemSize()];
        int type;
        in.get(0, 0, data);

        if(in.channels() == 1)
            type = BufferedImage.TYPE_BYTE_GRAY;
        else
            type = BufferedImage.TYPE_3BYTE_BGR;

        out = new BufferedImage(225, 45, type);
        out.getRaster().setDataElements(0, 0, 225, 45, data);
        return out;
    }

    public static Mat Rorate(Mat m, int angle)
    {
        org.opencv.core.Point point = new org.opencv.core.Point(m.width() / 2, m.height() / 2);
        Mat rot_mat = Imgproc.getRotationMatrix2D(point, Math.toRadians(angle), 1);
        Mat dst = new Mat();
        Imgproc.warpAffine(m, m, rot_mat, m.size());
        Imgcodecs.imwrite("C:/test2.jpg", m);
        return m;
    }

    public static String recognize(int angle) {

        int numberHeightMin = 20;
        int numberHeightMax = 23;
        int numberWidthMin = 100;
        int numberWidthMax = 500;
        int numberImage = 0;

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        Mat m = getIMGFromCamera(0);
        //Mat m = Imgcodecs.imread("C:/1/31.jpg", Imgproc.COLOR_RGBA2GRAY);
        m = Rorate(m, angle);

        Mat Grey = new Mat(m.size(), Core.NORM_TYPE_MASK);
        Mat Blurr = new Mat(m.size(), Core.NORM_TYPE_MASK);
        Mat imageA = new Mat(m.size(), Core.NORM_TYPE_MASK);
        Imgproc.cvtColor(m, Grey, Imgproc.COLOR_RGBA2GRAY);//в градиции серого
        Imgproc.GaussianBlur(Grey, Blurr, new Size(5,5), 0);//сглаживание
        Imgproc.adaptiveThreshold(Blurr, imageA, 255,Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY,7, 5);

        java.util.List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(imageA, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

        Tesseract instance = Tesseract.getInstance();
        String out = "";
        Mat subimage = new Mat();
        for(int i=0; i< contours.size();i++){
            if (Imgproc.contourArea(contours.get(i)) > 5 * numberHeightMin && Imgproc.contourArea(contours.get(i)) < m.height()*m.width()/2){
                Rect rect1 = Imgproc.boundingRect(contours.get(i));
                if(rect1.height > numberHeightMin && rect1.height * 2.5 < rect1.width && rect1.height * 5 > rect1.width && rect1.y>20 && rect1.x>20 && rect1.width < numberWidthMax){
                    Imgproc.rectangle(m, new Point(rect1.x, rect1.y), new Point(rect1.x + rect1.width, rect1.y + rect1.height), new Scalar(0, 0, 255));

                    subimage = m.submat(rect1);
                    Imgproc.resize(subimage, subimage, new Size(225, 45));

                    try {
                        String result = instance.doOCR(mat2Img(subimage));

                        result = result.toUpperCase();
                        if (result.length()>6)
                               result = result.substring(0, 6);
                        String res = "";
                        for (int n = 0; n < result.length(); n++)
                        {
                            Pattern p = Pattern.compile("^[ETYOPKHAXCBM0123456789]$");
                            String a = result.substring(n,n+1);
                            Matcher ma = p.matcher(a);
                            if(ma.matches())
                                res += a;
                        }
                        Pattern p = Pattern.compile("^[ETYOPKHAXCBM]\\d{3}[ETYOPKHAXCBM]{2}$");
                        Matcher ma = p.matcher(res);
                        if(ma.matches()) {
                            out = res;
                        }
                    } catch (TesseractException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return out;
    }
}