package pheno.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import ij.IJ;
import ij.ImagePlus;
import ij.process.ColorProcessor;
import ij.process.ImageStatistics;

public class Phenotype {

	private ImagePlus image;
	private String filename;
	private byte[] mask;
	private ImagePlus hsbImage;
	private byte[] H, S, B;
	private int[] histogram;
	private double[] normalizedHist;
	private int mode;
	private int volumeCount;
	private double volume;
	private int LCC;
	
	public Phenotype(ImagePlus image, String filename)
	{
		this.image = image;
		this.filename = filename;
		processImage();
	}
	
	public void processImage()
	{
		create8Bit();
		createMask();
		createHistogram();
		computeMode();
	}
	
	public void create8Bit()
	{
		H = new byte[image.getWidth() * image.getHeight()];
		S = new byte[image.getWidth() * image.getHeight()];
		B = new byte[image.getWidth() * image.getHeight()];
		ColorProcessor cp = (ColorProcessor) image.getProcessor();
		cp.getHSB(H, S, B);
		
//		try {
//			 
//			File file = new File("C:/Users/Briane/Desktop/Palette/DSCN7141 (Medium).TXT");
// 
//			// if file doesnt exists, then create it
//			if (!file.exists()) {
//				file.createNewFile();
//			}
// 
//			FileWriter fw = new FileWriter(file.getAbsoluteFile());
//			BufferedWriter bw = new BufferedWriter(fw);
//			for (int i = 0; i < H.length; i++) {
//				bw.write(H[i]+"");
//				bw.newLine();
//			}
//			bw.close();
// 
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		hsbImage = IJ.createImage("HSB", "8-bit", image.getWidth(), image.getHeight(), 1);
		ColorProcessor newCP = new ColorProcessor(image.getWidth(), image.getHeight());
		newCP.setHSB(H, S, B);
		hsbImage.setProcessor(newCP);
	}
	
	public void createMask()
	{
		mask = new byte[image.getWidth() * image.getHeight()];
		for (int i = 0; i < H.length; i++) {
			if(H[i] >= (byte) 43 && H[i] <= (byte) 95)
				mask[i] = (byte) 255;
			else
				mask[i] = (byte) 0;
		}
		
//		ImagePlus maskImage = IJ.createImage("HSB", "8-bit", image.getWidth(), image.getHeight(), 1);
//		ColorProcessor newCP = new ColorProcessor(image.getWidth(), image.getHeight());
//		newCP.setHSB(mask, S, B);
//		maskImage.setProcessor(newCP);
//		maskImage.show();
	}
	
	public void createHistogram()
	{
		volumeCount = 0;
		histogram = new int[256];
		normalizedHist = new double[256];
		for (int i = 0; i < histogram.length; i++) {
			histogram[i] = 0;
		}
		
		for (int j = 0; j < H.length; j++) {
			if(mask[j] == (byte) 255)
			{
				histogram[(int) H[j] & 0xFF]++;
				volumeCount++;
			}
		}
		
		volume = 1.0 * volumeCount / (image.getWidth() * image.getHeight()); 
		
//		for (int i = 0; i < histogram.length; i++) {
//			System.out.println(histogram[i]);
//		}
		
		//normalize histogram
		for (int i = 0; i < normalizedHist.length; i++) {
			normalizedHist[i] = 1.0 * histogram[i] / H.length;
		}
		
//		for (int i = 0; i < normalizedHist.length; i++) {
//			System.out.println(normalizedHist[i]);
//		}
	}
	
	public void saveHistogram(String filename)
	{
		try {
			 
			File file = new File(filename);
 
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
 
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			for (int i = 0; i < normalizedHist.length; i++) {
				bw.write(normalizedHist[i]+"");
				bw.newLine();
			}
			bw.close();
 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void computeMode()
	{
		mode = 0;
		for (int i = 1; i < histogram.length; i++) {
			if(histogram[i] > histogram[mode])
				mode = i;
		}
		
		System.out.println("Mode============================");
		System.out.println(mode);
	}

	public ImagePlus getImage() {
		return image;
	}

	public void setImage(ImagePlus image) {
		this.image = image;
	}

	public byte[] getMask() {
		return mask;
	}

	public void setMask(byte[] mask) {
		this.mask = mask;
	}

	public ImagePlus getHsbImage() {
		return hsbImage;
	}

	public void setHsbImage(ImagePlus hsbImage) {
		this.hsbImage = hsbImage;
	}

	public byte[] getH() {
		return H;
	}

	public void setH(byte[] h) {
		H = h;
	}

	public byte[] getS() {
		return S;
	}

	public void setS(byte[] s) {
		S = s;
	}

	public byte[] getB() {
		return B;
	}

	public void setB(byte[] b) {
		B = b;
	}

	public int[] getHistogram() {
		return histogram;
	}

	public void setHistogram(int[] histogram) {
		this.histogram = histogram;
	}

	public double[] getNormalizedHist() {
		return normalizedHist;
	}

	public void setNormalizedHist(double[] normalizedHist) {
		this.normalizedHist = normalizedHist;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public int getLCC() {
		return LCC;
	}

	public void setLCC(int lCC) {
		LCC = lCC;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public double getVolume() {
		return volume;
	}

	public void setVolume(double volume) {
		this.volume = volume;
	}

	public int getVolumeCount() {
		return volumeCount;
	}

	public void setVolumeCount(int volumeCount) {
		this.volumeCount = volumeCount;
	}
}
