/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ddos_marking;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 *
 * @author rajor
 */
public class CreateTreeFromRealData {

    public static void main(String[] args) throws FileNotFoundException {
        Scanner scn = new Scanner(new File("web-NotreDame.txt"));
        HashMap<Integer, Node> Nodes = new HashMap<Integer, Node>();
        HashSet<Integer> Visited = new HashSet<Integer>();
        
        Node root = new Node(0);
        Nodes.put(0, root);
        while (scn.hasNext()) {
            String line = scn.nextLine();
            StringTokenizer strtok = new StringTokenizer(line);
            int i = Integer.parseInt(strtok.nextToken());
            int j = Integer.parseInt(strtok.nextToken());
            Node I = Nodes.get(i);
            if (I == null) {
                I = new Node(i);
                Nodes.put(i, I);
            }
            Visited.add(i);
            if (!Visited.contains(j)) {
                Node J = Nodes.get(j);
                if (J == null) {
                    J = new Node(j);
                    Nodes.put(j, J);
                }
                I.addChild(J);

            }
        }
       // ddos_marking.graphics.DisplaySimpleTree.DrawTree(root);
    }

}
