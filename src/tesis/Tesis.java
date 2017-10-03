/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tesis;

import java.io.File;
import java.io.IOException;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.distance.PearsonCorrelationCoefficient;
import net.sf.javaml.featureselection.subset.GreedyForwardSelection;
import net.sf.javaml.tools.data.FileHandler;

/**
 *
 * @author mbernedo
 */
public class Tesis {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        /*double[] a = {2, 2, 5};
        double[] u = {3, 5, 2};
        Pearson corr = new Pearson(a, u);
        double rpta = corr.correlacion();
        System.out.println(rpta);*/
        Dataset data = FileHandler.loadDataset(new File("C:\\Users\\mbernedo.REMAGEOS\\Desktop\\UCI-small\\iris\\iris.data"), 4, ",");
        /*
         * Construct a greedy forward subset selector that will use the Pearson
         * correlation to determine the relation between each attribute and the
         * class label. The first parameter indicates that only one, i.e. 'the
         * best' attribute will be selected.
         */
        GreedyForwardSelection ga = new GreedyForwardSelection(3, new PearsonCorrelationCoefficient());
        /* Apply the algorithm to the data set */
        ga.build(data);
        /* Print out the attribute that has been selected */
        System.out.println(ga.selectedAttributes());
    }

}
