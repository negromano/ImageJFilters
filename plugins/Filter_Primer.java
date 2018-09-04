
import ij.*;
import ij.process.*;
import ij.gui.*;
import java.awt.*;
import ij.plugin.filter.*;
import ij.gui.GenericDialog.*;

public class Filter_Primer implements PlugInFilter {

    ImagePlus imp;
    double contraste;
    int brillo;

    @Override
    public int setup(String arg, ImagePlus imp) {
        this.imp = imp;
        //Para obtener Datos con un cuadro de texto
      GenericDialog gd = new GenericDialog("Cambio de brillo");
      gd.addNumericField("Brillo", 0, 0);
      gd.addNumericField("Contraste", 0, 1);
      gd.showDialog();
      if(gd.wasCanceled()) return DONE;
      brillo = (int)gd.getNextNumber();
      contraste = gd.getNextNumber();
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
 
           pixels[i]= pixel>255?(byte)255:pixel<0?(byte)0:(byte)pixel;
        }
        
        imp.updateAndDraw();
    }

}
