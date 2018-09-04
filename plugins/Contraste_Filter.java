import ij.*;
import ij.process.*;
import ij.gui.*;
import java.awt.*;
import ij.plugin.filter.*;
import ij.gui.GenericDialog.*;

public class Contraste_Filter implements PlugInFilter {
	ImagePlus imp;
        double contraste = 0;
        int brillo = 0;

	public int setup(String arg, ImagePlus imp) {
		this.imp = imp;
                GenericDialog gd = new GenericDialog("Cambio de brillo");
                gd.addNumericField("Brillo", 0, 0);
                gd.addNumericField("Contraste", 0, 1);
                gd.showDialog();
                if(gd.wasCanceled()) return DONE;
                brillo = (int)gd.getNextNumber();
                contraste = gd.getNextNumber();
		return DOES_8G;
	}

	public void run(ImageProcessor ip) {
		byte[] pixels = (byte[]) ip.getPixels();
                int pixel;

                for (int i = 0; i < ip.getHeight()* ip.getWidth(); i++) 
                {
                    pixel =(byte) ((pixels [i] & 0xFF)*contraste+brillo);

                   pixels[i]= pixel>255?(byte)255:pixel<0?(byte)0:(byte)pixel;
                }

                imp.updateAndDraw();
	}

}
