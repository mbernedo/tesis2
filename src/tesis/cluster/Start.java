package tesis.cluster;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;
import data.Ruta;

public class Start {

    public static void main(String[] args) throws IOException {
        Ruta ruta = new Ruta();
        CSVReader reader = new CSVReader(new FileReader(ruta.getRutaCLus()));
        FileWriter writer = new FileWriter("C:\\Users\\migue_000\\Desktop\\out.csv");
        List<String[]> myEntries = reader.readAll();
        List<Punto> puntos = new ArrayList<Punto>();

        for (String[] strings : myEntries) {
            Punto p = new Punto(strings);
            puntos.add(p);
        }

        KMeans kmeans = new KMeans();
        int k = 5;
        KMeansResultado resultado = kmeans.calcular(puntos, k);
        writer.write("------- Con k=" + k + " ofv=" + resultado.getOfv()
                + "-------\n");
        int i = 0;
        for (Cluster cluster : resultado.getClusters()) {
            i++;
            writer.write("-- Cluster " + i + " --\n");
            for (Punto punto : cluster.getPuntos()) {
                writer.write(punto.toString() + "\n");
            }
            writer.write("\n");
            writer.write(cluster.getCentroide().toString());
            writer.write("\n\n");
        }

        writer.close();
    }
}
