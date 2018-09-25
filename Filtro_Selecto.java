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
        gd.showDialog();
        anchoCuadro = (int)gd.getNextNumber();
        return DOES_8G;
    }

    @Override
    public void run(ImageProcessor ip) {
        int[][] copia;
        int alto = ip.getHeight(), ancho = ip.getWidth();
        copia = new int[alto][ancho];
        for(int i = 0;i<alto;i++){
            for(int j = 0;j<ancho;j++){
                copia[i][j] = ip.get(i,j);
            }
        }
        int menor = 0;
        for(int i = anchoCuadro/2;i+anchoCuadro/2<=alto-1;i++){
            for(int j = anchoCuadro/2;j+anchoCuadro/2<=ancho-1;j++){
                int a=0,b=0,c=0,d=0;
                for(int n = (-anchoCuadro/2); n<=(anchoCuadro/2);n++){
                a += (copia[i-n][j-n]);
                b += (copia[i-n][j+n]);
                c += (copia[i][j+n]);
                d += (copia[i+n][j]);
                }
                a/=anchoCuadro;
                b/=anchoCuadro;
                c/=anchoCuadro;
                d/=anchoCuadro;
                if(a<=b&&a<=c&&a<=d){
                    menor = a;
                }else if(b<=a&&b<=c&&b<=d){
                    menor = b;
                }else if(c<=b&&c<=a&&c<=d){
                    menor = c;
                }else if(d<=b&&d<=c&&d<=a){
                    menor = d;
                }
                ip.set(i, j, menor);
            }
        }
        imp.updateAndDraw();
    }
    
}
