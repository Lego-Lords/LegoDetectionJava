package model;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import view.ImgWindow;

import java.awt.image.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class Image {
	private Mat img;
	private Mat greyImg;
	private Mat edges;
	private Mat lines;
	private String fileName;
	
	public Image(String fileName) {
		this.fileName = fileName;
		loadImage();
		filter();
//		displayImage(img);
//		displayImage(greyImg);
	}
	
	private void loadImage() {
		img = Highgui.imread(fileName);
	}
	
	public void displayImage(Mat toDisplay, String title) {
//		Imgproc.resize(toDisplay, toDisplay, new Size(640, 480));
	    MatOfByte matOfByte = new MatOfByte();
	    Highgui.imencode(getFileExtension(), toDisplay, matOfByte);
	    byte[] byteArray = matOfByte.toArray();
	    BufferedImage bufImage = null;
	    try {
	        InputStream in = new ByteArrayInputStream(byteArray);
	        bufImage = ImageIO.read(in);
	        JFrame frame = new JFrame();
	        frame.setTitle(title);
	        frame.getContentPane().add(new JLabel(new ImageIcon(bufImage)));
	        frame.pack();
	        frame.setVisible(true);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public void detectEdge() {
		edges = new Mat();
		Imgproc.Canny(greyImg, edges, 300, 600, 5, true); 
		displayImage(greyImg, "Grey");
		//ImgWindow.newWindow(edges);
		displayImage(edges, "Edges");
	}
	
	public void fitLines() {
		lines = new Mat();
		int threshold = 50;
		int minLineLength = 75;
		int maxLineGap = 10;
		//threshold: The minimum number of intersections to “detect” a line
		//minLinLength: The minimum number of points that can form a line. Lines with less than this number of points are disregarded.
		//maxLineGap: The maximum gap between two points to be considered in the same line.
		Imgproc.HoughLinesP(edges, lines, 1, Math.PI / 180, threshold, minLineLength, maxLineGap);
		System.out.println(lines.cols());
		for(int i = 0; i < lines.cols(); i++) {
			double[] val = lines.get(0, i);
			Core.line(img, new Point(val[0], val[1]), new Point(val[2], val[3]), new Scalar(255, 255, 0), 2);
		}
		//ImgWindow.newWindow(lines);
		displayImage(img, "Image with Lines");
	}
	
	private void filter() {
		greyImg = new Mat();
		Imgproc.cvtColor(img, greyImg, Imgproc.COLOR_BGR2GRAY);
		double sigmaX = 3;
		Imgproc.GaussianBlur(greyImg, greyImg, new Size(5, 5), sigmaX);
//		blur(greyImg, greyImg, new Size(5, 5));
		Imgproc.adaptiveThreshold(greyImg, greyImg, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 13, 10);
	}
	


	private String getFileExtension() {
		try {
			System.out.println(fileName.substring(fileName.lastIndexOf(".")));
			return fileName.substring(fileName.lastIndexOf("."));
		} catch (Exception e) {
			return "";
		}
	}
}
