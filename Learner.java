import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;

import javax.swing.JPanel;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
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
 
public class Learner {
   Mat image;
   public static void main(String args[]) { 
      //Loading the OpenCV core library  
      System.loadLibrary( Core.NATIVE_LIBRARY_NAME ); 
      
      String file ="E:\\Documents\\Files\\Code\\OpenSourceClub\\DroneScanner\\Hallway.png"; 
      Learner a = new Learner(file);
      int i = a.getPixel(a.image, 0, 1);
      a.save(a.grey(a.image));
      System.out.println(i);
      a.image = a.reduce(a.image);
      a.detector(a.image, a.image.width()/2, a.image.height(), 10);
      
      Window q = new Window(a.image);
      
   }
   
   public Learner(String location)
   {
	   image = Imgcodecs.imread(location);
	   //System.out.println(image.dump());
	   System.out.println("Image Loaded");
   }
   
   //Grabs an individual pixel from a mat.
   public int getPixel(Mat I, int row, int col)
   {
	  int ave = 0;
	  for(int i = 0; i < 3; ++i)
	  {
	 	  ave += I.get(row, col)[i]; 
	  }
	  ave /= 3;
	  return ave;
   }
   
   public Mat grey(Mat color)
   {
	   Imgproc.blur(color,color,new Size(145,145));
	   Imgproc.cvtColor( color, color, Imgproc.COLOR_BGR2GRAY);
	   return color;
   }
   
   public void save(Mat image)
   {
	   Imgcodecs.imwrite( "E:\\Documents\\Files\\Code\\OpenSourceClub\\OpenCV\\HallwayGrey.png", image);
   }
   
   public void detector(Mat image, int col, int row, int tol)
   {
	   int ave = 0;
	   for(int i = -10; i < 11; ++i)
	   {
		   for(int j = -19; j < 1; ++j)
		   {
			   ave += image.get(row+j, col+i)[0];
		   }
	   }
	   ave /= 400;
	   int div = 10;
	   int[][] edgePts = new int[div][2];
	   for(int deg = 0; deg < 180; deg+=180/div)
	   {
		   int slope = (int) Math.tan(deg);
		   int y = 0;
		   if(slope > 0)
		   {
			   for(int x = 0; image.get(row, col) != null; ++x)
			   {
				   y = x * slope;
				   if(Math.abs((int)image.get(row - y, col + x)[0]) < tol)
				   {
					   edgePts[deg/(180/div)][0] = row - y;
					   edgePts[deg/(180/div)][1] = row - y;
					   break;
				   }
			   }
		   }else
		   {
			   for(int x = 0; image.get(row, col) != null; --x)
			   {
				   y = x * slope;
				   if(Math.abs((int)image.get(row - y, col + x)[0]) < tol)
				   {
					   edgePts[deg/(180/div)][0] = row - y;
					   edgePts[deg/(180/div)][1] = row - y;
					   break;
				   }
			   }
		   }
	   }
	   System.out.println("a");
   }
   
   public Mat reduce(Mat input)
   {
	   int size = 2;
	   int row = input.rows()/size;
	   int col = input.cols()/size;
	   Mat simple = new Mat(row, col, 1);
	   int a;
	   for(int i = 0; i < col; ++i)
	   {
		   for(int j = 0; j < row; ++j)
		   {
			   a = 0;
			   for(int k = 0; k < size; ++k)
			   {
				   for(int m = 0; m < size; ++m)
				   {
					   a += input.get((j*size)+k, (i*size)+m)[0];//this.getPixel(input, (j*8)+k, (i*8)+m);
				   }
			   }
			   simple.put(j, i, (a/(size*size)));
		   }
	   }
	   return simple;
   }
}
/*

import org.opencv.core.Core; 
import org.opencv.core.Mat;  
import org.opencv.imgcodecs.Imgcodecs;

class Learner {

  public static void main(String args[]) {
	System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	
	Imgcodecs imageCodecs = new Imgcodecs();
	Mat matrix = imageCodecs.imread("E:/Documents/Files/Code/Open Source Club/OpenCV/Pixels.jpg");
	//JFrame frame0 = new JFrame();
	System.out.println(matrix);
	
	
	/*
    Mat m = new Mat(5, 10, CvType.CV_8UC1, new Scalar(0));
    System.out.println("OpenCV Mat: " + m);
    Mat mr1 = m.row(1);
    mr1.setTo(new Scalar(1));
    Mat mc5 = m.col(5);
    mc5.setTo(new Scalar(5));
    System.out.println("OpenCV Mat data:\n" + m.dump());
    
    
    System.out.println();
    
  }
	

  
}
*/