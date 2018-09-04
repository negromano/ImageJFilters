/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Juan Pablo
 */

import ij.*;
import ij.process.*;
import ij.gui.*;
import ij.plugin.filter.PlugInFilter;
import javax.swing.JOptionPane;

public class Filtro_Selecto implements PlugInFilter{

    int anchoCuadro = 0;
    double k;
    ImagePlus imp;
    
    @Override
    public int setup(String string, ImagePlus ip) {
        GenericDialog gd;
        imp = ip;
        gd = new GenericDialog("Transformación Local del Histograma");
        gd.addNumericField("Ancho del Cuadro", 0, 0);
        gd.addNumericField("Contraste", 0, 1);
        gd.showDialog();
        anchoCuadro = (int)gd.getNextNumber();
        k = gd.getNextNumber();
        return DOES_8G;
    }

    @Override
    public void run(ImageProcessor ip) {
        double promedioLocal, promedioglobal;
        int pixel, alto = ip.getHeight(), ancho = ip.getWidth(), sumatoriaGlobal=0;
        for(int i = 0;i<alto;i++){
            for(int j = 0;j<ancho;j++){
                sumatoriaGlobal+=ip.get(i,j);
            }
        }
        promedioglobal=sumatoriaGlobal/(ancho*alto);
        for(int i = 0;i<alto;i++){
            for(int j = 0;j<ancho;j++){
                int sumatorioLocal=0;
                for(int k = -anchoCuadro/2;k<anchoCuadro/2;k++){
                    for(int l = -anchoCuadro/2;l<anchoCuadro/2;l++){
                        int x = i+k;
                        int y = j+l;
                        if(x<0||x>=ip.getHeight()||y<0||y>=ip.getWidth()){
                            pixel = 0;
                        }else{
                            pixel = ip.get(x, y);
                        }
                        sumatorioLocal+=pixel;
                    }
                }
                promedioLocal = sumatorioLocal/(anchoCuadro*anchoCuadro);
                int r = (int)((promedioglobal*k/(Math.pow(promedioLocal-ip.get(i, j), 2)
                        /anchoCuadro*anchoCuadro))*
                        (ip.get(i, j)-promedioLocal) + promedioLocal);
                if(r>255) r = 255;
                if(r<0) r = 0;
                ip.set(i, j, r);
            }
        }
        imp.updateAndDraw();
    }
    
}
