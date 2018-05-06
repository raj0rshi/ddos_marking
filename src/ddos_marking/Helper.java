/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ddos_marking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author rajor
 */
public class Helper {

    public static void main(String[] args) {

        ArrayList<Pair> P = new ArrayList<Pair>();
        P.add(new Pair(1, 2));
        P.add(new Pair(2, 3));
        P.add(new Pair(3, 4));
        P.add(new Pair(1, 4));
        P.add(new Pair(4, -1));
        ArrayList<Integer> path = PairToPath(P);
        for (int x : path) {
            System.out.print(x + " ");
        }

    }

    public static ArrayList<Integer> PairToPath(ArrayList<Pair> P) {

        ArrayList<Integer> Path = new ArrayList<Integer>();
        if (P == null) {
            return Path;
        }
        ArrayList<Pair> Pairs = new ArrayList<Pair>(P);
        while (!Pairs.isEmpty()) {
            int A = FindFirst(Pairs);
            //           System.out.print(A + " ");
            Path.add(A);
            RemoveFirst(Pairs, A);
            //  System.out.println("after removing :" + A);
//            for (Pair p : Pairs) {
//                System.out.print("[" + p.A + "," + p.D + "]");
//            }
            //           System.out.println("");
        }

        return Path;
    }

    private static int FindFirst(ArrayList<Pair> P) {
        int A = 0, D = 0, ANS = 0;
        boolean found = false;

        HashSet<Integer> Ds = new HashSet<Integer>();
        for (Pair p : P) {
            Ds.add(p.D);
        }
        for (Pair p : P) {
            if (!Ds.contains(p.A)) {
                return p.A;
            }
        }

        return -1;
    }

    private static void RemoveFirst(ArrayList<Pair> P, int A) {
        for (int i = 0; i < P.size(); i++) {
            Pair p = P.get(i);
            if (p.A == A) {
                P.remove(i);
                i--;
            }
        }
    }

    public static Node PathsToTree(HashMap<Integer, ArrayList<Integer>> Paths) {
        Node root = null;
 //       System.out.println("in paths to tree : # of paths " + Paths.size());
        HashMap<Integer, Node> Nodes = new HashMap<Integer, Node>();
        
        for (int S : Paths.keySet()) {
//            System.out.print("Path to "+ S +":");
            ArrayList<Integer> path = Paths.get(S);
            for (int x : path) {
//                System.out.print(x+" ");
                Node n = new Node(x);
                Nodes.put(x, n);
            }
 //           System.out.println("");
        }

//        for (int L : Nodes.keySet()) {
//            System.out.print(L+" ");
//        }
//        System.out.println();
        for (int S : Paths.keySet()) {
            ArrayList<Integer> path = Paths.get(S);
            for (int i = 1; i < path.size(); i++) {
                Node A = Nodes.get(path.get(i));
                if(A==null) System.out.println(i);
                Node D = Nodes.get(path.get(i-1));
                A.addChild(D);
            }
        }
        for (int L : Nodes.keySet()) {
            Node n = Nodes.get(L);
      //      System.out.print(n.L + " ->");
//            for (Node c : n.C) {
//                System.out.print(c.L + " ");
//            }
//            System.out.println("");
            if (n.P == null) {
                root = n;
            }
        }

          return root;
    }

}
