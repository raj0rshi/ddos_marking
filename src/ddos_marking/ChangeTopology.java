/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ddos_marking;

import java.io.IOException;
import java.util.HashMap;

/**
 *
 * @author rajor
 */
public class ChangeTopology {

    static String input = "F:\\OneDrive - Temple University\\NetBeansProjects\\DDOS_MARKING\\topologies\\tree_exp_2.txt";
    static String output = "topologies/tree_exp_6_ar_60.txt";

    public static void main(String[] args) throws IOException, InterruptedException {

        for (int i = 0; i < 10; i++) {
            System.out.println("*******************started all*********************");

            Node ROOT = CreateRandomTree.readSubtree(input);
            ROOT = ChangeUserAttackerRatio(ROOT, .75);
           // ddos_marking.graphics.DisplaySimpleTree.DrawTree(ROOT);
            //CreateRandomTree.saveSubtree(ROOT, output);

            CreateRandomTree.StartAll(ROOT);

            for (Thread T : CreateRandomTree.Threads) {
                T.join();
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
