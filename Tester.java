
import java.awt.Color;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.opencv.core.*;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture; 

public class Tester extends JPanel{

    BufferedImage image;

    public static void main (String args[]) throws InterruptedException{
    	
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Tester t = new Tester();
        VideoCapture camera = new VideoCapture(0);
        Mat frame = new Mat();
        JFrame frame0 = new JFrame();
        BufferedImage image;
        
        if(!camera.isOpened()){
            System.out.println("Error");
        }
        else {                  
            while(true){        

                if (camera.read(frame)){

                    
                    frame = t.edges(frame, 20);
                    //frame = t.lines(frame);

                    image = t.MatToBufferedImage(frame);                    
                                      
                    t.windowUpdate(frame0, image, "Image", 40, 60);
                    
                    
                    //t.loadImage("ImageName");
                   // t.drawImage(image, 0, 0, null);

                    //t.window(t.grayscale(image), "Processed Image", 40, 60);

                    //t.window(t.loadImage("ImageName"), "Image loaded", 0, 0);

                    
                }
            }   
        }
        camera.release();
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(image, 0, 0, this);
    }

    public Tester() {
    }

    public Tester(BufferedImage img) {
        image = img;
    }   
    
    public Mat lines(Mat frame)
    {
    	Mat lines = new Mat();
        Imgproc.HoughLines(frame, lines, 1, Math.PI/180, 150); // runs the actual detection
        
        for (int x = 0; x < lines.rows(); x++) {
            double rho = lines.get(x, 0)[0],
                    theta = lines.get(x, 0)[1];
            double a = Math.cos(theta), b = Math.sin(theta);
            double x0 = a*rho, y0 = b*rho;
            Point pt1 = new Point(Math.round(x0 + 1000*(-b)), Math.round(y0 + 1000*(a)));
            Point pt2 = new Point(Math.round(x0 - 1000*(-b)), Math.round(y0 - 1000*(a)));
            Imgproc.line(frame, pt1, pt2, new Scalar(0, 0, 255), 3, Imgproc.LINE_AA, 0);
        }
        return frame;
    }
    
    public Mat edges(Mat frame, int value)
    {
    	Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2GRAY);
        Imgproc.blur(frame, frame, new Size(3, 3));
        Imgproc.Canny(frame, frame, value, value * 3, 3, false);
        Core.add(frame, Scalar.all(0), frame);
        return frame;
    }

    //Show image on window
    public void windowUpdate(JFrame frame0, BufferedImage img, String text, int x, int y) {
        frame0.getContentPane().add(new Tester(img));
        frame0.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame0.setTitle(text);
        frame0.setSize(img.getWidth(), img.getHeight() + 30);
        frame0.setLocation(x, y);
        frame0.setVisible(true);
    }

    //Load an image
    public BufferedImage loadImage(String file) {
        BufferedImage img;

        try {
            File input = new File(file);
            img = ImageIO.read(input);

            return img;
        } catch (Exception e) {
            System.out.println("erro");
        }

        return null;
    }

    //Save an image
    public void saveImage(BufferedImage img) {        
        try {
            File outputfile = new File("Images/new.png");
            ImageIO.write(img, "png", outputfile);
        } catch (Exception e) {
            System.out.println("error");
        }
    }

    //Grayscale filter
    public BufferedImage grayscale(BufferedImage img) {
        for (int i = 0; i < img.getHeight(); i++) {
            for (int j = 0; j < img.getWidth(); j++) {
                Color c = new Color(img.getRGB(j, i));

                int red = (int) (c.getRed() * 0.299);
                int green = (int) (c.getGreen() * 0.587);
                int blue = (int) (c.getBlue() * 0.114);

                Color newColor =
                        new Color(
                        red + green + blue,
                        red + green + blue,
                        red + green + blue);

                img.setRGB(j, i, newColor.getRGB());
            }
        }

        return img;
    }

    public BufferedImage MatToBufferedImage(Mat frame) {
        //Mat() to BufferedImage
        int type = 0;
        if (frame.channels() == 1) {
            type = BufferedImage.TYPE_BYTE_GRAY;
        } else if (frame.channels() == 3) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        BufferedImage image = new BufferedImage(frame.width(), frame.height(), type);
        WritableRaster raster = image.getRaster();
        DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();
        byte[] data = dataBuffer.getData();
        frame.get(0, 0, data);

        return image;
    }

}
