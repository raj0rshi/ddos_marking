/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ddos_marking;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author rajor
 */
public class ChangeTopology {

    static String input = "F:\\OneDrive - Temple University\\NetBeansProjects\\DDOS_MARKING\\tree_exp_6.txt";

//    public static void main(String[] args) throws IOException, InterruptedException {
//
//        for (int i = 0; i < 10; i++) {
//            System.out.println("*******************started all*********************");
//
//            Node ROOT = CreateRandomTree.readSubtree(input);
//            ROOT = ChangeUserAttackerRatio(ROOT, .75);
//           // ddos_marking.graphics.DisplaySimpleTree.DrawTree(ROOT);
//            //CreateRandomTree.saveSubtree(ROOT, output);
//
//            CreateRandomTree.StartAll(ROOT);
//
//            for (Thread T : CreateRandomTree.Threads) {
//                T.join();
//            }
//        }
//
//    }
    public static void main(String[] args) throws IOException, InterruptedException {

      //  int B = 20;
        double AR = 0.75;
        int Round = 100;

        // Node ROOT = CreateRandomTree.CreateTree(10,4);
        // ddos_marking.graphics.DisplaySimpleTree.DrawTree(ROOT);
        String log = "F:\\OneDrive - Temple University\\NetBeansProjects\\DDOS_MARKING\\Presenatation\\AR " + AR  + " VAR B.csv";
        FileWriter fw = new FileWriter(new File(log));
        fw.append("B, C1,C2,C, STD C1, STD C2, STD C, \n");

        for (int b = 0; b <= 50; b+=5) {
            System.out.println("B:" + b);

            SYSTEM_VARIABLE.OMEGA = .5;

            ArrayList<Double> C = new ArrayList<Double>();
            ArrayList<Double> C1 = new ArrayList<Double>();
            ArrayList<Double> C2 = new ArrayList<Double>();
            for (int i = 0; i < Round; i++) {
                System.out.print("*");
                // System.out.println("*******************started all*********************");

                Node ROOT = CreateRandomTree.readSubtree(input);
                // System.out.println(ROOT);
                ROOT = ChangeUserAttackerRatio(ROOT, AR);

                // ddos_marking.graphics.DisplaySimpleTree.DrawTree(ROOT);
                // CreateRandomTree.saveSubtree(ROOT, output);
                Optimization1 OP = new Optimization1(ROOT);

                ArrayList<Integer> F = OP.FindDPAssignment(b);
                // OP.printA();

                double c1 = OP.CalculateCost(F);
                double c2 = OP.CalculateCost2(F);
                double c = (SYSTEM_VARIABLE.OMEGA * c1 + (1 - SYSTEM_VARIABLE.OMEGA) * c2);

                C1.add(c1);
                C2.add(c2);
                C.add(c);
                // System.out.println("c1: " + c1 + "\t c2:" + c2);
                //System.out.println("C: " + (SYSTEM_VARIABLE.OMEGA * c1 + (1 - SYSTEM_VARIABLE.OMEGA) * c2));
                // System.out.println("CDP: " + OP.A[0][B]);
                // System.out.println(F);

//                System.out.println("c1: " + c1 + "\t c2:" + c2);
//                System.out.println("C: " + (SYSTEM_VARIABLE.OMEGA * c1 + (1 - SYSTEM_VARIABLE.OMEGA) * c2));
//                System.out.println("CDP: " + OP.A[0][B]);
//                System.out.println(F);
//                // CreateRandomTree.saveSubtree(ROOT, "topologies/test_tree_err.txt");
            }
            System.out.println("");

            fw.append(b + ",");

            fw.append(StatHelper.avg(C1) + ",");
            fw.append(StatHelper.avg(C2) + ",");
            fw.append(StatHelper.avg(C) + ",");
            fw.append(StatHelper.std(C1) + ",");
            fw.append(StatHelper.std(C2) + ",");
            fw.append(StatHelper.std(C) + ",");
            fw.append("\n");
        }
        fw.close();
    }

    public static Node ChangeUserAttackerRatio(Node ROOT, double AR) {
        HashMap<Integer, Node> Nodes = new HashMap<Integer, Node>();
        CreateRandomTree.TreeGetAllNodes(ROOT, Nodes);
        for (Node n : Nodes.values()) {
            if (n.U.size() > 0) {
                User u = n.U.get(0);
                n.U.clear();
                if (Math.random() < AR) {
                    u.isLegit = false;
                } else {
                    u.isLegit = true;
                }
                n.U.add(u);
            }
        }
        return ROOT;
    }

}
