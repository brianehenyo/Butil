package pheno.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import ij.IJ;
import ij.ImagePlus;
import ij.io.Opener;
import ij.process.ColorProcessor;

public class Greenness {

	double[][] LCC = new double[4][256];
	int[] LCCMode = new int[4];
	ArrayList<Phenotype> plantImages = new ArrayList<Phenotype>();

	public Greenness(String directory) {
		loadPalette();
		loadImages(directory);
		estimateLCCMode();
	}

	public void loadImages(String directoryName) {
		Opener opener = new Opener();
		File directory = new File(directoryName);

		// get all the files from a directory
		File[] fList = directory.listFiles();

		// ImagePlus imp =
		// opener.openImage("C:/Users/Briane/Desktop/PhenotypicALL/DSCN7141 (Medium).JPG");
		// plantImages.add(new Phenotype(imp,"DSCN7141 (Medium).JPG"));
		// plantImages.get(0).saveHistogram("src\\palette\\LCC5.pheno");
		// imp.show();

		for (File file : fList) {
			if (file.isFile()) {
				plantImages.add(new Phenotype(opener.openImage(directoryName
						+ "/" + file.getName()), file.getName()));
			}
		}
	}

	public void loadPalette() {
		// load Histograms
		for (int i = 0; i < 4; i++) {
			try (BufferedReader br = new BufferedReader(new FileReader(
					"src\\palette\\LCC" + (i + 2) + ".pheno"))) {
				String sCurrentLine;
				int j = 0;

				while ((sCurrentLine = br.readLine()) != null) {
					try {
						LCC[i][j] = Double.parseDouble(sCurrentLine);
					} catch (Exception e) {
						LCC[i][j] = 0.0;
					}
					j++;
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// load modes
		try (BufferedReader br = new BufferedReader(new FileReader(
				"src\\palette\\paletteRef.pheno"))) {
			String sCurrentLine;
			int j = 0;

			while ((sCurrentLine = br.readLine()) != null) {
				try {
					LCCMode[j] = Integer.parseInt(sCurrentLine);
				} catch (Exception e) {
					LCCMode[j] = 0;
				}
				j++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void estimateLCCMode() {
		System.out.println("LCC Mode============================");

		for (int i = 0; i < plantImages.size(); i++) {
			if (plantImages.get(i).getMode() >= LCCMode[3]
					&& plantImages.get(i).getMode() <= 95)
				plantImages.get(i).setLCC(5);
			else if (plantImages.get(i).getMode() >= LCCMode[2]
					&& plantImages.get(i).getMode() < LCCMode[3])
				plantImages.get(i).setLCC(4);
			else if (plantImages.get(i).getMode() >= LCCMode[1]
					&& plantImages.get(i).getMode() < LCCMode[2])
				plantImages.get(i).setLCC(3);
			else if (plantImages.get(i).getMode() >= 43
					&& plantImages.get(i).getMode() < LCCMode[1])
				plantImages.get(i).setLCC(2);
			System.out.println(plantImages.get(i).getFilename() + " - "
					+ plantImages.get(i).getLCC() + " - "
					+ plantImages.get(i).getMode() + " - "
					+ plantImages.get(i).getVolume());
		}
	}

	public void estimateLCC() {
		System.out.println("LCC=================================");

		for (int i = 0; i < plantImages.size(); i++) {
			int estLCC = 2;
			double estLCCVal = computeEuclideanDist(plantImages.get(i)
					.getNormalizedHist(), LCC[0]);
			double tempVal;
			for (int j = 1; j < LCC.length; j++) {
				tempVal = computeEuclideanDist(plantImages.get(i)
						.getNormalizedHist(), LCC[j]);
				if (tempVal < estLCCVal) {
					estLCC = j + 2;
					estLCCVal = tempVal;
				}
			}
			plantImages.get(i).setLCC(estLCC);
			System.out.println(plantImages.get(i).getFilename() + " - "
					+ estLCC);
		}
	}

	public double computeEuclideanDist(double[] imgHist, double[] lcc) {
		double sum = 0.0;
		for (int i = 0; i < lcc.length; i++) {
			sum += Math.pow(imgHist[i] - lcc[i], 2);
		}

		return Math.sqrt(sum);
	}
}
