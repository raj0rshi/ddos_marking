/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ddos_marking;

import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author rajor
 */
public class CompareOptimal {

    public static void main(String[] args) throws IOException {
        Node ROOT = new Node(0);

        for (int i = 0; i < 100; i++) {
            ddos_marking.CreateRandomTree.CreateSubtree(3, 3, ROOT);

            
            Optimization1 opt1 = new Optimization1(ROOT);
           // System.out.println(opt1.Nodes.size() );
            if (opt1.Nodes.size() >= 25 && opt1.Nodes.size() <= 38) {
                System.out.println("round: "+i);
                ddos_marking.CreateRandomTree.saveSubtree(ROOT, "test\\test" + i + ".txt");
                // ROOT = ddos_marking.CreateRandomTree.readSubtree("test\\test"+i+".txt");
                Permutation.main(ROOT, 5);
                break;

            }
        }

    }

}
