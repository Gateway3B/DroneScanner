import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.opencv.core.Mat;

public class Window extends JPanel{

	JFrame frame;
	
	public Window(Mat image)
	{
		frame = new JFrame();
		
		BufferedImage img = MatToBufferedImage(image);
		
		//JLabel picLabel = new JLabel(new ImageIcon(img));
		//add(picLabel);
		System.out.println("work");
		
		frame.getContentPane().setLayout(new FlowLayout());
		frame.getContentPane().add(new JLabel(new ImageIcon(img)));
		frame.pack();
		frame.setVisible(true);
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setTitle("Window");
	    frame.setSize(img.getWidth(),img.getHeight());//img.getWidth(), img.getHeight() + 30);
	    frame.setLocation(0, 0);
	}
	
	public BufferedImage MatToBufferedImage(Mat frame) {
        //Mat() to BufferedImage
        int type = 0;
        if (frame.channels() == 1) {
            type = BufferedImage.TYPE_BYTE_GRAY;
        } else if (frame.channels() == 3) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        BufferedImage image = new BufferedImage(frame.width(),frame.height(),type);//frame.width(), frame.height(), type);
        WritableRaster raster = image.getRaster();
        DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();
        byte[] data = dataBuffer.getData();
        frame.get(0, 0, data);

        return image;
    }
	
	public void updateImage(Mat image)
	{
		frame.getContentPane().add(new Window(image));
	}
	
}
