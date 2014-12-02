package com.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.mathworks.toolbox.javabuilder.MWNumericArray;

public class ImageManager {

	public static BufferedImage getImageFromMWNumericArray(MWNumericArray array) {
		int[] dim = array.getDimensions();
		int[] res = new int[dim[0] * dim[1]];		
		for (int i = 0; i < dim[0] * dim[1]; i++) {
			int g = array.getInt(i + 1);
			res[i] = GetColorFromRGB(g, g, g);
		}
		writeImageToFile(getImageFromArray(res, dim[0], dim[1]), "png",
	 "C:\\out.png");		
		return getImageFromArray(res, dim[0], dim[1]);
	}
	
	public static BufferedImage getColorImageFromMWNumericArray(MWNumericArray array) {
		int[] dim = array.getDimensions();
		int iw=dim[1];
		int ih=dim[0];		
		
		System.out.println(iw+" "+ih);
		BufferedImage image = new BufferedImage(iw, ih,
				BufferedImage.TYPE_INT_RGB);		
		for (int i = 0; i < ih; i++) {
			for (int j = 0; j < iw; j++) {
				int r = array.getInt(new int[]{i+1,j+1,1});
				int g = array.getInt(new int[]{i+1,j+1,2});
				int b = array.getInt(new int[]{i+1,j+1,3});
				image.setRGB(j,i, GetColorFromRGB(r, g, b));
			}
		}
		return image;
	}	

	private static int GetColorFromRGB(int r, int g, int b) {
		int col = (r << 16) | (g << 8) | b;
		return col;
	}

	public static BufferedImage getImageFromArray(int[] mas, int ih, int iw) {
		BufferedImage image = new BufferedImage(iw, ih,
				BufferedImage.TYPE_INT_RGB);
		int s = 0;
		for (int i = 0; i < iw; i++) {
			for (int j = 0; j < ih; j++) {
				int g = mas[s];
				image.setRGB(i,j, GetColorFromRGB(g, g, g));
				s++;

			}
		}
		return image;
	}

	public static void writeImageToFile(BufferedImage im, String type,
			String file) {
		try {
			File file1 = new File(file);
			ImageIO.write(im, type, file1);
		} catch (IOException ex) {

		}
	}	
	
	public static int[][] toHalfToningImage(BufferedImage img) {
		int iw = img.getWidth();
		int ih = img.getHeight();
		int[][] image = new int[ih][iw];
		for (int i = 0; i < ih; i++) {
			for (int j = 0; j < iw; j++) {
				int c = img.getRGB(j, i);
				int a = (int) (((c & 16711680) >> 16) * 0.3
						+ ((c & 65280) >> 8) * 0.59 + ((c & 255)) * 0.11);
				image[i][j] = a;
			}
		}
		return image;
	}

}
