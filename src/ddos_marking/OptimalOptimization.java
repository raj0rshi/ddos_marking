/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ddos_marking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

/**
 *
 * @author rajor
 */
public class OptimalOptimization {

    double[][][] A_1;
    double[][] A_2;
    double[][] A_3;
    int[][][] R_1;
    int[][][] R_2;
    int[][][] R_3;
    double[] AL;
    double[] L;

    final int INF = 9999;

    Node ROOT;
    HashMap<Integer, Node> Nodes;
    ArrayList<Node> NodesTopToButtom;
    ArrayList<Node> NodeButtomToTop;

    HashMap<Integer, Node> LeafNodes;
    HashMap<Integer, Node> NonLeafNodes;
    int[] dist;
    int MaxDelta = 0;

    public OptimalOptimization(Node root) {
        ROOT = root;
        init();
    }

    private void init() {
        Nodes = new HashMap<>();
        LeafNodes = new HashMap<>();
        NonLeafNodes = new HashMap<>();
        NodesTopToButtom = new ArrayList<>();
        NodeButtomToTop = new ArrayList<>();
        ArrayList<Node> N = new ArrayList<Node>();
        N.add(ROOT);

        while (!N.isEmpty()) {
            Node n = N.remove(0);
            NodesTopToButtom.add(n);
            Nodes.put(n.L, n);
            if (n.C.size() == 0) {
                LeafNodes.put(n.L, n);
            } else {
                NonLeafNodes.put(n.L, n);
            }

            MaxDelta = Math.max(MaxDelta, n.C.size());
            //  Collections.reverse(n.C);
            N.addAll(n.C);

        }

        AL = new double[Nodes.size()];
        L = new double[Nodes.size()];
        dist = new int[Nodes.size()];

        for (Node n : Nodes.values()) {
            for (User u : n.U) {
                if (!u.isLegit) {
                    AL[n.L] += u.dataRate;
                } else {
                    L[n.L] += u.dataRate;
                }
            }
        }
        NodeButtomToTop = new ArrayList<>(NodesTopToButtom);
        Collections.reverse(NodeButtomToTop);

        System.out.println(NodeButtomToTop);

        System.out.println("Leafs: " + LeafNodes);
        System.out.println("NonLeafs: " + NonLeafNodes);

        calculateL();
        CalculateDist();
    }

    void calculateL() {
        for (Node n : NodeButtomToTop) {
            if (NonLeafNodes.containsKey(n.L)) {
                //   System.out.println("computing "+ n);
                for (Node c : n.C) {
                    L[n.L] += L[c.L];
                }
            }
        }
    }

    private void CalculateD(Node n, int d) {
        dist[n.L] = d;
        for (Node c : n.C) {
            CalculateD(c, d + 1);
        }

    }

    public void CalculateDist() {
        CalculateD(ROOT, 0);
    }

    void PrintL() {
        System.out.print("L: ");
        int i = 0;
        for (double l : L) {
            System.out.print(i + "[" + l + "] ");
            i++;
        }
        System.out.println("");
    }

    void PrintDist() {
        System.out.print("Dist: ");
        int i = 0;
        for (double l : dist) {
            System.out.print(i + "[" + l + "] ");
            i++;
        }
        System.out.println("");
    }

    void PrintAL() {
        System.out.print("AL: ");
        int i = 0;
        for (double l : AL) {
            System.out.print(i + "[" + l + "] ");
            i++;
        }
        System.out.println("");
    }

