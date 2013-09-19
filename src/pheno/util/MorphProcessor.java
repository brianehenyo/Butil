package pheno.util;

import ij.IJ;
import ij.ImagePlus;
import ij.io.Opener;
import ij.process.BinaryProcessor;
import ij.process.ByteProcessor;
import ij.process.ColorProcessor;

public class MorphProcessor {
	private int width;
	private int height;
	private byte[][] image;
	private int[][] labmorph;
	private byte bgcolor;
	private int[] branches;
	private int[] branchAreas;
	private int branchCount;
	private int bitdepth;
	private ImagePlus morphImg;
	
	public byte getBgcolor() {
		return bgcolor;
	}

	public void setBgcolor(byte bgcolor) {
		this.bgcolor = bgcolor;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public byte[][] getImage() {
		return image;
	}

	public int[][] getLabMorph() {
		return labmorph;
	}
	
	public ImagePlus getMorphImage(){
		return morphImg;
	}

	public int[] getBranches() {
		return branches;
	}

	public int[] getBranchAreas() {
		return branchAreas;
	}

	public int getBranchCount() {
		return branchCount;
	}
	
	public MorphProcessor(String path, int mergelen, int prunelen){
		Opener open = new Opener();
		ImagePlus image = open.openImage(path);
		image.updateImage();
		ImagePlus mask = MorphProcessor.imageFromByteArray("Mask", image.getWidth(), image.getHeight(), MorphProcessor.getThresholdMask(image, 220));
		mask.updateImage();
		ByteProcessor byp = new ByteProcessor(image.getWidth(), image.getHeight(), MorphProcessor.getThresholdMask(image, 220), mask.getProcessor().getColorModel());
		BinaryProcessor bp = new BinaryProcessor(byp);
		bp.dilate();
		bp.erode();
		bp.invert();
		bp.skeletonize();
		bp.invert();
		morphImg = new ImagePlus("Morph Image", bp);
		morphImg.updateImage();
		MorphProcessor mp = new MorphProcessor(image.getWidth(), image.getHeight(), morphImg);
		ImagePlus skeleton = new ImagePlus("Skeleton", bp);
		skeleton.updateImage();
		int[][] seg = mp.getLabelledMask(MorphProcessor.byteArrayFromImage(mask), mergelen, prunelen);
		morphImg = mp.convertToColorLabels(seg);
	}
	
	/*
	 * Processes the provided morphological skeleton binary image[]
	 */
	public MorphProcessor(int width, int height, ImagePlus implus){
		this(width, height, byteArrayFromImage(implus));
	}
	
	/*
	 * Processes the provided morphological skeleton binary image[]
	 */
	public MorphProcessor(int width, int height, byte[] image){
		this.width = width;
		this.height = height;
		this.image = get2DFrom1D(width, height, image);
		bgcolor = 0;
		bitdepth = 65536;
		branches = new int[bitdepth];
		branchAreas = new int[bitdepth];
		branchCount = 1;
	}
	
	public static byte[][] get2DFrom1D(int width, int height, byte[] src){
		byte[][] out = new byte[width][height];
		
		for(int i = 0; i < width*height; i++)
			out[i%width][i/width] = src[i];
		
		return out;
	}
	
	public static byte[] get1DFrom2D(byte[][] src){
		byte[] out = new byte[src.length*src[0].length];
		int idx = 0;
		
		for(int j = 0; j < src[0].length; j++)
			for(int i = 0; i < src.length; i++)
				out[idx++] = src[i][j];
		
		return out;
	}	
	
	public static int[] get1DFrom2D(int[][] src){
		int[] out = new int[src.length*src[0].length];
		int idx = 0;
		
		for(int j = 0; j < src[0].length; j++)
			for(int i = 0; i < src.length; i++)
				out[idx++] = src[i][j];
		
		return out;
	}	
	
	public byte[][] clone (byte[][] src){
		byte[][] out = new byte[src.length][src[0].length];
		for(int i = 0; i < src.length; i++){
			for(int j = 0; j < src[i].length; j++)
				out[i][j] = src[i][j];	
		}
		return out;
	}
	
	public int[][] clone (int[][] src){
		int[][] out = new int[src.length][src[0].length];
		for(int i = 0; i < src.length; i++){
			for(int j = 0; j < src[i].length; j++)
				out[i][j] = src[i][j];	
		}
		return out;
	}
	
	public static ImagePlus imageFromByteArray(String title, int width, int height, byte[] src){
		ImagePlus img = IJ.createImage(title, "8-bit", width, height, 1);
		
		for(int i = 0; i < src.length; i++)
			img.getProcessor().setf(i, src[i]);
		
		return img;
	}
	
	public static ImagePlus imageFromIntArray(String title, int width, int height, int[] src){
		ImagePlus img = IJ.createImage(title, "32-bit", width, height, 1);
		
		for(int i = 0; i < src.length; i++)
			img.getProcessor().setf(i, src[i]);
		
		return img;
	}
	
	public static byte[] byteArrayFromImage(ImagePlus img){
		byte[] out = new byte[img.getWidth()*img.getHeight()];
		
		for(int i = 0; i < out.length; i++){
			out[i] = (byte) img.getPixel(i%img.getWidth(), i/img.getWidth())[0];
		}
		return out;
	}
	
	public static int[] intArrayFromImage(ImagePlus img){
		int[] out = new int[img.getWidth()*img.getHeight()];
		
		for(int i = 0; i < out.length; i++){
			out[i] = img.getPixel(i%img.getWidth(), i/img.getWidth())[0];
		}
		return out;
	}
	
	public static byte[] getHueMask(ImagePlus img){
		ColorProcessor cp = (ColorProcessor) img.getProcessor();
		byte[] mask = new byte[img.getWidth()*img.getHeight()];
		byte[] hue = new byte[img.getWidth()*img.getHeight()];
		byte[] sat = new byte[img.getWidth()*img.getHeight()];
		byte[] val = new byte[img.getWidth()*img.getHeight()];
		cp.getHSB(hue, sat, val);

		for(int i = 0; i < mask.length; i++)
			if( ((int)hue[i]&0xFF) >= 43 && ((int)hue[i]&0xFF) <= 95)
				mask[i] = (byte) 255;
			else
				mask[i] = 0;
		
		return mask;
	}
	
	public static byte[] getThresholdMask(ImagePlus img, int thresh){
		byte[] mask = byteArrayFromImage(img);
		
		for(int i = 0; i < mask.length; i++)
			if(((int)mask[i]&0xFF) > thresh)
				mask[i] = (byte) 255;
			else
				mask[i] = 0;
		
		return mask;
	}
	
	public ImagePlus convertToColorLabels(int[][] img){
		int width = img.length;
		int height = img[0].length;
		ColorProcessor cp = new ColorProcessor(width, height);
		byte[] h = new byte[width*height];
		byte[] s = new byte[width*height];
		byte[] b = new byte[width*height];
		int min, max, range;
		
//		min = bitdepth;
//		max = img[0][0];
//		
//		for(int j = 0; j < height; j++){
//			for(int i = 0; i < width; i++){
//				if(img[i][j] > max)
//					max = img[i][j];
//				else if(img[i][j] < min && img[i][j] > 0)
//					min = img[i][j];
//			}
//		}
//		range = max - min;

		for(int j = 0; j < height; j++){
			for(int i = 0; i < width; i++){
				h[i+j*width] = (byte)(img[i][j]*1.0/branchCount*255);
				if(img[i][j] > 0){
					s[i+j*width] = (byte) 255;
					b[i+j*width] = (byte) 255;
				}else{
					s[i+j*width] = 0;
					b[i+j*width] = 0;
				}
			}
		}
		
		cp.setHSB(h, s, b);
		ImagePlus out = new ImagePlus("Color Labelled Mask", cp);
		return out;
	}
	
	/*
	 * Returns the mask passed with labels depending on the closest morphology label to each pixel
	 * Passed mask should have the same width and height as the image stored in the MorphProcessor.
	 * Mask values should follow the same rules as the MorphProcessor (i.e., bgcolor value)
	 * This also updates the branchAreas measurement.
	 */
	public int[][] getLabelledMask(byte[] mask, int merge, int min){
		labmorph = getLabelledMorphology();
		pruneLabelledMorphology(labmorph, merge, min);
		byte[][] mask2d = get2DFrom1D(width, height, mask);
		int[][] out = clone(labmorph);

//		ImagePlus morph = imageFromIntArray("Labelled Morph", width, height, get1DFrom2D(labmorph));
//		morph.show();
		
		while(dilateLabel(out, mask2d) > 0);
		
		return out;
	}
	
	/*
	 * Dilates the labels in image[][] around a 6-neighborhood if those pixels are also foreground pixels in mask[][]
	 */
	private int dilateLabel(int[][] image, byte[][] mask){
		int count = 0; // number of pixels labelled
		int[][] temp = clone(image);
		
		for(int j = 0; j < height; j++){
			for(int i = 0; i < width; i++){
				if(temp[i][j] > 0 && temp[i][j] < bitdepth){
					for(int z = -1; z <= 1; z++)
						for(int y = -1; y <= 1; y++)
							if(isInBounds(i+y, j+z) && image[i+y][j+z] == 0 && mask[i+y][j+z] != bgcolor){
								image[i+y][j+z] = temp[i][j];
								branchAreas[temp[i][j]]++;
								count++;
							}
				}
			}
		}
		
		return count;
	}
	
	private void pruneLabelledMorphologyMergeFirst(int[][] img, int merge, int min){

		int[][] connected = new int[6][3]; // 6-neighbors, x-y-group
		int conCount = 0;
		
		for(int j = 0; j < height; j++)
			for(int i = 0; i < width; i++){
				if(img[i][j] == bitdepth){ // for each joint
					conCount = 0;
					for(int y = -1; y <= 1; y++)
						for(int x = -1; x <= 1; x++)
							if(isInBounds(i+x, j+y) && img[i+x][j+y] > 0 && img[i+x][j+y] < bitdepth){
								connected[conCount][0] = i;
								connected[conCount][1] = j;
								connected[conCount][2] = img[i+x][j+y];
								conCount++;
							}

					for(int m = 0; m < conCount; m++){
						if(branches[connected[m][2]] < merge){ 
							int bestCon = -1;
							int bestDist = 3; // TODO: Max distance possible between vectors in 2x2 space. Make better implementation without hardcode
							for(int n = 0; n < conCount; n++){
								if(m != n && connected[n][2] != connected[m][2]){
									int dist = (int) Math.sqrt(Math.pow((double)(connected[n][0] - connected[m][0]),2.0) + Math.pow((double)(connected[n][1] - connected[m][1]),2.0));
									if(dist < bestDist){
										bestCon = n;
										bestDist = dist;
									}
								}
							}
							
							if(bestCon >= 0){
								//System.out.println("Mering "+connected[m][2]+"("+branches[connected[m][2]]+")"+" ->"+connected[bestCon][2]+"("+branches[connected[bestCon][2]]+")");
								branches[connected[bestCon][2]] += branches[connected[m][2]];
								branches[connected[m][2]] = 0;
								for(int l = 0; l < height; l++) // merge with best pair
									for(int k = 0; k < width; k++){
										if(img[k][l] == connected[m][2]){
											img[k][l] = connected[bestCon][2];
										}
									}
								 connected[m][0] = connected[m][1] = connected[m][2] = 0;
							}
						}
					}
				}
			}
		for(int i = 1; i < branchCount; i++)
			if(branches[i] < min){ // candidate for removal
				int ncount = 0; // count number of neighboring joints (if less than 2 remove this branch)
				for(int v = 0; v < height; v++){
					for(int u = 0; u < width; u++){
						if(img[u][v] == i){
							for(int y = -1; y <= 1; y++){
								for(int x = -1; x <= 1; x++){
									if(isInBounds(u+x, v+y) && img[u+x][v+y] == bitdepth){ // count joint neighbors
										ncount++;
									}
								}
							}
						}
					}
				}
				if(ncount < 2){
					for(int v = 0; v < height; v++)
						for(int u = 0; u < width; u++)
							if(img[u][v] == i)
								img[u][v] = 0;
							
					branches[i] = branchAreas[i] = 0;
				}
			}
		
		packBranchList();
	}
	
	private void pruneLabelledMorphology(int[][] img, int merge, int min){
		for(int i = 1; i < branchCount; i++)
			if(branches[i] < min){ // candidate for removal
				int ncount = 0; // count number of neighboring joints (if less than 2 remove this branch)
				for(int v = 0; v < height; v++){
					for(int u = 0; u < width; u++){
						if(img[u][v] == i){
							for(int y = -1; y <= 1; y++){
								for(int x = -1; x <= 1; x++){
									if(isInBounds(u+x, v+y) && img[u+x][v+y] == bitdepth){ // count joint neighbors
										ncount++;
									}
								}
							}
						}
					}
				}
				if(ncount < 3){
					for(int v = 0; v < height; v++)
						for(int u = 0; u < width; u++)
							if(img[u][v] == i)
								img[u][v] = 0;
							
					branches[i] = branchAreas[i] = 0;
				}
			}
		
		int[][] connected = new int[6][3]; // 6-neighbors, x-y-group
		int conCount = 0;
		
		for(int j = 0; j < height; j++)
			for(int i = 0; i < width; i++){
				if(img[i][j] == bitdepth){ // for each joint
					conCount = 0;
					for(int y = -1; y <= 1; y++)
						for(int x = -1; x <= 1; x++)
							if(isInBounds(i+x, j+y) && img[i+x][j+y] > 0 && img[i+x][j+y] < bitdepth){
								connected[conCount][0] = i;
								connected[conCount][1] = j;
								connected[conCount][2] = img[i+x][j+y];
								conCount++;
							}

					for(int m = 0; m < conCount; m++){
						if(branches[connected[m][2]] < merge){ 
							int bestCon = -1;
							int bestDist = 3; // TODO: Max distance possible between vectors in 2x2 space. Make better implementation without hardcode
							for(int n = 0; n < conCount; n++){
								if(m != n && connected[n][2] != connected[m][2]){
									int dist = (int) Math.sqrt(Math.pow((double)(connected[n][0] - connected[m][0]),2.0) + Math.pow((double)(connected[n][1] - connected[m][1]),2.0));
									if(dist < bestDist){
										bestCon = n;
										bestDist = dist;
									}
								}
							}
							
							if(bestCon >= 0){
								//System.out.println("Mering "+connected[m][2]+"("+branches[connected[m][2]]+")"+" ->"+connected[bestCon][2]+"("+branches[connected[bestCon][2]]+")");
								branches[connected[bestCon][2]] += branches[connected[m][2]];
								branches[connected[m][2]] = 0;
								for(int l = 0; l < height; l++) // merge with best pair
									for(int k = 0; k < width; k++){
										if(img[k][l] == connected[m][2]){
											img[k][l] = connected[bestCon][2];
										}
									}
								 connected[m][0] = connected[m][1] = connected[m][2] = 0;
							}
						}
					}
				}
			}
		packBranchList();
	}
	
	private void packBranchList(){
		for(int i = 0; i < branchCount; i++){
			if(branches[i] < 1){
				int j = i+1;
				while(branches[j] < 1 && j < branchCount && j < bitdepth-1)
					j++;
				branches[i] = branches[j];
				branchAreas[i] = branchAreas[j];
				branches[j] = 0;
				branchAreas[j] = 0;
			}
		}
		
		int last = 0;
		for(int i = 1; i < branchCount && branches[i] > 0; i++)
			last = i;
		branchCount = last+1;
		
		System.out.println("Pruned length "+branchCount);
	}

	
	private int countNeighbors(int[][] image, int x, int y, int n){
		int ncount = 0;
		
		for(int i = -n; i <= n; i++){
			for(int j = -n; j <= n; j++){
				if(isInBounds(x+i, y+j) && image[x+i][y+j] != bgcolor && (i != 0 || j != 0 )){
					ncount++;
				}
			}
		}
		return ncount;
	}
	

	/*
	 * Returns a byte array representing a scanline image of the morphology with each branch marked with unique index values (1-254), all joints are indexed as white (255).
	 * Stores pixel length of branches in branches[] field and returns this informtion in the resultstable
	 */
	public int[][] getLabelledMorphology(){
		byte[][] morph = get2DFrom1D(width, height, getAnalyzedMorphology(1));
		int[][] out = new int[width][height];
		
		// reset counts
		branchCount = 1; // index 0  reserved for future data, also pixel 0 represents background
				
		// build labelled morphology image by checking for neighboring labelled pixels
		for(int i = 0; i < height; i++){
			for(int j = 0; j < width; j++){
				int v = (int)(morph[j][i]&0xFF);
				boolean found = false;
				if(v == 254 || v == 255){ // if the current pixel is a slab or end, mark a label for it
					for(int k = -1; k <= 1 && !found; k++){
						for(int l = -1; l <= 1 && !found; l++){
							if(isInBounds(j+l, i+k) && (int)(morph[j+l][i+k]&0xFF) >= 254 && out[j+l][i+k] > 0){
								out[j][i] = out[j+l][i+k];
								branches[out[j][i]]++;
								branchAreas[out[j][i]]++;
								found = true;
							}
						}
					}
					
					if(!found)
						if(branchCount < bitdepth-2){
							out[j][i] = branchCount++;
							branches[out[j][i]] = branchAreas[out[j][i]] = 1;
						}else{
							out[j][i] =  bitdepth-2;
							branches[out[j][i]]++;
							branchAreas[out[j][i]]++;
						}
					
				}else if(v > 0){ // if the current pixel is a joint, mark it white as a joint
					out[j][i] = bitdepth;
				}
			}
		}
			
		return out;
	}

	/*
	 * Returns a byte array representing a scanline image of the morphology using the following values:
	 * 254 - slab
	 * 255 - end point 
	 * <number of connected>- junction
	 */
	public byte[] getAnalyzedMorphology(int dist){
		byte[] out = new byte[width*height];
		
		for(int i = 0; i < out.length; i++){
			if(image[i%width][i/width] != bgcolor){
				int count = countNeighbors(i%width, i/width, dist);
				if(count > dist*2)
					out[i] = (byte) count; // mark as junction
				else if(count > dist)
					out[i] = (byte) 254; // mark as slab
				else if(count > 0)
					out[i] = (byte) 255; // mark as end point
			}
		}
		
		return out;
	}
	
	/*
	 * Returns the number of neighboring white pixels (255) from x,y based on the n-distance neighborhood
	 */
	private int countNeighbors(int x, int y, int n){
		int ncount = 0;
		
		for(int i = -n; i <= n; i++){
			for(int j = -n; j <= n; j++){
				if(isInBounds(x+i, y+j) && image[x+i][y+j] != bgcolor && (i != 0 || j != 0 )){
					ncount++;
				}
			}
		}
		return ncount;
	}
	
	private boolean isInBounds(int x, int y){
		if(x >= 0 && x < width && y >= 0 && y < height)
			return true;
		else
			return false;
	}
}
