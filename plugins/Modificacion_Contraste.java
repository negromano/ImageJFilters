
import ij.plugin.filter.PlugInFilter;

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

public class Modificacion_Contraste implements PlugInFilter {

    ImagePlus imp;
    double contraste = 0;
    double brillo = 0, qmin, qmax, rmin, rmax;
    int metodo;

    @Override
    public int setup(String string, ImagePlus imp) {
        this.imp = imp;
        GenericDialog gd = new GenericDialog("Modificacion de Contraste");
        String[] items = {"Metodo 1", "Metodo 2", "Metodo 3", "Metodo 4"};
        gd.addChoice("Metodo", items, "Metodo 1");
        gd.showDialog();
        if (gd.wasCanceled()) {
            return DONE;
        }
        metodo = gd.getNextChoiceIndex() + 1;
        return DOES_8G;
    }

    @Override
    public void run(ImageProcessor ip) {
        qmin = hallarQMin((byte[]) ip.getPixels());
        qmax = halalrQMax((byte[]) ip.getPixels());
        switch (metodo) {
            case 1:
                metodo1(ip);
                break;
            case 2:
                metodo2(ip);
                break;
            case 3:
                metodo3(ip);
                break;
            case 4:
                metodo4(ip);
                break;
        }
    }

    public int hallarQMin(byte[] pixels) {
        int min = (pixels[0] & 0xFF);
        int actual;
        for (byte pixel : pixels) {
            actual =pixel & 0xFF;
            if (actual < min) {
                min = actual;
            }
        }
        return min;
    }

    public int halalrQMax(byte[] pixels) {
        int max = (pixels[0] & 0xFF);
        int actual;
        for (byte pixel : pixels) {
            actual = (pixel & 0xFF);
            if (actual > max) {
                max = actual;
            }
        }
        return max;
    }

    public void metodo1(ImageProcessor ip) {
        byte[] pixels = (byte[]) ip.getPixels();
        int pixel;
        GenericDialog gd = new GenericDialog("Metodo 1");
        gd.addNumericField("K", 0, 1);
        gd.addNumericField("RMin", 0, 1);
        gd.showDialog();
        double k = gd.getNextNumber();
        rmin = gd.getNextNumber();
        for (int i = 0; i < ip.getHeight() * ip.getWidth(); i++) {
            int p = pixels[i] & 0xFF;
            pixel = (int)(((p - qmin) * k) + rmin);
            pixels[i] = (byte) (pixel > 255 ? (byte) 255 : pixel < 0 ? (byte) 0 : (byte) pixel);
        }
        imp.updateAndDraw();
    }

    public void metodo2(ImageProcessor ip) {
        double u;
        byte[] pixels = (byte[]) ip.getPixels();
        int sum = 0, pixel;
        GenericDialog gd = new GenericDialog("Metodo 2");
        gd.addNumericField("K", 0, 1);
        gd.showDialog();
        double k = gd.getNextNumber();
        for (int i = 0; i < ip.getHeight() * ip.getWidth(); i++) {
            sum += pixels[i] & 0xFF;
        }
        u = sum / (ip.getHeight() * ip.getWidth());
        for (int i = 0; i < ip.getHeight() * ip.getWidth(); i++) {
            int p = pixels[i] & 0xFF;
            pixel = (int) (k * (p - u) + u);
            pixels[i] = (byte)pixel > 255 ? (byte) 255 : pixel < 0 ? (byte) 0 : (byte) pixel;
        }
        imp.updateAndDraw();
    }

    public void metodo3(ImageProcessor ip) {
        double m = (qmax - qmin) / 2;
        int pixel;
        byte[] pixels = (byte[]) ip.getPixels();
        GenericDialog gd = new GenericDialog("Metodo 3");
        gd.addNumericField("Contraste", 0, 1);
        gd.showDialog();
        contraste=gd.getNextNumber();
        for (int i = 0; i < ip.getHeight() * ip.getWidth(); i++) {
            int p = pixels[i] & 0xFF;
            pixel = (int) (contraste * (p - m) + m);
            pixels[i] = (byte)pixel > 255 ? (byte) 255 : pixel < 0 ? (byte) 0 : (byte) pixel;
        }
        imp.updateAndDraw();

    }

    public void metodo4(ImageProcessor ip) {
        byte[] pixels = (byte[]) ip.getPixels();
        int pixel;
        GenericDialog gd = new GenericDialog("Metodo 4");
        gd.addNumericField("RMin", 0, 1); //10
        gd.addNumericField("RMax", 0, 1); //255
        gd.showDialog();
        rmin=gd.getNextNumber();
        rmax=gd.getNextNumber();
        for (int i = 0; i < ip.getHeight() * ip.getWidth(); i++) {
            pixel = (int) ((rmin * qmax - rmax * qmin + (rmax - rmin) * (pixels[i] & 0xFF)) / (qmax - qmin));
            pixels[i] = (byte)pixel > 255 ? (byte) 255 : pixel < 0 ? (byte) 0 : (byte) pixel;
        }

        imp.updateAndDraw();
    }

}
