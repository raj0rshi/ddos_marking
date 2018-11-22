/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ddos_marking;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author rajor
 */
public class ChangeTopology {

  static String input = "F:\\OneDrive - Temple University\\NetBeansProjects\\DDOS_MARKING\\tree_exp_2.txt";
//    static String input = "topologies/test_tree_err.txt";

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

        int B = 3;
        double AR = .75;

        for (int i = 0; i < 1000; i++) {
            // System.out.println("*******************started all*********************");

            Node ROOT = CreateRandomTree.readSubtree(input);
            ROOT = ChangeUserAttackerRatio(ROOT, AR);

            // ddos_marking.graphics.DisplaySimpleTree.DrawTree(ROOT);
            // CreateRandomTree.saveSubtree(ROOT, output);
            Optimization1 OP = new Optimization1(ROOT);

            ArrayList<Integer> F = OP.FindDPAssignment(B);
           // OP.printA();

            double c1 = OP.CalculateCost(F);
            double c2 = OP.CalculateCost2(F);
            double C = (SYSTEM_VARIABLE.OMEGA * c1 + (1 - SYSTEM_VARIABLE.OMEGA) * c2);
            // System.out.println("c1: " + c1 + "\t c2:" + c2);
            //System.out.println("C: " + (SYSTEM_VARIABLE.OMEGA * c1 + (1 - SYSTEM_VARIABLE.OMEGA) * c2));
            // System.out.println("CDP: " + OP.A[0][B]);
            // System.out.println(F);

            if (Math.abs(OP.A[0][B] - C) > 0.001) {
                System.out.println("c1: " + c1 + "\t c2:" + c2);
                System.out.println("C: " + (SYSTEM_VARIABLE.OMEGA * c1 + (1 - SYSTEM_VARIABLE.OMEGA) * c2));
                System.out.println("CDP: " + OP.A[0][B]);
                System.out.println(F);
               // CreateRandomTree.saveSubtree(ROOT, "topologies/test_tree_err.txt");
                break;
            }

        }

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
