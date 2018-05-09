/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ddos_marking;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 *
 * @author rajor
 */
public class FormatOutput {

     public static void main(String [] args) throws FileNotFoundException, IOException {
        // File f = new File("P" + SYSTEM_VARIABLE.ASSIGNMENT_POLICY + "-B" + SYSTEM_VARIABLE.B + "-MP" + SYSTEM_VARIABLE.MARKING_PROBABILITY + "-output.csv");
        File f = new File("collected/PNB30.csv");
        FileWriter fw = new FileWriter(f.getAbsolutePath()+".txt");
        int col = 0;
        Scanner scn = new Scanner(f);
        HashMap<Integer, String> H = new HashMap<Integer, String>();
        boolean flag = true;
        while (scn.hasNext()) {
            String line = scn.nextLine();
            StringTokenizer strtok = new StringTokenizer(line, ",");
            if (flag == true) {
                flag = false;
                continue;
            }
            col = strtok.countTokens();
            //  System.out.println(col);
            for (int i = 0; i < col; i++) {
                if (H.containsKey(i)) {
                    String s = H.get(i);
                    s += " " + (strtok.nextToken()).trim() + " ";
                    H.put(i, s);

                } else {
                    H.put(i, strtok.nextToken());
                }
            }
        }
        String A[] = {"T", "C", "AR", "AB", "LR", "LB", "X"};
        for (int i = 0; i < col; i++) {
            fw.append(A[i] + "=[" + H.get(i) + "]\n");

        }
        fw.close();
    }

    public static void main(File f) throws FileNotFoundException, IOException {
        // File f = new File("P" + SYSTEM_VARIABLE.ASSIGNMENT_POLICY + "-B" + SYSTEM_VARIABLE.B + "-MP" + SYSTEM_VARIABLE.MARKING_PROBABILITY + "-output.csv");
        //File f = new File("run/P3-B30-MP0.5-tree_exp_5.txt-output.csv");
        FileWriter fw = new FileWriter(f.getName()+".txt");
        int col = 0;
        Scanner scn = new Scanner(f);
        HashMap<Integer, String> H = new HashMap<Integer, String>();
        boolean flag = true;
        while (scn.hasNext()) {
            String line = scn.nextLine();
            StringTokenizer strtok = new StringTokenizer(line, ",");
            if (flag == true) {
                flag = false;
                continue;
            }
            col = strtok.countTokens();
            //  System.out.println(col);
            for (int i = 0; i < col; i++) {
                if (H.containsKey(i)) {
                    String s = H.get(i);
                    s += " " + (strtok.nextToken()).trim() + " ";
                    H.put(i, s);

                } else {
                    H.put(i, strtok.nextToken());
                }
            }
        }
        String A[] = {"T", "C", "AR", "AB", "LR", "LB", "F", "X"};
        for (int i = 0; i < col; i++) {
            fw.append(A[i] + "=[" + H.get(i) + "]\n");

        }
        fw.close();
    }

}
