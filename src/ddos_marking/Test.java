/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ddos_marking;

import java.io.IOException;

/**
 *
 * @author rajor
 */
public class Test {

    public static void main(String[] args) throws IOException {

        Node ROOT = CreateRandomTree.readSubtree("topologies/test_tree.txt");
          ddos_marking.graphics.DisplaySimpleTree.DrawTree(ROOT);
        OptimalOptimization OPT = new OptimalOptimization(ROOT);
        int B = 5;
        OPT.findOptimalP2(B);
        

    }
}
