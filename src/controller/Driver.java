package controller;

import java.util.Scanner;

import org.opencv.core.Core;

import model.Image;

public class Driver {

	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		System.out.println("Enter image file name w/ address: ");
		Scanner input = new Scanner(System.in);
		String fileName = input.nextLine();
		Image img = new Image(fileName);
		
		img.detectEdge();
		img.fitLines();
	}

}
