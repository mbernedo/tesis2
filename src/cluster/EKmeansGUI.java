package cluster;

import data.Ruta;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Random;

public class EKmeansGUI {

    private class DoubleEKmeansExt extends DoubleEKmeans {

        public DoubleEKmeansExt(double[][] centroids, double[][] points, boolean equal, DoubleDistanceFunction doubleDistanceFunction, Listener listener) {
            super(centroids, points, equal, doubleDistanceFunction, listener);
        }

        public int[] getAssignments() {
            return assignments;
        }

        public int[] getCounts() {
            return counts;
        }
    }

    private static final int MIN = 0;
    private static final int MAX = 1;
    private static final int LEN = 2;

    private static final int X = 0;
    private static final int Y = 1;
    private static final int Z = 2;
    private static final int W = 3;
    private static final int V = 4;
    private static final Ruta ruta = new Ruta();

    private static final int RESOLUTION = 300;
    private JToolBar toolBar;
    private JPanel canvaPanel;
    private double[][] centroids = null;
    private double[][] points = null;
    private double[][] minmaxlens = null;
    private DoubleEKmeansExt eKmeans = null;
    private String[] lines = null;

    public EKmeansGUI() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(RESOLUTION + 100, RESOLUTION + 100));
        frame.setPreferredSize(new Dimension(RESOLUTION * 2, RESOLUTION * 2));

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        frame.setContentPane(contentPanel);

        toolBar = new JToolBar();
        toolBar.setFloatable(false);
        contentPanel.add(toolBar, BorderLayout.NORTH);

        JButton csvImportButton = new JButton();
        csvImportButton.setAction(new AbstractAction(" Cargar dataset ") {
            public void actionPerformed(ActionEvent ae) {
                csvImport();
            }
        });
        toolBar.add(csvImportButton);

        JButton csvExportButton = new JButton();
        csvExportButton.setAction(new AbstractAction(" Obtener resultados ") {
            public void actionPerformed(ActionEvent ae) {
                csvExport();
            }
        });
        toolBar.add(csvExportButton);

        JButton runButton = new JButton();
        runButton.setAction(new AbstractAction(" Cluster ") {
            public void actionPerformed(ActionEvent ae) {
                start();
            }
        });
        toolBar.add(runButton);

        canvaPanel = new JPanel() {
            @Override
            public void paint(Graphics g) {
                EKmeansGUI.this.paint(g, getWidth(), getHeight());
            }
        };
        contentPanel.add(canvaPanel, BorderLayout.CENTER);

        frame.pack();
        frame.setVisible(true);
    }

    private void enableToolBar(boolean enabled) {
        for (Component c : toolBar.getComponents()) {
            c.setEnabled(enabled);
        }
    }

    private void csvImport() {
        enableToolBar(false);
        eKmeans = null;
        lines = null;
        try {
            minmaxlens = new double[][]{
                {Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY},
                {Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY},
                {0d, 0d, 0d, 0d, 0d}
            };
            java.util.List points = new ArrayList();
            java.util.List lines = new ArrayList();
            BufferedReader reader = new BufferedReader(new FileReader(ruta.getRutaCLus()));
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
                String[] pointString = line.split(",");
                double[] point = new double[5];
                point[X] = Double.parseDouble(pointString[X].trim());
                point[Y] = Double.parseDouble(pointString[Y].trim());
                point[Z] = Double.parseDouble(pointString[Z].trim());
                point[W] = Double.parseDouble(pointString[W].trim());
                point[V] = Double.parseDouble(pointString[V].trim());
                points.add(point);
                if (point[X] < minmaxlens[MIN][X]) {
                    minmaxlens[MIN][X] = point[X];
                }
                if (point[Y] < minmaxlens[MIN][Y]) {
                    minmaxlens[MIN][Y] = point[Y];
                }
                if (point[Z] < minmaxlens[MIN][Z]) {
                    minmaxlens[MIN][Z] = point[Z];
                }
                if (point[W] < minmaxlens[MIN][W]) {
                    minmaxlens[MIN][W] = point[W];
                }
                if (point[V] < minmaxlens[MIN][V]) {
                    minmaxlens[MIN][V] = point[V];
                }
                if (point[X] > minmaxlens[MAX][X]) {
                    minmaxlens[MAX][X] = point[X];
                }
                if (point[Y] > minmaxlens[MAX][Y]) {
                    minmaxlens[MAX][Y] = point[Y];
                }
                if (point[Z] > minmaxlens[MAX][Z]) {
                    minmaxlens[MAX][Z] = point[Z];
                }
                if (point[W] > minmaxlens[MAX][W]) {
                    minmaxlens[MAX][W] = point[W];
                }
                if (point[V] > minmaxlens[MAX][V]) {
                    minmaxlens[MAX][V] = point[V];
                }
            }
            minmaxlens[LEN][X] = minmaxlens[MAX][X] - minmaxlens[MIN][X];
            minmaxlens[LEN][Y] = minmaxlens[MAX][Y] - minmaxlens[MIN][Y];
            minmaxlens[LEN][Z] = minmaxlens[MAX][Z] - minmaxlens[MIN][Z];
            minmaxlens[LEN][W] = minmaxlens[MAX][W] - minmaxlens[MIN][W];
            minmaxlens[LEN][V] = minmaxlens[MAX][V] - minmaxlens[MIN][V];
            reader.close();
            this.points = (double[][]) points.toArray(new double[points.size()][]);
            this.lines = (String[]) lines.toArray(new String[lines.size()]);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        } finally {
            canvaPanel.repaint();
            enableToolBar(true);
        }
    }

    private void csvExport() {
        ArrayList<Cluster> lista = new ArrayList<>();
        Cluster cluster = new Cluster();
        if (eKmeans == null) {
            return;
        }
        enableToolBar(false);
        try {
            double[][] points = this.points;
            int[] assignments = eKmeans.getAssignments();
            if (lines != null) {
                for (int i = 0; i < points.length; i++) {
                    cluster.setCluster(assignments[i]);
                    cluster.setLines(lines[i]);
                    System.out.println(assignments[i] + ",::," + lines[i]);
                    lista.add(cluster);
                }
                System.out.println("aqui");
            } else {
                System.out.println("no hay datos");
            }
        } catch (Exception e) {
            e.printStackTrace(System.err);
        } finally {
            canvaPanel.repaint();
            enableToolBar(true);
        }
    }

    private void start() {
        System.out.println("aqui tambien run");
        new Thread(new Runnable() {
            public void run() {
                enableToolBar(false);
                try {
                    EKmeansGUI.this.run();
                } finally {
                    enableToolBar(true);
                }
            }
        }).start();
    }

    private void run() {
        try {
            URL url = new URL("http://staticmap.openstreetmap.de/staticmap.php?center=" + (minmaxlens[MIN][X] + (minmaxlens[LEN][X] / 2d)) + "," + (minmaxlens[MIN][Y] + (minmaxlens[LEN][Y] / 2d)) + "&zoom=14&size=" + canvaPanel.getWidth() + "x" + canvaPanel.getHeight() + "&maptype=mapnik");
            System.out.println("url:" + url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        int k = 5;
        boolean equal = false;
        centroids = new double[k][5];
        for (int i = 0; i < k; i++) {
            centroids[i][X] = minmaxlens[MIN][X] + (minmaxlens[LEN][X] / 2d);
            centroids[i][Y] = minmaxlens[MIN][Y] + (minmaxlens[LEN][Y] / 2d);
            centroids[i][Z] = minmaxlens[MIN][Z] + (minmaxlens[LEN][Z] / 2d);
            centroids[i][W] = minmaxlens[MIN][W] + (minmaxlens[LEN][W] / 2d);
            centroids[i][V] = minmaxlens[MIN][V] + (minmaxlens[LEN][V] / 2d);
        }
        AbstractEKmeans.Listener listener = null;

        eKmeans = new DoubleEKmeansExt(centroids, points, equal, DoubleEKmeans.EUCLIDEAN_DISTANCE_FUNCTION, listener);
        long time = System.currentTimeMillis();
        eKmeans.run();
        time = System.currentTimeMillis() - time;
        canvaPanel.repaint();
    }

    private void paint(Graphics g, int width, int height) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        if (minmaxlens == null) {
            return;
        }
        double widthRatio = (width - 6d) / minmaxlens[LEN][X];
        double heightRatio = (height - 6d) / minmaxlens[LEN][Y];
        if (points == null) {
            return;
        }
        g.setColor(Color.BLACK);
        for (int i = 0; i < points.length; i++) {
            int px = 3 + (int) (widthRatio * (points[i][X] - minmaxlens[MIN][X]));
            int py = 3 + (int) (heightRatio * (points[i][Y] - minmaxlens[MIN][Y]));
            g.drawRect(px - 2, py - 2, 4, 4);
        }
        if (eKmeans == null) {
            return;
        }
        int[] assignments = eKmeans.getAssignments();
        int[] counts = eKmeans.getCounts();
        int s = 225 / centroids.length;
        for (int i = 0; i < points.length; i++) {
            int assignment = assignments[i];
            if (assignment == -1) {
                continue;
            }
            int cx = 3 + (int) (widthRatio * (centroids[assignment][X] - minmaxlens[MIN][X]));
            int cy = 3 + (int) (heightRatio * (centroids[assignment][Y] - minmaxlens[MIN][Y]));
            int px = 3 + (int) (widthRatio * (points[i][X] - minmaxlens[MIN][X]));
            int py = 3 + (int) (heightRatio * (points[i][Y] - minmaxlens[MIN][Y]));
            int c = assignment * s;
            g.setColor(new Color(c, c, c));
            g.drawLine(cx, cy, px, py);
        }
        g.setColor(Color.GREEN);
        for (int i = 0; i < centroids.length; i++) {
            int cx = 3 + (int) (widthRatio * (centroids[i][X] - minmaxlens[MIN][X]));
            int cy = 3 + (int) (heightRatio * (centroids[i][Y] - minmaxlens[MIN][Y]));
            g.drawLine(cx, cy - 2, cx, cy + 2);
            g.drawLine(cx - 2, cy, cx + 2, cy);
            int count = counts[i];
            g.drawString(String.valueOf(count), cx, cy);
        }
    }

    public static void main(String[] args) {
        new EKmeansGUI();
    }
}