    void PrintA(double[][][] A) {
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                String str = "";
                for (int k = 0; k < A[i][j].length; k++) {
                    str = str + A[i][j][k] + " ";
                }
                System.out.print("(" + i + "," + j + ")" + (str + "             ").substring(0, 10));
            }
            System.out.println("");
        }
        System.out.println("");

    }

    void PrintA(int[][][] A) {
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                String str = "";
                for (int k = 0; k < A[i][j].length; k++) {
                    str = str + A[i][j][k] + " ";
                }
                System.out.print("(" + i + "," + j + ")" + (str + "             ").substring(0, 10));
            }
            System.out.println("");
        }
        System.out.println("");

    }

    void PrintA(double[][] A) {
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                String str = A[i][j] + "";
                if (A[i][j] >= INF) {
                    str = "INF";
                }
                System.out.print("(" + i + "," + j + ")" + (str + "             ").substring(0, 10));
            }
            System.out.println("");
        }
        System.out.println("");

    }

    int p(int i) {
        if (Nodes.get(i).P == null) {
            return Nodes.get(i).L;
        }
        return Nodes.get(i).P.L;
    }

    int[] toIntArray(ArrayList<Integer> a) {
        int[] A = new int[a.size()];
        for (int i = 0; i < a.size(); i++) {
            A[i] = a.get(i);
        }
        return A;
    }

    int[] toIntArray(ArrayList<Integer> a, int append) {
        int[] A = new int[a.size() + 1];
        for (int i = 0; i < a.size(); i++) {
            A[i] = a.get(i);
        }
        A[a.size()] = append;
        return A;
    }

    int[] toIntArray(ArrayList<Integer> a, int MaxDelta, int append) {

        int size = MaxDelta + 1;
        int[] A = new int[size];
        for (int i = 0; i < size; i++) {
            if (a.size() > i) {
                A[i] = a.get(i);
            } else {
                A[i] = 0;
            }
        }

        A[size - 1] = append;
        return A;
    }

    int debug_node = 9;

    void findOptimalP2(int B) {
        A_1 = new double[Nodes.size()][B + 1][2];
        A_2 = new double[Nodes.size()][B + 1];
        A_3 = new double[Nodes.size()][B + 1];
        R_1 = new int[Nodes.size()][B + 1][0];
        R_2 = new int[Nodes.size()][B + 1][0];
        R_3 = new int[Nodes.size()][B + 1][0];

        // PrintL();
        // PrintAL();
        //  PrintDist();
        calculateP1(B);
        PrintA(A_1);
        PrintA(R_1);
        calculateP2(B);

        PrintA(A_2);
        PrintA(R_2);
    }

    void calculateP1(int B) {
        for (Node n : NodeButtomToTop) {
            int i = n.L;
            int Delta = n.C.size();

            for (int j = 0; j <= B; j++) {
                ArrayList<Integer> min_b;
                double min_c = Double.MAX_VALUE;
                if (LeafNodes.containsKey(i)) {
                    if (j == 0) {
                        A_1[i][j][0] = AL[i] * (dist[i] - dist[p(i)]);
                        A_1[i][j][1] = AL[i];
                        R_1[i][j] = toIntArray(new ArrayList<>(), MaxDelta, 0);

                    } else {
                        A_1[i][j][0] = 0;
                        A_1[i][j][1] = 0;
                        R_1[i][j] = toIntArray(new ArrayList<>(), MaxDelta, j);
                    }

                } else {
                    for (ArrayList<Integer> b : new GFG().PermsKB(Delta, j)) {

                        double p = 0;
                        double unblocked_load = 0;
                        //  System.out.println(Delta+":"+b);

                        if (i == debug_node) {
                            System.out.println("i: " + i + " j: " + j);
                        }
                        if (i == debug_node) {
                            System.out.println("b: " + b);
                        }
                        for (int k = 0; k < Delta; k++) {
                            Node C_K = n.C.get(k);
                            int c_k = C_K.L;
                            int b_k = b.get(k);
                            p = p + A_1[c_k][b_k][0]
                                    + A_1[c_k][b_k][1] * (dist[i] - dist[p(i)]);

                            if (i == debug_node) {
                                System.out.println("p: " + p);
                            }

                            unblocked_load += A_1[c_k][b_k][1];

                        }
                        unblocked_load += AL[i];

                        p = p + AL[i] * (dist[i] - dist[p(i)]);
                        if (i == debug_node) {
                            System.out.println("final p: " + p);
                        }
                        if (min_c > p) {
                            min_c = p;
                            min_b = new ArrayList<>(b);
                            A_1[i][j][1] = unblocked_load;
                            R_1[i][j] = toIntArray(b, MaxDelta, 0);
                            if (i == debug_node) {
                                System.out.println("R_1 option 2: " + Arrays.toString(R_1[i][j]));
                            }
                        }
                    }

                    for (ArrayList<Integer> b : new GFG().PermsKB(Delta, j - 1)) {
                        //  System.out.println(b);
                        double p = 0;
                        for (int k = 0; k < Delta; k++) {
                            Node C_K = n.C.get(k);
                            int c_k = C_K.L;
                            int b_k = b.get(k);
                            p = p + A_1[c_k][b_k][0];

                        }
                       // p = p +AL[i] * (dist[i] - dist[p(i)]);
                        if (min_c > p) {
                            min_c = p;
                            min_b = new ArrayList<>(b);
                            A_1[i][j][1] = 0;
                            R_1[i][j] = toIntArray(b, MaxDelta, 1);

                            if (i == debug_node) {
                                System.out.println("R_1 option 1: " + Arrays.toString(R_1[i][j]));
                            }
                        }

                    }
                    A_1[i][j][0] = min_c;
                }
            }
        }

    }

    void calculateP2(int B) {
        PrintAL();
        for (Node n : NodeButtomToTop) {
            int i = n.L;
            int Delta = n.C.size();
            for (int j = 0; j <= B; j++) {

                if (i == debug_node) {
                    System.out.println("**************** i: " + i + " j: " + j+"*****************");
                }

                double min_c = Double.MAX_VALUE;
                if (LeafNodes.containsKey(i)) {
                    if (j == 0) {
                        A_2[i][j] = 0;
                        if (AL[i] > 0) {
                            A_2[i][j] = INF;
                        }
                        R_2[i][j] = toIntArray(new ArrayList<>(), MaxDelta, 0);

                    } else {
                        A_2[i][j] = 0;
                        R_2[i][j] = toIntArray(new ArrayList<>(), MaxDelta, j);
                    }

                } else {
                    if (AL[i] ==0) {
                        for (ArrayList<Integer> b : new GFG().PermsKB(Delta, j)) {
                            double p = 0;
                            //  System.out.println(Delta+":"+b);

                            for (int k = 0; k < Delta; k++) {
                                Node C_K = n.C.get(k);
                                int c_k = C_K.L;
                                int b_k = b.get(k);
                                p = p + A_2[c_k][b_k];

                            }
                            if (i == debug_node) {
                                System.out.println("option 2 final p: " + p);
                            }
                            if (i == debug_node) {
                                  System.out.println("p: " + p+ " min_c: "+ min_c);
                            }
                            if (min_c > p) {
                                min_c = p;
                                R_2[i][j] = toIntArray(b, MaxDelta, 0);
                                if (i == debug_node) {
                                    System.out.println("R_2 option 2: " + Arrays.toString(R_2[i][j]));
                                }
                            }
                        }
                    }
                    for (ArrayList<Integer> b : new GFG().PermsKB(Delta, j - 1)) {
                        if (i == debug_node) {
                            System.out.println("b: " + b);
                        }
                        double p = 0;
                        for (int k = 0; k < Delta; k++) {
                            Node C_K = n.C.get(k);
                            int c_k = C_K.L;
                            int b_k = b.get(k);
                            p = p + A_1[c_k][b_k][0];

                        }

                        if (i == debug_node) {
                            System.out.println("p: " + p+ " min_c: "+ min_c);
                        }
                        if (min_c > p) {
                            min_c = p;

                            R_2[i][j] = toIntArray(b, MaxDelta, 1);
                            if (i == debug_node) {
                                System.out.println("R_2 option 1: " + Arrays.toString(R_2[i][j]));
                            }
                        }
                    }
                    if (i == debug_node) {
                        System.out.println("min c: " + min_c);
                    }

                    A_2[i][j] = min_c;
                }
            }
        }

    }

    
    void calculateP3(int B) {
        PrintAL();
        for (Node n : NodeButtomToTop) {
            int i = n.L;
            int Delta = n.C.size();
            for (int j = 0; j <= B; j++) {

                if (i == debug_node) {
                    System.out.println("**************** i: " + i + " j: " + j+"*****************");
                }

                double min_c = Double.MAX_VALUE;
                if (LeafNodes.containsKey(i)) {
                    if (j == 0) {
                        A_2[i][j] = 0;
                        if (AL[i] > 0) {
                            A_2[i][j] = INF;
                        }
                        R_2[i][j] = toIntArray(new ArrayList<>(), MaxDelta, 0);

                    } else {
                        A_2[i][j] = 0;
                        R_2[i][j] = toIntArray(new ArrayList<>(), MaxDelta, j);
                    }

                } else {
                    if (AL[i] ==0) {
                        for (ArrayList<Integer> b : new GFG().PermsKB(Delta, j)) {
                            double p = 0;
                            //  System.out.println(Delta+":"+b);

                            for (int k = 0; k < Delta; k++) {
                                Node C_K = n.C.get(k);
                                int c_k = C_K.L;
                                int b_k = b.get(k);
                                p = p + A_2[c_k][b_k];

                            }
                            if (i == debug_node) {
                                System.out.println("option 2 final p: " + p);
                            }
                            if (i == debug_node) {
                                  System.out.println("p: " + p+ " min_c: "+ min_c);
                            }
                            if (min_c > p) {
                                min_c = p;
                                R_2[i][j] = toIntArray(b, MaxDelta, 0);
                                if (i == debug_node) {
                                    System.out.println("R_2 option 2: " + Arrays.toString(R_2[i][j]));
                                }
                            }
                        }
                    }
                    for (ArrayList<Integer> b : new GFG().PermsKB(Delta, j - 1)) {
                        if (i == debug_node) {
                            System.out.println("b: " + b);
                        }
                        double p = 0;
                        for (int k = 0; k < Delta; k++) {
                            Node C_K = n.C.get(k);
                            int c_k = C_K.L;
                            int b_k = b.get(k);
                            p = p + A_1[c_k][b_k][0];

                        }

                        if (i == debug_node) {
                            System.out.println("p: " + p+ " min_c: "+ min_c);
                        }
                        if (min_c > p) {
                            min_c = p;

                            R_2[i][j] = toIntArray(b, MaxDelta, 1);
                            if (i == debug_node) {
                                System.out.println("R_2 option 1: " + Arrays.toString(R_2[i][j]));
                            }
                        }
                    }
                    if (i == debug_node) {
                        System.out.println("min c: " + min_c);
                    }

                    A_2[i][j] = min_c;
                }
            }
        }

    }
    
}
