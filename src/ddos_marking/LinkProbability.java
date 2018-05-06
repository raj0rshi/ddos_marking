/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ddos_marking;

/**
 *
 * @author rajor
 */
public class LinkProbability {

    static double a = .5;
    static int R = 5;

    public static void main(String[] args) {

        double sum = 0;
        for (int i = 1; i < R; i++) {
            // R = 5;

            sum += P(i);
        }

        System.out.println(R / sum);

    }

    public static double expN(int RR, double aa) {
        R=RR;
        a=aa;
        
        double sum = 0;
        for (int i = 1; i < R; i++) {
            // R = 5;

            sum += P(i);
        }

        return Math.ceil(R / sum);

    }

    public static double P(int i) {
        //  System.out.println(i);
        if (i <= 1) {
            return a * a * (R - 1) / (R * (R - 1) / 2);
        }
        if (i >= R) {
            return a * a * (R - 1) / (R * (R - 1) / 2);
        }

        double pm1 = P(i - 1);
        double pp1 = P(1);
        double ret = pm1 * a + (1 - pm1) * P(1)
        + pp1 * a + (1 - pp1) * P(1)
        + pp1*pm1
                ;

        return ret;
    }

    public static int count1bit(int x) {
        int count = 0;
        for (int i = 0; i < 32; i++) {
            if (checkBit(x, i) == 1) {
                count++;
            }
        }
        return count;
    }

    public static int checkBit(int x, int i) {
        int r = x >>> i;
        if (((r & 1) == 0)) {

            return 0;
        } else {
            return 1;
        }
    }

}
