import ij.*;
import ij.process.*;
import ij.gui.*;
import java.awt.*;
import ij.plugin.filter.*;

public class Filter_Inverse implements PlugInFilter {
	ImagePlus imp;

	@Override
    public int setup(String arg, ImagePlus imp) {
        this.imp = imp;
        return DOES_8G;
      
   
        //8g grices
        //8c colores 
   
    }

    @Override
    public void run(ImageProcessor ip) {
       // ip.invert();
        //ip.blurGaussian(2.0);
        //ip.findEdges();
        byte[] pixels = (byte[]) ip.getPixels();
        int pixel;
        
        for (int i = 0; i < ip.getHeight()* ip.getWidth(); i++) 
        {
            pixel =(byte) ((pixels [i] & 0xFF) +50);
 
           pixels[i]=(byte)(-1*pixel+255);
        }
        
        imp.updateAndDraw();
	}

}
