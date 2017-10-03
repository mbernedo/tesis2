/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tesis;

import java.util.List;

/**
 *
 * @author mbernedo
 */
public class Pearson {

    /*List<List<Integer>> x;
    int n, m;

    public Pearson(List<List<Integer>> x) {
        this.x = x;
        this.n = x.length;
        this.m = x[1].length;
    }

    public double correlacion(int posI, int posJ) {
        double suma = 0.0;
        for (int i = 0; i < n; i++) {
            suma += x[i];
        }
        double mediaX = suma / n;
        suma = 0.0;
        for (int i = 0; i < n; i++) {
            suma += y[i];
        }
        double mediaY = suma / n;
        double pxy, sx2, sy2;
        pxy = sx2 = sy2 = 0.0;
        for (int i = 0; i < n; i++) {
            pxy += (x[i] - mediaX) * (y[i] - mediaY);
            sx2 += (x[i] - mediaX) * (x[i] - mediaX);
            sy2 += (y[i] - mediaY) * (y[i] - mediaY);
        }
        return pxy / Math.sqrt(sx2 * sy2);
    }

    public double prediccion(double correlacion) {
        double pred = 0.0;
        double suma = 0.0;
        for (int i = 0; i < n; i++) {
            suma += x[i];
        }
        double mediaX = suma / n;
        pred = mediaX;
        return pred;
    }*/
}
