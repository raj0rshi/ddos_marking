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
public class Analytical {

    public static void main(String[] args) {

        for (int i = 4; i < 5; i++) {
            System.out.println(i + "\t" + count11(i, 3) + " " + getConsCount(i, 3));
        }

    }

    public static double get_number_of_packets(int R, double a) {

        double up = R - 1;
        double down = 0;

        for (int r = 2; r <= R; r++) {

            double fact = count11(R, r);
          //  System.out.print(fact+" ");
            down += Math.pow(a, r) * Math.pow(1 - a, R - r) * fact;
        }
      //  System.out.println("");
        return up / down;
    }

    public static double getConsCount(int R, int r) {
        double ret = 0;

        String s = "";

        for (int i = 0; i < r; i++) {
            s += "1";
        }

        for (int i = R - r; i < R; i++) {
            s += "0";
        }

        ArrayList<String> perm = permutation(s);
        ArrayList<String> perm_uniq = new ArrayList<String>();
        for (String ss : perm) {
            if (!perm_uniq.contains(ss)) {
                perm_uniq.add(ss);
                ret += getCons1Count(ss);
            }
        }
        // System.out.println(perm_uniq.size());
        return ret;
    }

    public static int getCons1Count(String s) {

        int count = 0;
        for (int i = 1; i < s.length(); i++) {
            if (s.charAt(i) == s.charAt(i - 1) && s.charAt(i - 1) == '1') {
                count++;

            }
        }
        return count;
    }

    public static ArrayList<String> permutation(String s) {
        // The result
        ArrayList<String> res = new ArrayList<String>();
        // If input string's length is 1, return {s}
        if (s.length() == 1) {
            res.add(s);
        } else if (s.length() > 1) {
            int lastIndex = s.length() - 1;
            // Find out the last character
            String last = s.substring(lastIndex);
            // Rest of the string
            String rest = s.substring(0, lastIndex);
            // Perform permutation on the rest string and
            // merge with the last character
            res = merge(permutation(rest), last);
        }
        return res;
    }

    /**
     * @param list a result of permutation, e.g. {"ab", "ba"}
     * @param c the last character
     * @return a merged new list, e.g. {"cab", "acb" ... }
     */
    public static ArrayList<String> merge(ArrayList<String> list, String c) {
        ArrayList<String> res = new ArrayList<>();
        // Loop through all the string in the list
        for (String s : list) {
            // For each string, insert the last character to all possible positions
            // and add them to the new list
            for (int i = 0; i <= s.length(); ++i) {
                String ps = new StringBuffer(s).insert(i, c).toString();
                res.add(ps);
            }
        }
        return res;
    }

    public static double count11(int R, int r) {
        int count = 0;
        double c = 0;
        for (int i = 0; i < (1 << (R)); i++) {

            if (count1bit(i) == r) {
                c++;
                //System.out.println(i+":");
                for (int j = 1; j < R; j++) {
                    if (checkBit(i, j) == checkBit(i, j - 1) && checkBit(i, j - 1) == 1) {
                        count++;
                        c++;
                        //  System.out.println(i);
                    }
                }
                // System.out.println(i+":"+c);
            }
        }
        return count/c;
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
