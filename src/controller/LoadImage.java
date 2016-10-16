package controller;

import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

public final class LoadImage {
	private static Mat img;
	
	public static void loadImage(String fileName) {
		img = Highgui.imread(fileName);
	}
	
}
