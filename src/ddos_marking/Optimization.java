/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ddos_marking;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author rajor
 */
public class Optimization {

    Node root;
    int N;
    int MaxL;
    int[][] LCA;
    int[] D;
    HashMap<Integer, Node> Nodes = new HashMap<Integer, Node>();
    ArrayList<Node> NodesTopToButtom = new ArrayList<Node>();

    public Optimization(Node root) {
        this.root = root;
        ArrayList<Node> N = new ArrayList<Node>();
        N.add(root);
        MaxL = root.L;
        while (!N.isEmpty()) {
            Node n = N.remove(0);
            NodesTopToButtom.add(n);
            Nodes.put(n.L, n);
            N.addAll(n.C);
            if (MaxL < n.L) {
                MaxL = n.L;
            }
        }
        this.N = Nodes.size();
        LCA = new int[Math.max(this.N, MaxL + 1)][Math.max(this.N, MaxL + 1)];
        D = new int[Math.max(this.N, MaxL + 1)];
        CalculateD(root, 0);
    }

    void PrintN() {
        System.out.println(N);
        System.out.println(NodesTopToButtom.size());
    }

    void PrintLCA() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                System.out.print(LCA[i][j] + " ");
            }
            System.out.println("");
        }
    }

    public void CalculateLCA() {

        int[][] LCA = new int[this.N][this.N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                LCA[i][j] = Integer.MAX_VALUE;
            }
        }

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                Node A = NodesTopToButtom.get(i);
                Node B = NodesTopToButtom.get(j);
                //     System.out.println(A.L + "-" + B.L);

                if (A.L == B.L) {
                    LCA[i][j] = A.L;
                } else if ((A.P != null) && (A.P.L == B.L)) {
                    LCA[i][j] = B.L;
                } else if ((B.P != null) && (B.P.L == A.L)) {
                    LCA[i][j] = A.L;
                } else {
                    if (i != 0) {
                        LCA[i][j] = Math.min(LCA[i - 1][j], LCA[i][j]);
                    }
                    if (j != 0) {
                        LCA[i][j] = Math.min(LCA[i][j - 1], LCA[i][j]);
                    }
                    if ((i != 0) && (j != 0)) {
                        LCA[i][j] = Math.min(LCA[i - 1][j - 1], LCA[i][j]);
                    }
                }

            }
        }

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                this.LCA[NodesTopToButtom.get(i).L][NodesTopToButtom.get(j).L] = LCA[i][j];
            }
        }
    }

    public void initBAT() {
        for (Node n : NodesTopToButtom) {
            n.BAT = 0;
        }
    }

    public void CalculateD(Node n, int d) {
        D[n.L] = d;
        for (Node c : n.C) {
            CalculateD(c, d + 1);
        }

    }

    void PrintD() {
        System.out.println("distance from root to any node:");
        for (int i : D) {
            System.out.print(i + " ");
        }
        System.out.println("");
    }

    public void ComputeAL(HashMap<Integer, Integer> ALs) {
        for (int i : ALs.keySet()) {
            if (Nodes.containsKey(i)) {
                Nodes.get(i).AL = ALs.get(i);
            }
        }
    }

    public void ComputeUL(HashMap<Integer, Integer> ULs) {
        for (int i : ULs.keySet()) {
            if (Nodes.containsKey(i)) {
                Nodes.get(i).UL = ULs.get(i);
            }
        }
    }

    double Penalty(int a, int b) {
        double p = 0;
        Node A = Nodes.get(a);
        Node B = Nodes.get(b);
        int lca = LCA[a][b];
        p = A.BAT * (D[a] - D[lca]) + B.BAT * (D[b] - D[lca]);
        return p;
    }

    public ArrayList<Integer> FindAssignment(int B) {
        ArrayList<Integer> g = new ArrayList<Integer>();
        for (Node n : NodesTopToButtom) {
            if (n.AL > 0) {
                g.add(n.L);
                n.BAT = n.AL;
            }
        }
        System.out.println("in find ass g:" + g.size());

        while (g.size() > B) {
            double min_p = Double.MAX_VALUE;
            int min_i = Integer.MAX_VALUE, min_j = Integer.MAX_VALUE;
            for (int i = 0; i < g.size(); i++) {
                for (int j = i; j < g.size(); j++) {
                    if (i == j) {
                        continue;
                    }
                    if (Penalty(g.get(i), g.get(j)) < min_p) {
                        min_p = Penalty(g.get(i), g.get(j));
                        min_i = i;
                        min_j = j;
                    }
                }

            }

            Node A = Nodes.get(LCA[g.get(min_i)][g.get(min_j)]);
            A.BAT += min_p;

            System.out.println("merging: " + g.get(min_i) + " & " + g.get(min_j));
            g.remove(min_i);
            if (min_i < min_j) {
                g.remove(min_j - 1);
            } else {
                g.remove(min_j);
            }
            g.add(A.L);

        }
        return g;
    }

}
