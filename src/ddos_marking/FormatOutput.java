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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 *
 * @author rajor
 */
public class FormatOutput {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        // File f = new File("P" + SYSTEM_VARIABLE.ASSIGNMENT_POLICY + "-B" + SYSTEM_VARIABLE.B + "-MP" + SYSTEM_VARIABLE.MARKING_PROBABILITY + "-output.csv");
        File f = new File("Presenatation\\Ar .25 P1-B10-W0.5-MP0.5-tree_exp_2.txt-output.csv");

        main(f);
    }

    public static void main(File f) throws FileNotFoundException, IOException {
        // File f = new File("P" + SYSTEM_VARIABLE.ASSIGNMENT_POLICY + "-B" + SYSTEM_VARIABLE.B + "-MP" + SYSTEM_VARIABLE.MARKING_PROBABILITY + "-output.csv");
        //File f = new File("run/P3-B30-MP0.5-tree_exp_5.txt-output.csv");
        FileWriter fw = new FileWriter("Presenatation\\Formatted_"+f.getName());

        Scanner scn = new Scanner(f);
        boolean flag = true;

        ArrayList<ArrayList<String>> DB = new ArrayList<ArrayList<String>>();
        ArrayList<String> db = null;
        int n = 0;
        while (scn.hasNext()) {
            String line = scn.nextLine();
            if (line.startsWith("Time")) {
                db = new ArrayList<String>();
                DB.add(db);
                line=line+", ";
            }
            db.add(line);
        }

        int MinIndex = Integer.MAX_VALUE;
        for (ArrayList<String> d : DB) {
            if (MinIndex > d.size()) {
                MinIndex = d.size();
            }
        }
        for (int i = 0; i < MinIndex; i++) {
            for (ArrayList<String> d : DB) {
                fw.append(d.get(i).replaceAll("\\s+","")+",");
                System.out.println(d.get(i));
            }
            fw.append("\n");
        }
        fw.flush();
        fw.close();
    }

}
