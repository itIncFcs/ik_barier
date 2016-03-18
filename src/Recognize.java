import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.*;
import java.util.*;

/**
 * Created by admin on 10.03.16.
 */
public class Recognize {

    private Map<byte[], String> map = new HashMap<>();


    //словарь номеров
    public void Recognize() throws Exception {
        File myFolder = new File("C:/1/text/1");
        File[] files = myFolder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".txt");
            }
        });

        byte[][] symbols = new byte[files.length][625];
        byte[] zxc = {15, 12, 11};
        int i = 0;
        for (File f : files)
        {
            FileInputStream FIS = new FileInputStream(f);
            FIS.read(symbols[i]);
            map.put(symbols[i], f.getName().substring(0,1));
            i++;
        }
    }

    //изображение с камеры
    public Mat getIMGFromCamera(int camera_id){
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

    //из МАТ в BufferedImage
    public BufferedImage mat2Img(Mat in, int w, int h)
    {
        BufferedImage out;
        byte[] data = new byte[w * h * (int)in.elemSize()];
        int type;
        in.get(0, 0, data);

        if(in.channels() == 1)
            type = BufferedImage.TYPE_BYTE_GRAY;
        else
            type = BufferedImage.TYPE_3BYTE_BGR;

        out = new BufferedImage(w, h, type);
        out.getRaster().setDataElements(0, 0, w, h, data);
        return out;
    }

    //поворот
    public Mat Rorate(Mat m, int angle)
    {
        org.opencv.core.Point point = new org.opencv.core.Point(m.width() / 2, m.height() / 2);
        Mat rot_mat = Imgproc.getRotationMatrix2D(point, Math.toRadians(angle), 1);
        Mat dst = new Mat();
        Imgproc.warpAffine(m, m, rot_mat, m.size());
        Imgcodecs.imwrite("C:/test2.jpg", m);
        return m;
    }

    //закраска rus
    public Mat paint(Mat m) throws IOException {
        BufferedImage img = mat2Img(m, m.width(), m.height());
        File imageFile = new File("C:/test5.jpg");
        for (int width = img.getWidth() - 1; width >= (img.getWidth()-1) * 75 / 100; width--)
            for (int heigth = img.getHeight() - 1; heigth >= img.getHeight() * 60 / 100; heigth--)
            {
                img.setRGB(width, heigth, 0xffffff);
            }
        ImageIO.write(img,"jpg",imageFile);
        m = Imgcodecs.imread("C:/test5.jpg");
        return m;
    }

    //распознавание контура номера
    public String recognize(int angle) throws Exception {

        int numberHeightMin = 20;
        int numberHeightMax = 23;
        int numberWidthMin = 100;
        int numberWidthMax = 800;
        int numberImage = 0;

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        //Mat m = getIMGFromCamera(1);
        Mat m = Imgcodecs.imread("3.jpg", Imgproc.COLOR_RGBA2GRAY);
        m = Rorate(m, angle);

        Mat Grey = new Mat(m.size(), Core.NORM_TYPE_MASK);
        Mat Blurr = new Mat(m.size(), Core.NORM_TYPE_MASK);
        Mat imageA = new Mat(m.size(), Core.NORM_TYPE_MASK);
        Imgproc.cvtColor(m, Grey, Imgproc.COLOR_RGBA2GRAY);//в градиции серог
        Imgproc.GaussianBlur(Grey, Blurr, new Size(5,5), 0);//сглаживание
        Imgproc.adaptiveThreshold(Blurr, imageA, 255,Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY,7, 5);

        Imgcodecs.imwrite("4.jpg", Grey);
        Imgcodecs.imwrite("5.jpg", Blurr);
        Imgcodecs.imwrite("6.jpg", imageA);

        java.util.List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(imageA, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

        String out = "";
        Mat subimage = new Mat();
        for(int i=0; i< contours.size();i++){
            if (Imgproc.contourArea(contours.get(i)) > 5 * numberHeightMin){
                Rect rect1 = Imgproc.boundingRect(contours.get(i));
                if(rect1.height > numberHeightMin && rect1.height * 3 < rect1.width && rect1.y>20 && rect1.x>20 && rect1.width < numberWidthMax){
                    Imgproc.rectangle(m, new Point(rect1.x, rect1.y), new Point(rect1.x + rect1.width, rect1.y + rect1.height), new Scalar(0, 0, 255));

                    subimage = m.submat(rect1);
                    Imgproc.resize(subimage, subimage, new Size(225, 45));
                    out = recognizeSymbols(subimage);
                    Imgcodecs.imwrite("111.jpg", subimage);


                }
            }
        }
        Imgcodecs.imwrite("2.jpg", m);
        return out;
    }

    //распознавание символов
    public String recognizeSymbols(Mat m) throws Exception {

        final Map<Integer, String> map2 = new HashMap<Integer, String>();

//m = paint(m);

        Mat Grey = new Mat(m.size(), Core.NORM_TYPE_MASK);
        Mat Blurr = new Mat(m.size(), Core.NORM_TYPE_MASK);
        Mat imageA = new Mat(m.size(), Core.NORM_TYPE_MASK);
        Imgproc.cvtColor(m, Grey, Imgproc.COLOR_RGBA2GRAY);//в градиции серого
        Imgproc.GaussianBlur(Grey, Blurr, new Size(5,5), 0);//сглаживание
        Imgproc.adaptiveThreshold(Blurr, imageA, 255,Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY,15, 8);

        java.util.List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Imgproc.findContours(imageA, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

        String out = "";
        Mat subimage = new Mat();
        for(int i=0; i< contours.size();i++){
            if (Imgproc.contourArea(contours.get(i)) > 75 && Imgproc.contourArea(contours.get(i)) < 500) {
                Rect rect1 = Imgproc.boundingRect(contours.get(i));
                if (rect1.height < 40 && rect1.height > 10) {
                    Imgproc.rectangle(m, new org.opencv.core.Point(rect1.x, rect1.y), new org.opencv.core.Point(rect1.x + rect1.width, rect1.y + rect1.height), new Scalar(0, 0, 255));

                    subimage = m.submat(rect1.y + 1, rect1.y + rect1.height, rect1.x + 1, rect1.x + rect1.width);
                    Imgcodecs.imwrite("C:/1/test7.jpg", subimage);
                    Imgproc.resize(subimage, subimage, new Size(25, 25));
                    Imgcodecs.imwrite("C:/1/test8.jpg", subimage);
                    Imgproc.cvtColor(subimage, subimage, Imgproc.COLOR_RGBA2GRAY);//в градиции серого
                    Imgcodecs.imwrite("C:/1/test9.jpg", subimage);
                    Imgproc.GaussianBlur(subimage, subimage, new Size(5, 5), 0);//сглаживание
                    Imgcodecs.imwrite("C:/1/test99.jpg", subimage);
                    Imgproc.adaptiveThreshold(subimage, subimage, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 15, 0);
                    Imgcodecs.imwrite("C:/1/test6.jpg", subimage);

                    BufferedImage bufIm = mat2Img(subimage, 25, 25);

                    DataBuffer byteOut;
                    int count = 0;

                    byteOut = bufIm.getRaster().getDataBuffer();

                    byte[] zx = new byte[25 * 25];

                    for (int j = 0; j < 25 * 25; j++) {
                        if ((byte) byteOut.getElem(j) == -1)
                            zx[j] = 0;
                        else zx[j] = 1;
                    }

                    int p = 0, pp = 0;
                    double pMax = 0;
                    String pMaxString = "";

                    for (Map.Entry entry : map.entrySet()) {
//получить ключ
                        p = 0;
                        pp = 0;
                        String value = (String) entry.getValue();
//получить значение
                        byte[] key = (byte[]) entry.getKey();

                        for (int j = 0; j < 25 * 25; j++) {
                            if (key[j] == 1) {
                                if (zx[j] == key[j]) {
                                    p++;
                                }
                                pp++;
                            }
                        }

                        if (pMax < (double) p / pp) {
                            pMax = (double) p / pp;
                            pMaxString = value;
                        }

                    }

                    map2.put(rect1.x, pMaxString);

                }
            }
        }


        List<Map.Entry<Integer, String>> list = new ArrayList<Map.Entry<Integer, String>>(map2.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<Integer, String>>() {
            @Override
            public int compare(Map.Entry<Integer, String> o1, Map.Entry<Integer, String> o2) {
                return o1.getKey() - o2.getKey();
            }
        });

        for (Map.Entry<Integer, String> e : list){
            out += e.getValue();
        }

        System.out.println(out);
        Imgcodecs.imwrite("C:/1/test4.jpg", m);

        return out;
    }
}