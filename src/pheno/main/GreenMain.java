package pheno.main;

import ij.ImagePlus;
import pheno.util.Greenness;
import pheno.util.MorphProcessor;

public class GreenMain {

	public static void main(String[] args) {
		Greenness greenModule = new Greenness("C:/Users/Briane/Desktop/PhenotypicALL");
//		Greenness greenModule = new Greenness("C:/Users/Briane/Dropbox/BIGAS2HACK/Images and Videos/2013-08-31/RGB");
		MorphProcessor mp = new MorphProcessor("C:/Users/Briane/Dropbox/BIGAS2HACK/Images and Videos/2013-08-31/enzo/image.JPG", 25, 15);
		mp.getMorphImage().show();
//		Greenness greenModule = new Greenness(args[0]);
	}
}
