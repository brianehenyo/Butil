package pheno.util;

import ij.IJ;
import ij.ImagePlus;
import ij.io.Opener;
import ij.process.ColorProcessor;

public class Palette {
	
//	public static void main(String[] args) {
//		Opener opener = new Opener();  
//		ImagePlus imp = opener.openImage("C:/Users/Briane/Desktop/Palette/P101.JPG");  
//		byte[] H = new byte[imp.getWidth() * imp.getHeight()];
//		byte[] S = new byte[imp.getWidth() * imp.getHeight()];
//		byte[] B = new byte[imp.getWidth() * imp.getHeight()];
//		byte[] B2 = new byte[imp.getWidth() * imp.getHeight()];
//		ColorProcessor cp = (ColorProcessor) imp.getProcessor();
//		cp.getHSB(H, S, B);
//		int[] hist = cp.getHistogram();
//		long[] hist = cp.getStatistics().getHistogram();
//		System.out.println(cp.getHistogramMin() + " - " + cp.getHistogramMax());
//		System.out.println(cp.getHistogramSize());
//		
//		
//		for (int i = 0; i < hist.length; i++) {
//			System.out.println(hist[i]);
//		}
//				
//		ImagePlus newImp = IJ.createImage("PAL2 HSB", "HSB", imp.getWidth(), imp.getHeight(), 1);
//		ColorProcessor newCP = new ColorProcessor(imp.getWidth(), imp.getHeight());
//		newCP.setHSB(H, S, B);
//		newImp.setProcessor(newCP);
//		newImp.show();
//	}
//	
//	public static byte[] reduceBrightness(byte[] B, int n)
//	{
//		for (int i = 0; i < B.length; i++) {
//			B[i] -= n;
//		}
//		
//		return B;
//	}
//	
//	public static byte[] reduceSaturation(byte[] S, int n)
//	{
//		for (int i = 0; i < S.length; i++) {
//			S[i] -= n;
//		}
//		
//		return S;
//	}
//	
//	public static byte[] reduceHue(byte[] H, int n)
//	{
//		for (int i = 0; i < H.length; i++) {
//			H[i] -= n;
//		}
//		
//		return H;
//	}
	
	
}
