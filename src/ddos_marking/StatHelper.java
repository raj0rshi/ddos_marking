/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ddos_marking;

import java.util.ArrayList;

/**
 *
 * @author rajor
 */
public class StatHelper {

    static double avg(ArrayList<Double> A) {
        double sum = 0;
        for (double d : A) {
            sum += d;
        }
        return sum / A.size();
    }

    static double max(ArrayList<Double> A) {
        double max = Double.MIN_VALUE;
        for (double d : A) {
            if (max < d) {
                max = d;
            }
        }
        return max;
    }

    static double min(ArrayList<Double> A) {
        double min = Double.MAX_VALUE;
        for (double d : A) {
            if (min > d) {
                min = d;
            }
        }
        return min;
    }

    static double std(ArrayList<Double> A) {

        double avg = avg(A);
        double sum = 0;
        for (double d : A) {
            sum += (avg - d) * (avg - d);
        }
        return Math.sqrt(sum / A.size());
    }

}
